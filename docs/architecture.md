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
- VM strategy: **translate DEX to JVM bytecode** (`tartine.tx`), the only VM
  since 2026-10-29 (ADR-0008; interpreter-first per ADR-0001 is superseded).
  The translator emits classfile v49 (StackMapTable-free — the old
  type-inferencing verifier covers it) with `COMPUTE_FRAMES` and a
  DEX-hierarchy `getCommonSuperClass`; dex2jar/Enjarify are the oracles. It
  reached parity with the interpreter on the fixture app, so the interpreter
  (`tartine.vm`, `irun`/`igui`, `VmTest`) was deleted rather than maintained
  alongside. Unknown constructs surface as the framework skeleton's named gap
  (`UnsupportedOperationException("Tartine: not implemented: <signature>")`)
  — the gap-driven loop survives.
- The instruction model is a pre-resolved IR: `Insn.reference` is typed
  (`MethodRef`/`FieldRef`/`TypeRef`/`StringRef`) and payload users carry the
  decoded payload's index, so translation never re-parses smali-like text.
  `tartine.tx.ReferenceResolver` resolves those refs guest-dex-first, then by
  host reflection: it finds a field's real declaring class (including host
  ancestors), classifies interface owners, accepts signature-polymorphic
  `MethodHandle`/`VarHandle` calls and array owners, and powers `mise run refs`
  — an up-front census of unresolved references. The census reports; it does
  not gate lazy translation. During a probe wall, grep the census for the
  failing signature: a hit names the missing class/member to port, a miss
  points at behavior or translation rather than linkage.
- `android.jar` bodies are all `throw new RuntimeException("Stub!")`
  (verified via `javap -c`): reuse it for hierarchy/signatures/constants
  only, never behavior. Devices have no `android.jar` — they ship
  `framework.jar` with real DEX, but it needs natives/Binder/preloaded
  state and is not reusable on desktop; don't go there.
  **`android.jar` is a build input for skeleton regeneration only** (ADR-0008):
  the `fw-tools` module parses the LOCAL SDK jar (matching API **36**, the
  level Tartine models) into the `framework` module's android.* skeletons.
  The runtime never reads the jar. Maven's `com.google.android:android`
  artifact is API 16 and useless. True constants (`View.VISIBLE = 0`) read
  fine; runtime fields (`VERSION.SDK_INT`) have no value in the jar and are
  supplied by us (`android.os.Build.VERSION.SDK_INT = 36`). Even pure-Java
  utils (SparseArray) drag `com.android.internal` deps — port their semantics
  as framework Java, don't compile AOSP sources. (`d8` exists in local
  build-tools; compiling framework Java to DEX stays deferred — it needs full
  opcodes + JNI answers anyway.)
- `android.*` is reimplemented (not stubbed): Activity/lifecycle,
  `AssetManager`/resources, Binder-based services, Bionic/libc shims, and
  `lib/<abi>` native libs (ARM `.so` needs same-arch APK or binary
  translation) are ported into the `framework` module as each wall needs
  them. Framework-in-Java (below) is now the mechanism; the old "framework
  objects allocate as interpreter placeholders with no-op `<init>`" model
  is gone with `tartine.vm`.
- Translation resolves guest `android.*` against the `framework` module and
  guest `java.*` against the host JDK by ordinary JVM linkage: there is no
  runtime reflection seam anymore (deleted with the interpreter). The former
  interpreter allowlist/deny-set sandbox is historical; host-JDK access from
  guest code is currently unrestricted, and any future sandbox needs a
  ClassLoader-level mechanism, not a dispatch table.

## Framework in Java (2026-10 audit adoption, user-approved)

- The android.* framework is real Java classes. Skeletons carry exact
  signatures from the LOCAL SDK `android.jar` (generator, dev-time, in the
  `fw-tools` module) into a `framework` Gradle module; every unimplemented
  body throws
  `UnsupportedOperationException("Tartine: not implemented: <signature>")`
  — the gap-driven loop survives verbatim. Behavior is implemented in plain
  Java there; AOSP/Robolectric/layoutlib stay read-only references (never
  runtime deps).
- Dispatch is plain JVM linkage (no adapter): a translated guest class either
  overrides a framework method (its translated bytecode runs) or inherits the
  skeleton, which throws the named gap. Guest classes extending android.* are
  ordinary subclasses on the same classloader.
- New framework behavior is written in the framework module only.
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
  the class a wall needs instead of stubbing more of it. Run `mise run refs`
  first: a census hit names the missing linkage, a miss means the wall is
  behavior (or translation) rather than a missing symbol.
- Batch wall ports: when a wall's family has a clear shape (listener
  add/remove pairs, getters returning constants, service-name dispatch arms,
  no-op setters), port the WHOLE family from the AOSP source in one pass.
  Stop batching when shapes stop being uniform.
- Generator state: `SkeletonGen`/`Sdk`/`FwIndex`/`Imports` live in the
  dev-time `fw-tools` module (never on the runtime classpath); see AGENTS for
  current behavior and deletion+regen refresh rules. `SkeletonGen` runs the
  shared `Imports` pass over each emitted file, so regenerated skeletons use
  simple names + imports instead of fully-qualified names.
- Translator: ASM emitting classfile version 49 (no
  StackMapTable — the old type-inferencing verifier covers it), with
  dex2jar/Enjarify as oracles, `COMPUTE_FRAMES` and a DEX-hierarchy
  `getCommonSuperClass`. It is the only VM since 2026-10-29 (ADR-0008): the
  interpreter is gone.
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
- Backend: Skia via Skija (`io.github.humbleui:skija-*` 0.143.18) behind the
  `gfx-api`/`gfx-skija` seam (ADR-0007). `android.graphics.*` depends only on
  `gfx-api`; `gfx-skija` is the sole module importing `io.github.humbleui`
  and carries the platform natives. The host installs `SkijaBackend` once at
  boot and creates surfaces through `GfxBackend`, then blits the raster
  through LWJGL's GL context; no pure-GL drawing of UI content; LWJGL stays
  windowing + GL + the future GLES path. Threading: a dedicated host UI thread
  behind a `HostDisplay` interface (main thread on macOS via
  `-XstartOnFirstThread`); guest code posts frame updates through
  `HostDisplay`.
- First bridge landed (`f4f113c`): `DesktopWindow` uploads the first-draw
  raster (`GfxSurface.readPixels`) as a GL texture, letterboxes per
  VSync-paced swap. `RasterPresenter` mints the surface and binds it to a
  framework `Canvas`.
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

- Remove runtime dependency on a local Android SDK (done 2026-10-29: SDK
  tooling moved to the dev-time `fw-tools` module; `android.jar` is a
  regeneration input only — see ADR-0008 and the "APK to execution pipeline"
  section).
- Decouple Skija from `android.*` (done 2026-10-29: `gfx-api`/`gfx-skija`
  seam, host-installed backend, ArchUnit guard; see ADR-0007).
- Adopt import convention (done 2026-10-30): simple names + imports, with a
  fully-qualified name kept only where a simple-name clash makes an import
  impossible. The clash-aware `tartine.sdk.Imports` pass runs inside
  `SkeletonGen` and over the committed sources via `fwShorten` (idempotent).
- Move generator (`SkeletonGen`, `Sdk`, `FwIndex`, `FwIndexLoader`) out of the
  runtime source set into a tools module (done 2026-10-29: `fw-tools`).
- Split `tartine.fw` across modules (done 2026-10-30): the runtime
  `tartine.fw` (system color/resource tables) lives only in `framework`;
  the dev-time SDK tooling moved to `tartine.sdk` in `fw-tools`.
- Add tests inside `framework/` (done 2026-10-29: 86 native-free tests in
  `framework/src/test/java/`; root `test` depends on `:framework:test`).
- Split `Interpreter.java` and move to a pre-resolved IR: moot — the
  interpreter was removed 2026-10-29 (ADR-0008). Its successor work lives in
  the translation path (below).
- Translation still has its own work: internal IR (done 2026-10-31: typed
  `Insn.Ref` values — method/field/type/string — plus payload indices, so the
  writer never re-parses reference text); resolving references at translation
  time (done 2026-10-31: `ReferenceResolver` walks guest dex then host
  reflection, retargets fields to their declaring class and feeds the `refs`
  census that reports unresolved references up front); lazy per-class
  translation (maybe, for very large multi-dex apps); caching translated
  output (even more relevant — keyed by APK hash + translator version +
  framework version; deferred until the installation process).
- Split `AGENTS.md` into further ADRs as needed.
- Move root `icon.png`/`icon.svg`/`logo.png` to `docs/assets/` (done 2026-10-30; see task 9).
- Scheme: Theme sync (host light/dark → guest `Configuration.uiMode`).
