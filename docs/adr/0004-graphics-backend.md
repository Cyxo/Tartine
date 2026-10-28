# ADR-0004: Graphics backend — Skia (Skija) on LWJGL

- **Status**: Accepted (draw chain decided 2026-10-18)
- **Date**: decided 2026-10-18

## Context

Android's graphics stack is Skia 2D + EGL/GLES + SurfaceFlinger. JavaFX is a
widget toolkit at the wrong layer and cannot host Surfaces/HWC semantics.
LWJGL supplies windowing + GL but no 2D engine.

## Decision

Rasterize UI `Canvas` draws with Skia via the JetBrains Skija bindings
(`io.github.humbleui:skija-*` 0.143.18) and present through LWJGL as a
software raster uploaded to a GL texture. LWJGL stays windowing + blit + the
future GLES/`ANativeWindow` path. One Android Window/Surface maps to one
desktop window; a minimal WindowManager + SurfaceFlinger-lite paces frames off
desktop VSync; a dedicated host UI thread owns GLFW behind a `HostDisplay`
interface (main thread on macOS).

## Consequences

- No pure-GL UI drawing; UI content stays rasterized through Skia.
- Skija is Java's route to Skia; verify license row in THIRD-PARTY.
- Working only when a desktop window can be created: pluggable per OS.
- Backend stays swappable later, at the cost of decoupling `android.*` from
  Skija (tracked as deferred work).