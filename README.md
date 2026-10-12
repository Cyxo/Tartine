# Tartine - Android Runtime

> **Tartine** *(noun)*: A slice of bread topped with sweet or savoury spreadable food such as butter, jam, honey, cream, or sauce.

Tartine Android RunTime Is Not an Emulator.

Tartine is a tool to run Android apps on Desktop OSes natively, in a similar fashion to [Wine](https://www.winehq.org/).

## Prerequisites

- [mise](https://mise.jdx.dev/). JDK 18, Gradle, and PowerShell install
  automatically from `mise.toml` on first verify (`mise install` is
  idempotent). No manual toolchain setup. Code must stay compatible with
  Java 14: `build.gradle` enforces `options.release = 14`.
- After editing `mise.toml`, run `mise trust` (mise distrusts modified
  configs).

## Layout

- `src/main/java/tartine/` — `Main` entrypoint, `apk/`, `dex/`, `vm/`, `runtime/`, `graphics/`
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
mise exec -- gradle test --tests "tartine.apk.*"
```

## Headless APK probes (no GUI yet)

```sh
mise run apk [apks/app-debug.apk]   # manifest, code, and library summary
mise run run [apks/app-debug.apk]   # launcher onCreate until the first gap
mise run dump <file.dex>            # instruction dump of one dex file
```

`mise tasks ls` lists everything; `mise run <task> --help` shows args.
Launchers reuse the last `mise run build` output via direct `java`
(no recompile when unchanged).
