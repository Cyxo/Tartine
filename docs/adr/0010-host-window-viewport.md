# ADR-0010: Host window viewport and guest density

- **Status**: Accepted
- **Date**: 2026-11-04

## Context

The desktop mapping started with a fixed 480x960 mdpi raster: `Main` minted
that surface directly, `RasterPresenter` documented it as the 480x960-style
surface, and `Resources`/`DisplayMetrics` answered hardcoded 480x960 px at
160 dpi. The host window was already resizable, but resizing only changed the
letterboxed blit: guest measure/layout stayed on the 480x960 grid, and HiDPI
monitors reported mdpi metrics even though the window drew more device pixels.
That makes dp values wrong on HiDPI screens and makes a maximized window
render a phone-sized layout in the middle of the desktop.

The window is the display device here, so its framebuffer and content scale
are the source of truth for the guest viewport and density. A device also
reports that geometry through `Configuration`/`DisplayMetrics`, so the
framework seam needs to carry it.

## Decision

**Host metrics.** `HostDisplay.WindowMetrics(width, height, contentScale)`
carries the drawable framebuffer size in pixels plus the desktop content
scale; `WindowMetrics.DEFAULT` is the historical 480x960 px at 1.0 scale.
`HostDisplay.awaitMetrics()` lets the guest block on the window existing;
fakes and headless runs return `DEFAULT` through the default method.

GLFW implements it concretely:

- `awaitMetrics()` blocks until `glfwCreateWindow` has succeeded (a latch),
  then fails loudly if the display failed or closed first.
- The initial size is `glfwGetFramebufferSize` (pixels, not window points)
  and the scale is the maximum component of `glfwGetWindowContentScale`.
  A zero/absent scale falls back to 1.0, and a not-yet-laid-out 0x0
  framebuffer falls back to 480x960.
- `Resized` now comes from `glfwSetFramebufferSizeCallback`, so its
  width/height are already guest pixels; no point-to-pixel conversion sits
  between the host and the viewport.

**Framework seam.** `tartine.fw.DisplayConfig` holds the chosen width,
height and density in the framework module. `Main` writes the awaited
metrics before guest code launches; `Resources` builds both the device
`Configuration` and `DisplayMetrics` from it:

- `densityDpi = round(contentScale * 160)`, `density = densityDpi / 160`,
  so a 2x window reports 320 dpi and keeps the same dp size as an mdpi
  window with twice the pixels.
- `screenWidthDp`/`screenHeightDp`/`smallestScreenWidthDp` and the
  orientation derive from the pixel viewport and density.
- `screenLayout` follows AOSP's `Configuration.reduceScreenLayout`
  thresholds (small/normal/large/xlarge, long, legacy size-compat bits) and
  starts from LTR, because the shell has no locale or RTL configuration.
  This is the size class apps read from `Configuration`; it does not yet
  choose a different resource set.
- `DisplayConfig` defaults to 480x960 px mdpi so framework-only tests and
  no-window runs keep their historical numbers.

**Resize path.** `tartine.graphics.RenderTarget` is the replaceable raster
target; `GuestSurface` owns one `GfxSurface` plus the framework `Canvas`
bound to it and swaps the pair together on resize (closing the old surface;
`close()` is idempotent). `Main.postHostEvent` queues the resize onto the
guest looper, updates `DisplayConfig.setSize`, and calls `GuestSurface.resize`
on the guest thread. `RenderLoop` asks the target for its surface each tick,
detects a new identity or size, reallocates its readback array, and forces
one draw even without a frame callback or dirty view. `txDrawFrame` reads
width/height/canvas from the target every frame.

**Snapshot semantics.** `Resources` snapshots the configuration and metrics
when it is constructed; a live resize updates `DisplayConfig` (so future
snapshots see the new viewport) but does not mutate an existing guest
`Resources`. This mirrors a device delivering a configuration change rather
than rewriting live metrics in place: with no relaunch/activity-recreate path
yet, the running app keeps its startup configuration.

**Defaults and qualifiers.** With no config knob, the window still opens at
480x960 and scale 1.0, so behavior without a resize is unchanged. Qualifier
resource-set selection (`layout-sw600dp`, `<supports-screens>`, density
buckets for arbitrary resources) is deferred: the shell has no general
resource loader yet — `ResourceTable` only models density and sdkVersion, and
the sole consumer is the launcher-icon picker, which deliberately chooses the
densest bitmap because the host rescales it to 16/32/48 px. Parsing
`<supports-screens>` to clamp a resource set that does not exist would be dead
configuration. The dp size and `screenLayout` are now available for the
resource pipeline to consume when it lands; the host framebuffer stays the
layout source of truth, as on a device where the app does not own the screen.

## Consequences

- The guest viewport equals the window framebuffer: resizing reflows the app
  at the new pixel size on the next frame, and HiDPI windows keep their dp
  proportions via content-scale density.
- Startup no longer assumes 480x960: the initial frame is drawn at the real
  window size, and the default only applies when the host reports nothing.
- `Resources`/`DisplayMetrics` agree with the window at construction and
  `screenLayout`, dp sizes, and orientation now track it. Apps that wait for
  `onConfigurationChanged` do not receive resize events yet; a relaunch picks
  up the new snapshot.
- A monitor-scale change while the app is running is not re-read; density
  changes require a restart until the host reports scale changes as events.
- Qualifier-based resource selection and manifest `<supports-screens>`
  handling remain open, tied to the general resource loader; the viewport
  and density decisions do not depend on them.
