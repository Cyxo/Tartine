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

- `src/main/java/aine/` — `Main` entrypoint, `apk/`, `dex/`, `runtime/`, `graphics/`
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
