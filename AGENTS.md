# AGENTS.md

## Stack
- Java only. Compile with JDK 18, compatibility floor Java 14.
- Use `javac --release 14` (not just `source`/`target`) so APIs newer than 14 fail the build.
- Do not use language features or JDK APIs introduced after Java 14. Verify with `javap -verbose` / `jdeps` if unsure.

## Repo state
- Layout: `src/main/java/aine/` (`Main`, `dex/`, `runtime/`, `graphics/`), `src/test/java/` (hand-rolled `main`-based tests, no test deps), `scripts/`, `out/` (ignored build output). No build tool; plain `javac` only.
- Verify (compiles main + tests with `--release 14`, runs tests, runs `aine.Main`):
  - Windows: `powershell -ExecutionPolicy Bypass -File scripts/verify.ps1`
  - Mac/Linux: `sh scripts/verify.sh`

## Goal
- Run Android APKs natively on desktop (Windows/Mac/Linux): no QEMU, no container/VM image (i.e. not Anbox/Waydroid/Emulator). Wine-like translation layer instead.
- Reference prior art before designing: GitLab `android_translation_layer/android_translation_layer` (bionic_translation + art_standalone, Linux-only).

## APK to execution pipeline
- APK is a signed ZIP: `AndroidManifest.xml` (binary XML, use `aapt2 dump`/`apktool`), `classes.dex` (+`classes2.dex`… multidex), `resources.arsc`, `res/`, `assets/`, `lib/<abi>/*.so`, `META-INF/`.
- DEX != JVM bytecode, never load it directly: register-based (frames fixed at creation, up to v65535, typically <16), 16-bit code units, whole-app shared pools (string/type/proto/field/method); JVM is stack-based per-class files with ~different opcodes. Spec: `source.android.com/docs/core/runtime/dalvik-bytecode`, `/dex-format`.
- Dalvik (old JIT) is superseded by ART (AOT via on-device `dex2oat` + JIT/GC); both execute DEX so Dalvik-targeted apps run on ART with edge cases. Stock ART is not desktop-portable: it depends on Bionic, Binder IPC, Zygote fork model, system services.
- Pick one VM strategy and document it here; options in preference order:
  1. Port ART/Dalvik (precedent: AOSP `platform/dalvik` porting-guide portable interpreter, community `art_standalone`, `dalvikvm-multiplatform`).
  2. DEX interpreter in Java (simplest, slowest; good bootstrap).
  3. DEX→JVM bytecode translator (register→locals+operand stack + type inference for untyped moves; precedent: Enjarify preferred over lossy dex2jar, IcedRobot/Daneel).
- Whichever VM is chosen, `android.jar` is stubs only: Activity/lifecycle, `AssetManager`/resources, Binder-based services, Bionic/libc shims, and `lib/<abi>` native libs (ARM `.so` needs same-arch APK or binary translation) must each be reimplemented or explicitly stubbed.

## Graphics ("windows") mapping
- Android path, bottom-up: `Canvas` (Skia 2D) / GLES-via-EGL(`ANativeWindow`) / Vulkan → `Surface` (BufferQueue producer) → `SurfaceFlinger` (VSync-driven system compositor, HWC/OpenGL) → display. `WindowManager` owns Window/View lifecycle, z-order, transforms, and hands layers (`Surface`+`SurfaceControl`) to SurfaceFlinger. `SurfaceView` gets its own layer (isolated); `TextureView` composites inside app GLES. Spec: `source.android.com/docs/core/graphics/architecture`, `/surfaceflinger-windowmanager`.
- Desktop mapping: reimplement a minimal WindowManager + SurfaceFlinger-lite — one Android Window/Surface maps to one desktop window; bridge BufferQueue/gralloc to desktop buffers; pace frames off desktop VSync (Choreographer equivalent).
- Rendering backend: prefer Skia (same engine backing Android `Canvas`, cross-platform C++) or LWJGL + GLES via ANGLE for the GLES path. Do not use JavaFX as the compositor: it is a widget toolkit at the wrong layer and cannot faithfully host Surfaces/HWC semantics; low-level windowing (GLFW/SDL) + GL/Vulkan is the correct substrate.

## Commits
- Conventional Commits required (`feat:`, `fix:`, `chore:`, etc.). No other convention is defined.
