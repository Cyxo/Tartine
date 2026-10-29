# AGENTS.md

## Stack
- Java only. Toolchains via mise (`mise.toml`: Corretto 27 + Gradle 9.8.1); run everything through `mise exec -- gradle ...` so the pinned JDK is used. Scripts under `scripts/` are POSIX bash (Mac/Linux); Windows uses the mise `run_windows` pwsh twins (`scripts/*.ps1`). A Gradle wrapper is committed for IDEs/CI — keep its version in sync with mise's `gradle` pin.
- Java 21 compatibility floor, enforced as `options.release = 21` in `build.gradle` (raised from 14 per the 2026-10 audit; our sources may be new-JDK-built on Corretto 27 but must stay Java-21-loadable). Do not use language features or JDK APIs introduced after Java 21. Verify with `javap -verbose` / `jdeps` if unsure.
- Launch floor (follow-up audit, decided 2026-10-19): runtime launches require JDK 25+ LTS — bundled later; until then a present JDK ≥25 is required. The launcher flags already refuse older JVMs (`--sun-misc-unsafe-memory-access` exists only from JDK 24), so "Java 21" was never a launch floor; release=21 remains the bytecode/source compatibility floor only. Dev pin stays Corretto 27 via mise (non-LTS is fine for building; launches target 25 LTS).

## Repo state
- Layout: `src/main/java/tartine/` (`Main`, `apk/`, `config/`, `dex/`, `tx/` (the DEX→JVM translator, the only VM), `runtime/`, `graphics/`), `framework/` (Gradle module: generated android.* skeletons + Java behavior), `fw-tools/` (dev-time SDK tooling: `Sdk`, `FwIndex`, `FwIndexLoader`, `SkeletonGen`; never on the runtime classpath), `gfx-api/` (renderer seam, pure Java), `gfx-skija/` (Skija adapter: the only module importing `io.github.humbleui`; see ADR-0007),
`src/test/java/` (JUnit 5 suite), `settings.gradle`/`build.gradle` (+ committed Gradle wrapper — mise still pins the version; keep the two in sync), `build/` (ignored output). `tools/` (dev-time CLI oracles, gitignored — fetch with `mise run fetch-tools`), `apks/` (local fixtures — gitignored except the committed probe fixture `apks/app-debug.apk`, 23.5 MB, the one exception: fresh clones can probe without shipping their own; never re-ignore it), `THIRD-PARTY.md` (versions/licenses). Deps resolve from Maven Central + Google Maven (`google()` in `build.gradle`; smali-dexlib2 publishes only there) via Gradle; nothing vendored under `libs/`.
- `android.jar` (local SDK, API 36 — the level Tartine models) is a build input for skeleton regeneration only (`fw-tools`); the runtime never reads it. See ADR-0008.
- Verify: `sh scripts/verify.sh` (all platforms). Runs `mise install` (idempotent) then `gradle test`.
- Prefer `mise run <task>` (`fetch-tools`/`test`/`build`/`apk`/`run`/`gui`/`dump`/`fw-gen`, see `mise tasks ls`): `apk`/`run`/`gui`/`dump` launch the last build via direct `java`, no recompile when unchanged. Run `mise trust` after editing `mise.toml` (edits revoke trust).
- Single test: `mise exec -- gradle test --tests "tartine.apk.*"`.


## Goal and design

See [docs/architecture.md](docs/architecture.md) for the design (goal, APK ->
execution pipeline, VM strategy, framework-in-Java, virtual filesystem,
graphics mapping, installation/daemon/IPC) and [docs/adr/](docs/adr/) for the
architectural decision records. Design rationale was split out of this file
2026-10-30 so AGENTS.md stays focused on operational guidance.

## Commits
- Conventional Commits required (`feat:`, `fix:`, `chore:`, etc.). No other convention is defined.
- A commit adding or changing a user-facing configuration knob (config-file key, env var, CLI flag) updates README's Configuration section — and the AGENTS config bullet when the seam changes — in the same commit.

## Third-party policy
- Reuse-first: prefer OSS/AOSP over hand-rolled (dexlib2 for DEX, baksmali/apktool CLIs as decode oracles, dex2jar/Enjarify as translation oracles, LWJGL3 for windowing/GLES, AOSP ART/Skia as reference only).
- Download CLI oracles into `tools/` via `mise run fetch-tools` (gitignored, never on the runtime classpath); record version + URL + license in `THIRD-PARTY.md`. Compile deps are declared in `build.gradle` (Maven Central + Google Maven; see Repo state), not vendored.
- Only permissive licenses in the runtime path (Apache-2.0/BSD/MIT); keep license texts, never strip copyrights. Our sources stay at the `--release 21` floor regardless of what a vendored jar targets.
