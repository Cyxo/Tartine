# Tartine architecture notes

Design decisions and roadmap for the runtime. Operational guidance (toolchain,
verification, commit convention, third-party policy) stays in `AGENTS.md`;
this document and the `adr/` records capture the *why*.

## Goal

Run Android APKs natively on desktop (Windows/Mac/Linux): no QEMU, no
container/VM image (i.e. not Anbox/Waydroid/Emulator). Wine-like
translation layer instead. Reference prior art before designing:
GitLab `android_translation_layer/android_translation_layer`
(bionic_translation + art_standalone, Linux-only).

## APK to execution pipeline

- APK is a signed ZIP: `AndroidManifest.xml` (binary XML, use
  `aapt2 dump`/`apktool`), `classes.dex` (+`classes2.dex`… multidex),
  `resources.arsc`, `res/`, `assets/`, `lib/<abi>/*.so`, `META-INF/`.
- DEX != JVM bytecode, never load it directly: register-based (frames
  fixed at creation, up to v65535, typically <16), 16-bit code units,
  whole-app shared pools (string/type/proto/field/method); JVM is
  stack-based per-class files with ~different opcodes. Spec:
  `source.android.com/docs/core/runtime/dalvik-bytecode`, `/dex-format`.
- Dalvik (old JIT) is superseded by ART (AOT via on-device `dex2oat` +
  JIT/GC); both execute DEX so Dalvik-targeted apps run on ART with edge
  cases. Stock ART is not desktop-portable: it depends on Bionic, Binder
  IPC, Zygote fork model, system services.
- VM strategy (decided Step 3): interpreter-first.
  `tartine.vm.Interpreter` executes our IR directly; unknown opcodes and
  unimplemented methods throw `VmException` naming the gap, so each failure
  is the next task. The android.* framework must be reimplemented either
  way; the interpreter grows one opcode/test at a time, while translation
  (spiked via ASM 9.6 on the fixture: runs, but first attempt died in the
  JVM verifier, and type inference/frames/exceptions remain untouched) adds
  pitfalls without removing framework work. Audit 2026-10 revision
  (user-approved): framework behavior migrates into real Java classes (see
  "Framework in Java" below) and the translation spike runs right after the
  first Activity renders instead of "after apps run".
- `android.jar` bodies are all `throw new RuntimeException("Stub!")`
  (verified via `javap -c`): reuse it for hierarchy/signatures/constants
  only, never behavior. Devices have no `android.jar` — they ship
  `framework.jar` with real DEX, but it needs natives/Binder/preloaded
  state and is not reusable on desktop; don't go there. Parse the LOCAL SDK
  jar matching the APK (`<sdk>/platforms/android-<targetSdk>/android.jar`);
  Maven's `com.google.android:android` artifact is API 16 and useless.
  True constants (`View.VISIBLE = 0`) read fine; runtime fields
  (`VERSION.SDK_INT`) have no value in the jar and must be supplied by us.
  Even pure-Java utils (SparseArray) drag `com.android.internal` deps —
  port their semantics as host builtins, don't compile AOSP sources. (`d8`
  exists in local build-tools; compiling framework Java to DEX stays
  deferred — it needs full opcodes + JNI answers anyway.)
- Whichever VM is chosen, `android.jar` is stubs only: Activity/lifecycle,
  `AssetManager`/resources, Binder-based services, Bionic/libc shims, and
  `lib/<abi>` native libs (ARM `.so` needs same-arch APK or binary
  translation) must each be reimplemented or explicitly stubbed. Host
  builtins (`String`, atomics, …) carry value semantics. (The "framework
  objects allocate as placeholders with no-op `<init>`" model predates the
  2026-10 audit adoption — it is what we run today; the Framework-in-Java
  section supersedes it as we migrate.)
- `java.*` calls fall back to host reflection (table first for
  normalizations like boolean→int, then reflective dispatch on host objects
  only; model objects never cross). Scope is deny-by-default (landed
  2026-10-19): an explicit allowlist of modules/classes may cross. The landed
  shape: allowlist prefixes `java.lang` + `reflect`/`annotation`, `java.util`
  + `concurrent` (+`function`/`stream`)+`regex`, `java.text`, `java.math`,
  with the 9-class deny set (+`ProcessHandle`) overriding inside them;
  `java.io`/`java.nio`/`java.net` stay off the list (the `File` bridge
  builtins route through `tartine.runtime.Vfs` and fail closed); static
  calls gate the defining class, instance calls gate the receiver's runtime
  class (java.* receivers only — interpreter-internal models like
  `BoxedZero` stay reachable); `Class.forName`/`loadClass` resolve the
  dex+framework universe only, so `forName("java.lang.Runtime")` answers
  `ClassNotFoundException` (pinned by the fixture's `forNameDenied`).
  Allocation, methods, and statics resolve reflectively per call with no
  method cache — hand-write only behavioral mismatches, with a fixture test
  proving each.
- Interpreter architecture: `Interpreter.java` grew to ~8.1k lines with
  uncached reflection dispatch; split it (dispatch, reflection bridge,
  builtins registry, frame plumbing) and move to a pre-resolved IR —
  method/field/type references resolve once into cached host targets. The
  translation spike builds on the same resolution layer.

## Framework in Java (2026-10 audit adoption, user-approved)

- The android.* framework is real Java classes, not string-keyed builtins
  inside `tartine.vm.Interpreter`. Skeletons carry exact signatures from
  the LOCAL SDK `android.jar` (generator, dev-time) into a `framework`
  Gradle module; every unimplemented body throws
  `UnsupportedOperationException("Tartine: not implemented: <signature>")`
  — the gap-driven loop survives verbatim. Behavior is implemented in plain
  Java there; AOSP/Robolectric/layoutlib stay read-only references (never
  runtime deps).
- The interpreter becomes the adapter: (1) guest allocations of android.*
  classes construct host framework objects (fields move out of `VmObject`
  HashMap placeholders as each class ports); (2) framework dispatch checks
  guest override first (runs DEX), then reflective host invocation, else
  throws the skeleton's named gap; (3) guest classes extending android.*
  get generated peers whose overridden methods call back into the
  interpreter.
- Builtins stop growing: new framework behavior is written in the framework
  module only. Port opportunistically; delete a registration as soon as
  its class ports and tests prove parity.
- Never port framework behavior from memory. Read the AOSP source first and
  cite the exact file + method in the porting comment (see
  `ViewGroup.getChildAt`). Stale-memory ports are how `getChildAt`
  regressed: current AOSP returns null out of range; older releases threw.
  When memory and source disagree, source wins; pin the parity with a test.
- Local source refs (read-only, machine-local, never commit absolute local
  paths into tracked docs — they leak the username): the OpenCode temp root's
  `refs\` folder, i.e. `%LOCALAPPDATA%\Temp\opencode\refs\`. Shallow
  blobless `--sparse` clones, `core.autocrlf=false`. The dir sits under the
  opencode temp root, so the OS may purge it — re-clone if missing.
- Migration order follows the probe walls (measure → layout → draw): port
  the class a wall needs instead of adding another builtin.
- Batch wall ports: when a wall's family has a clear shape (listener
  add/remove pairs, getters returning constants, service-name dispatch arms,
  no-op setters), port the WHOLE family from the AOSP source in one pass.
  Stop batching when shapes stop being uniform.
- Generator state: see AGENTS for current `SkeletonGen` behavior and
  deletion+regen refresh rules.
- Translation spike: ASM translator emitting classfile version 49 (no
  StackMapTable — the old type-inferencing verifier covers it), with
  dex2jar/Enjarify as oracles, `COMPUTE_FRAMES` and a DEX-hierarchy
  `getCommonSuperClass`. Trigger was met (first Activity renders / first
  frame blits, `f4f113c`); the translated path is now the default (`run`/
  `gui`) while the interpreter stays as probing/debug tool.
- Scope cap: pure-DEX apps only. ARM `.so` (Bionic) can't load on desktop
  JVMs — Flutter, most games and native-code apps excluded until binary
  translation exists.
- References only, no runtime deps: Robolectric shadow patterns, AOSP
  layoutlib, Paparazzi as golden-frame oracle in a separate dev-time module.

## Virtual filesystem (decided 2026-10)

- Seam: `tartine.runtime.Vfs` maps guest absolute paths to host paths;
  unmapped paths resolve to `null` and the future java.io/java.nio.file
  guest bridge must fail closed (deny), never fall back to the host cwd.
- Root = app data, not config: Windows `%LOCALAPPDATA%\Tartine`, macOS
  `~/Library/Application Support/Tartine`, Linux
  `$XDG_DATA_HOME/tartine` (default `~/.local/share/tartine`);
  `TARTINE_HOME` env or `tartine.home` property override everything. The
  host config file (`config.properties`, loaded by
  `tartine.config.HostConfig`) sits at this root too — host-only, never
  guest-mapped. Keys: `color.<name|id>` and `font.families` (see README).
- Guest mounts (longest prefix wins): `/data/data/<pkg>` and
  `/data/user/0/<pkg>` (+ `files|cache|databases|shared_prefs`) →
  `<root>/data/<pkg>`; `/data/app/<pkg>` → `<root>/apps/<pkg>`; `/data/local/tmp` → `<root>/tmp`. `/sdcard` mounts follow the tiers below.
  `/system`, `/proc`, `/dev`, … stay unmapped.
- Traversal (`..`) and Windows-unsafe segments denied at the seam
  (`VfsTest`).
- Shared storage, three tiers (user-approved 2026-10-19):
  1. Default private virtual card at `<root>/sdcard`, shared by all apps,
     seeded with standard folders.
  2. Opt-in user-mapped folders, each read-only or read-write, globally
     or per app.
  3. File pickers (`ACTION_OPEN_DOCUMENT*`) as desktop dialogs, grants
     stored per app (scoped-storage apps don't expect broad sdcard access).
  Whole-home mounting survives only as an explicit advanced option
  (read-only by default, with a warning).
  Seam rules: mounts containing Tartine's own root must exclude it; resolve
  symlinks/junctions and realpath-check inside the mount; refuse dangerous
  roots; enforce read-only flag; config via file with `TARTINE_SDCARD`/CLI
  override.

## Graphics and viewport ("windows") mapping

- Android path bottom-up: `Canvas` (Skia 2D) / GLES-via-EGL
  (`ANativeWindow`) / Vulkan → `Surface` (BufferQueue producer) →
  `SurfaceFlinger` (VSync compositor, HWC/OpenGL) → display.
  `WindowManager` owns Window/View lifecycle, z-order, transforms and hands
  layers to SurfaceFlinger; `SurfaceView` gets its own layer; `TextureView`
  composites inside app GLES.
- Desktop mapping: minimal WindowManager + SurfaceFlinger-lite; one Android
  Window/Surface maps to one desktop window; bridge BufferQueue/gralloc to
  desktop buffers; pace frames off desktop VSync (Choreographer equivalent).
- Backend: Skia via Skija (`io.github.humbleui:skija-*` 0.143.18), first
  bridge being a software raster blitted through LWJGL's GL context; no
  pure-GL drawing of UI content; LWJGL stays windowing + GL + future GLES
  path. Threading: a dedicated host UI thread behind a `HostDisplay`
  interface (main thread on macOS via `-XstartOnFirstThread`); guest code
  posts frame updates through `HostDisplay`.
- First bridge landed (`f4f113c`): `DesktopWindow` uploads the first-draw
  Skia raster as a GL texture, letterboxes per VSync-paced swap.
- Window icon landed: `tartine.apk.AppIcon` resolves
  `android:icon`/`roundIcon` through `ResourceTable` and renders 16/32/48
  RGBA buffers; `<adaptive-icon>`/`<vector>` convert to SVG via jsvg;
  unconverted sources log an `icon:` gap and fall back to the OS default.

## Installation, daemon, IPC (design notes)

- One host JVM per guest app; app processes start on demand.
- Install on first run: verify signatures with `apksig` (v1/v2/v3), unpack
  into `<root>/apps/<pkg>`, record metadata in a package database
  (`versionCode`, signing certificate).
- Updates: `versionCode` must increase and signing certificate must match
  (watch v3 cert-lineage rotation).
- Daemon: per-user unprivileged background process owning install state
  and launch decisions; IPC over Unix-domain sockets (JDK 16+ AF_UNIX,
  Windows 10+); shortcuts point at it; AppCDS archives per app at install;
  Binder-style `Parcel` seam in-process first, then cross-JVM over the
  daemon socket.
- Scope cap (document in README): pure-DEX apps only — ARM `.so` (Bionic)
  can't load on desktop JVMs.

## References (read-only)

- AOSP layoutlib (native text/gfx), Robolectric shadow patterns (Loopers,
  system services), Paparazzi as possible golden-frame oracle in a separate
  dev-time module.
- Local source refs under `%LOCALAPPDATA%\Temp\opencode\refs\` (gitignored):
  `androidx\`, `aosp-frameworks-base\`. Shallow blobless `--sparse`
  clones; re-clone if purged.

## Deferred housekeeping (tracked, in order)

Carried from the 2026-10 audits; do later:

- Remove runtime dependency on a local Android SDK (FwIndex derived from
  the framework jar at build time).
- Decouple Skija from `android.*` (hidden public factory first, then
  delegates).
- Adopt import convention (FQNs only where names clash + generator
  clash-aware import manager + one-time shorten pass).
- Move generator (`SkeletonGen`, `Sdk`, `FwIndex`) out of the runtime
  source set into a tools module.
- Unify `tartine.fw` across the two modules (framework module owns it).
- Add tests inside `framework/`.
- Split `Interpreter.java` and move to a pre-resolved IR (also
  prerequisite for translation performance).
- Split `AGENTS.md` into further ADRs as needed.
- Move root `icon.png`/`icon.svg`/`logo.png` to `docs/assets/` (done 2026-10-30; see task 9).
- Scheme: Theme sync (host light/dark → guest `Configuration.uiMode`).
