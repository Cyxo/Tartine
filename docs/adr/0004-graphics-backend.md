# ADR-0004: Graphics backend - Skia (Skija) on LWJGL

- **Status**: Accepted (draw chain decided 2026-10-18; threading and the
  event channel landed 2026-11-02)
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
desktop VSync.

Threading and events are part of the seam, not deferred work:

- One host UI thread owns GLFW, the GL context, `glfwPollEvents` and the
  buffer swaps: the process main thread on macOS (the launchers pass
  `-XstartOnFirstThread`), a dedicated `tartine-display` thread elsewhere.
  Guest code always runs on its own main-looper thread (`tartine-guest` on
  macOS, the caller elsewhere) and never calls GLFW.
- Frames flow guest to display through `HostDisplay.postFrame`: the guest
  draws and reads back on its thread, the display thread uploads the newest
  frame as a GL texture and presents it at VSync. `postFrame` reports a
  closed window so a render loop can stop without polling.
- Events flow display to guest through `HostDisplay.Event`: GLFW input,
  resize and focus callbacks publish on the display thread, a buffering
  channel replays anything that arrives before registration, and the guest's
  listener posts each event to its `Looper` `MessageQueue`. `Looper.loop()`
  runs them on the looper thread; `Closed` is a normal event that quits the
  loop. Input values stay in GLFW space until the framework maps them to
  Android `KeyEvent`/`MotionEvent`.
- When the window goes away while guest work is still running, the macOS
  path signals `close()` and bounds the `guest.join()` so a guest that
  ignores the close event cannot hang the caller.

## Consequences

- No pure-GL UI drawing; UI content stays rasterized through Skia.
- Skija is Java's route to Skia; verify license row in THIRD-PARTY.
- Working only when a desktop window can be created: pluggable per OS.
- Backend is swappable: the renderer sits behind the `gfx-api`/`gfx-skija`
  seam (ADR-0007), so `android.*` never imports Skija.
- The event channel carries input but does not yet dispatch it into the view
  tree: ViewRootImpl-style focus/key/motion delivery and the GLFW-to-Android
  key mapping are still gaps.
