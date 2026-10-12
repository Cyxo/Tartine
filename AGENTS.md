# AGENTS.md

## Stack
- Java only. Toolchains via mise (`mise.toml`: Corretto 18 + Gradle); run everything through `mise exec -- gradle ...` so the pinned JDK is used.
- Java 14 compatibility floor, enforced as `options.release = 14` in `build.gradle`. Do not use language features or JDK APIs introduced after Java 14. Verify with `javap -verbose` / `jdeps` if unsure.

## Repo state
- Layout: `src/main/java/tartine/` (`Main`, `apk/`, `dex/`, `vm/`, `runtime/`, `graphics/`), `src/test/java/` (JUnit 5 suite), `settings.gradle`/`build.gradle` + wrapper, `build/` (ignored output). `tools/` (dev-time CLI oracles), `apks/` (ignored local fixtures), `THIRD-PARTY.md` (versions/licenses). Deps resolve from Maven Central via Gradle; nothing vendored under `libs/`.
- Verify: `powershell -ExecutionPolicy Bypass -File scripts/verify.ps1` (Windows) or `sh scripts/verify.sh` (Mac/Linux). Both run `mise install` (idempotent) then `gradle test`.
- Prefer `mise run <task>` (`test`/`build`/`apk`/`run`/`dump`, see `mise tasks ls`): `apk`/`run`/`dump` launch the last build via direct `java`, no recompile when unchanged. Run `mise trust` after editing `mise.toml` (edits revoke trust).
- Single test: `mise exec -- gradle test --tests "tartine.apk.*"`.

## Goal
- Run Android APKs natively on desktop (Windows/Mac/Linux): no QEMU, no container/VM image (i.e. not Anbox/Waydroid/Emulator). Wine-like translation layer instead.
- Reference prior art before designing: GitLab `android_translation_layer/android_translation_layer` (bionic_translation + art_standalone, Linux-only).

## APK to execution pipeline
- APK is a signed ZIP: `AndroidManifest.xml` (binary XML, use `aapt2 dump`/`apktool`), `classes.dex` (+`classes2.dex`… multidex), `resources.arsc`, `res/`, `assets/`, `lib/<abi>/*.so`, `META-INF/`.
- DEX != JVM bytecode, never load it directly: register-based (frames fixed at creation, up to v65535, typically <16), 16-bit code units, whole-app shared pools (string/type/proto/field/method); JVM is stack-based per-class files with ~different opcodes. Spec: `source.android.com/docs/core/runtime/dalvik-bytecode`, `/dex-format`.
- Dalvik (old JIT) is superseded by ART (AOT via on-device `dex2oat` + JIT/GC); both execute DEX so Dalvik-targeted apps run on ART with edge cases. Stock ART is not desktop-portable: it depends on Bionic, Binder IPC, Zygote fork model, system services.
- VM strategy (decided Step 3): interpreter-first. `tartine.vm.Interpreter` executes our IR directly; unknown opcodes and unimplemented methods throw `VmException` naming the gap, so each failure is the next task. The android.* framework must be reimplemented either way; the interpreter grows one opcode/test at a time, while translation (spiked via ASM 9.6 on the fixture: runs, but first attempt died in the JVM verifier, and type inference/frames/exceptions remain untouched) adds pitfalls without removing framework work. Revisit translation for performance only, after apps run.
- `android.jar` bodies are all `throw new RuntimeException("Stub!")` (verified via `javap -c`): reuse it for hierarchy/signatures/constants only, never behavior. Devices have no `android.jar` — they ship `framework.jar` with real DEX, but it needs natives/Binder/preloaded state and is not reusable on desktop; don't go there. Parse the LOCAL SDK jar matching the APK (`<sdk>/platforms/android-<targetSdk>/android.jar`); Maven's `com.google.android:android` artifact is API 16 and useless. True constants (`View.VISIBLE = 0`) read fine; runtime fields (`VERSION.SDK_INT`) have no value in the jar and must be supplied by us. Even pure-Java utils (SparseArray) drag `com.android.internal` deps — port their semantics as host builtins, don't compile AOSP sources. (`d8` exists in local build-tools; compiling framework Java to DEX stays deferred — it needs full opcodes + JNI answers anyway.)
- Whichever VM is chosen, `android.jar` is stubs only: Activity/lifecycle, `AssetManager`/resources, Binder-based services, Bionic/libc shims, and `lib/<abi>` native libs (ARM `.so` needs same-arch APK or binary translation) must each be reimplemented or explicitly stubbed.

## Graphics ("windows") mapping
- Android path, bottom-up: `Canvas` (Skia 2D) / GLES-via-EGL(`ANativeWindow`) / Vulkan → `Surface` (BufferQueue producer) → `SurfaceFlinger` (VSync-driven system compositor, HWC/OpenGL) → display. `WindowManager` owns Window/View lifecycle, z-order, transforms, and hands layers (`Surface`+`SurfaceControl`) to SurfaceFlinger. `SurfaceView` gets its own layer (isolated); `TextureView` composites inside app GLES. Spec: `source.android.com/docs/core/graphics/architecture`, `/surfaceflinger-windowmanager`.
- Desktop mapping: reimplement a minimal WindowManager + SurfaceFlinger-lite — one Android Window/Surface maps to one desktop window; bridge BufferQueue/gralloc to desktop buffers; pace frames off desktop VSync (Choreographer equivalent).
- Rendering backend: prefer Skia (same engine backing Android `Canvas`, cross-platform C++) or LWJGL + GLES via ANGLE for the GLES path. Do not use JavaFX as the compositor: it is a widget toolkit at the wrong layer and cannot faithfully host Surfaces/HWC semantics; low-level windowing (GLFW/SDL) + GL/Vulkan is the correct substrate.

## Commits
- Conventional Commits required (`feat:`, `fix:`, `chore:`, etc.). No other convention is defined.

## Third-party policy
- Reuse-first: prefer OSS/AOSP over hand-rolled (dexlib2 for DEX, baksmali/apktool CLIs as decode oracles, dex2jar/Enjarify as translation oracles, LWJGL3 for windowing/GLES, AOSP ART/Skia as reference only).
- Vendor CLI oracles under `tools/` (never on the runtime classpath); record version + URL + license in `THIRD-PARTY.md`. Compile deps are declared in `build.gradle` (Maven Central), not vendored.
- Only permissive licenses in the runtime path (Apache-2.0/BSD/MIT); keep license texts, never strip copyrights. Our sources stay `--release 14` regardless of what a vendored jar targets.
