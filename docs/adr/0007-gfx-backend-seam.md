# ADR-0007: Renderer seam — gfx-api, gfx-skija, host-installed backend

- **Status**: Accepted
- **Date**: 2026-10-29

## Context

ADR-0004 fixed Skia (via Skija) as the 2D renderer, but the first bridge let
Skija leak into the framework: `android.graphics.Canvas` held
`io.github.humbleui` types, `Paint` built native paints and fonts, and the host
reached a package-private factory by reflection. That coupled the `android.*`
surface to one concrete renderer, kept Skija natives on the framework test
classpath, and made a backend swap invasive. ADR-0004 recorded the decoupling
as deferred work; the 2026-10 audit and this change resolve it.

## Decision

Split the renderer behind a pure-Java seam across three Gradle modules:

- `gfx-api` (package `tartine.gfx`): the seam only. `GfxBackend` plus opaque
  handles (`GfxCanvas`, `GfxSurface`, `GfxImage`, `GfxFont`, `GfxTypeface`,
  `GfxPicture`, `GfxPictureRecorder`) and values/enums (`GfxPaint`, `GfxRect`,
  `GfxFontMetrics`, `GfxClipMode`, `GfxPaintStyle`). No renderer dependency.
- `gfx-skija`: the only module importing `io.github.humbleui`. Adapts the
  seam to Skija and carries the platform natives.
- `framework`: depends only on `gfx-api`. `android.graphics.*` snapshots state
  into seam values and forwards primitives; it never imports a renderer or
  sees an `io.github.humbleui` type.

The host installs one backend once at boot
(`GfxBackend.install(new SkijaBackend())`); `GfxBackend.get()` fails loudly
when nothing is installed instead of silently rendering nothing. The host
creates surfaces through `GfxBackend` and reads pixels / decodes icons through
`GfxSurface`/`GfxImage`, so host graphics code is renderer-free as well. The
framework exposes one public host factory, `Canvas.tartineCreate(GfxCanvas)`;
the reflective bridge is gone and `SkiaPresenter` becomes `RasterPresenter`.

Text measurement moved with the seam: `Paint` caches one `GfxFont` per
resolved typeface + text size, closes the previous font when either changes,
and releases the last font through a `java.lang.ref.Cleaner` when the Paint is
collected (the resource bookkeeping `NativeAllocationRegistry` gives on
device).

An ArchUnit rule keeps the boundary: nothing in `android..` may depend on
`io.github.humbleui..` or on the `gfx-skija` package.

## Consequences

- A backend swap is a new module implementing `GfxBackend`; the framework and
  host stay untouched.
- Framework tests run native-free against the `gfx-api` recording backend.
- Behavior is preserved: `txgui` produced the same first-draw pixel hash
  (`2e24d86e0b1489cb`, 460800/460800 painted) before and after each step of the
  move.
- Resolves the ADR-0004 deferred item "decouple Skija from `android.*`".
