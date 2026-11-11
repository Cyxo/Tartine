# ADR-0014: View attach, lifecycle and input dispatch path

- **Status**: Accepted
- **Date**: 2026-11-11

## Context

The `gui` command could show a Compose window, but it reached that state by
reflecting into androidx/Compose internals from `Main`: it forced the
composition, poked view fields, and drew the tree without a window root. The
framework had named gaps for the pieces a real Android window relies on — no
`ViewRootImpl`, no `View.AttachInfo`, no `dispatchAttachedToWindow`, no
activity lifecycle dispatch, and host events stopped at the Looper queue.
The audit's P4 asked for a genuine attach/lifecycle/input path built from the
framework's own window and view classes, so guest code (and androidx code
compiled against the SDK) runs the same handshake as on a device instead of
depending on host-side reflection pokes that rot with each library version.

The `ViewRootImpl` and input work exposed a translator bug on the same probe:
Compose's headless snapshot read a mixed `{int,ref}` DEX slot with `if-eqz`,
and the writer unboxed the live `StateRecord` reference as an int
(`ClassCastException`). The fix reads both views (null = zero, boxed number =
its value, other non-null reference = non-zero) and keeps a regression demo
in the fixture suite; it is a translation-correctness fix, not part of the
window model.

## Decision

**The framework owns the window root.** `android.view.ViewRootImpl` is a real
class in the framework module. `setView(decor)` sets the decor's
`mParent`, fills `View.AttachInfo` (window token, tree observer, root view,
visibility), calls `dispatchAttachedToWindow` on the tree, dispatches the
`ViewTreeObserver` window-attach/visibility callbacks, and requests layout.
`die()` detaches the tree and clears the parent. `ViewGroup` fans the attach
and detach dispatch out to its children, so `onAttachedToWindow` /
`onDetachedFromWindow` run in tree order.

**Traversals are host-driven but tree-shaped.** `ViewRootImpl.performTraversals`
measures the decor with `EXACTLY` specs at the framebuffer size, lays it out,
runs `dispatchOnGlobalLayout`, then dispatches `onPreDraw`/`onDraw` and draws.
`requestLayout` and view invalidation mark the root dirty; `Main`'s render
loop asks `tartineNeedsTraversal()` each VSync and only traverses when the
first frame, a layout request, or a dirty subtree says so. There is no
WindowManager or Binder round trip: the host loop is the scheduling seam, and
`Choreographer.pumpFrameCallbacks` stays the animation clock.

**Lifecycle is the framework Activity's own.** `Activity` exposes
`performCreate`/`performStart`/`performResume`/`performPause`/`performStop`/
`performDestroy`, each dispatching both the Application-level and
activity-local `ActivityLifecycleCallbacks` before/after the activity hook,
as AOSP does. `Main.txGuiRun` follows `ActivityThread.handleLaunchActivity` +
`handleResumeActivity`: create, start, resume, then `ViewRootImpl.setView`
(so `AbstractComposeView` starts composition from
`onAttachedToWindow`/`onMeasure`, as on device). Teardown is pause, stop,
`die`, destroy, each step best-effort so a guest gap does not mask the
render-loop outcome. Headless `run` keeps its "onCreate until the first gap"
probe shape.

**Input is translated at the host seam and dispatched by the tree.**
`tartine.HostInput` maps GLFW key codes, modifiers, buttons and cursor
coordinates (scaled by the window content scale) to real
`android.view.KeyEvent`/`MotionEvent` instances. It delivers them through
`ViewRootImpl.dispatchTouchEvent` / `dispatchKeyEvent` /
`dispatchGenericMotionEvent` / `dispatchHoverEvent`, which call the ordinary
`View.dispatch*` chain: `ViewGroup` touch-target tracking, interception,
click/focus/hover handling, and `onKeyDown`/`onKeyUp`. `windowFocusChanged`
from the host focus event updates `AttachInfo.mHasWindowFocus` and fans out
to the tree and the observer. Events are posted to the guest looper and run
synchronously on the guest thread; there is no input batching.

**Unmodelled subsystems terminate at the framework seam.** The shell has no
window-insets model, so `View.requestApplyInsets` walks to the parent and
`ViewRootImpl.requestFitSystemWindows` is the terminating no-op; Compose's
insets listeners are registered and invoked, but receive no real insets.
Outline, shadow color, render-effect and layer-type state is stored on
`View`/`RenderNode` so guest property traffic round-trips; the raster path
does not consume transforms, alpha, layers or shadows yet. Telling the truth
at these seams keeps the guest-side API alive without a second, fake
implementation of the missing subsystem.

**`Main` no longer reflects into androidx.** `txGuiRun` only starts the
framework lifecycle, creates the `ViewRootImpl`, attaches the decor, wires
`HostInput` and the event queue, and runs the render loop.

## Consequences

- Compose and ordinary SDK views go through the same attach/measure/layout/
  draw path as on a device; the host driver is the only substitution.
  No host-side reflection into library internals remains in the GUI path.
  The committed Compose probe renders and animates through it (a run drew
  254 frames before the host auto-closed the window).
- Window focus, mouse clicks, hover, scroll wheel and keyboard events reach
  the guest view tree through `ViewRootImpl`, so apps that gate behavior on
  click/key/focus now work; text input still uses key events plus a raw
  typed-text fallback, not `InputConnection`/IME.
- The traversal seam is explicit: `tartineNeedsTraversal()` is the only
  redraw predicate, which keeps the idle screen at one blit and makes a
  wedged draw fail once instead of every VSync.
- Not covered here: WindowManager/Binder window tokens and real
  `WindowManager.LayoutParams`, window insets and system-bar/display-cutout
  values, multi-window z-order, input batching/`InputConnection`/IME,
  drag-and-drop, and rendering effects for transforms/alpha/layers/outlines.
  They attach to the same seams when their subsystems land.
- The translator fix is pinned by `mixedZeroDemo` in the fixture suite so
  mixed `{int,ref}` null tests cannot regress into an unbox.
