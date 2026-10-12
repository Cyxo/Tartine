# Aine

Run Android applications natively on desktop (Windows/Mac/Linux) via a
Wine-like translation layer. No emulator, no container image.

See `AGENTS.md` for the APK-to-execution pipeline and graphics mapping.

## Prerequisites

- [mise](https://mise.jdx.dev/). JDK 18 and Gradle install automatically
  from `mise.toml` on first verify (`mise install` is idempotent).
  No manual toolchain setup. Code must stay compatible with Java 14:
  `build.gradle` enforces `options.release = 14`.

## Layout

- `src/main/java/aine/` — `Main` entrypoint, `apk/`, `dex/`, `vm/`, `runtime/`, `graphics/`
- `src/test/java/` — JUnit 5 suite
- `settings.gradle` / `build.gradle` + `gradlew` wrapper
- `tools/` — dev-time CLI oracles (see `THIRD-PARTY.md`)
- `apks/` — local test fixtures, gitignored
- `build/` — Gradle output (ignored by git)

## Verify

Windows (PowerShell):

```powershell
powershell -ExecutionPolicy Bypass -File scripts/verify.ps1
```

Mac/Linux:

```sh
sh scripts/verify.sh
```

Both trust + install toolchains via mise, then run `gradle test`.
Single test/class:

```sh
mise exec -- gradle test --tests "aine.apk.*"
```

## Headless APK probes (no GUI yet)

Against a local APK in `apks/` (gitignored):

```sh
mise exec -- gradle run --args="apk apks/app-debug.apk"
mise exec -- gradle run --args="run apks/app-debug.apk"
mise exec -- gradle run --args="dump out/fixture-classes2.dex"
```

`apk` prints manifest, code, and library summary. `run` executes the
launcher `onCreate` until the first gap and reports it with the step
count. `dump` prints the instruction dump of one dex file.
