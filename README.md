# Aine

Run Android applications natively on desktop (Windows/Mac/Linux) via a
Wine-like translation layer. No emulator, no container image.

See `AGENTS.md` for the APK-to-execution pipeline and graphics mapping.

## Prerequisites

- JDK 18 (`java -version` should report 18). Code must stay compatible
  with Java 14: always compile with `javac --release 14`.

## Layout

- `src/main/java/aine/` — `Main` entrypoint, `dex/`, `runtime/`, `graphics/`
- `src/test/java/` — hand-rolled `main`-based tests (no test dependencies yet)
- `scripts/` — `verify.ps1` (Windows) / `verify.sh` (Mac/Linux)
- `libs/` / `tools/` — vendored jars / dev-time CLI oracles (see `THIRD-PARTY.md`)
- `apks/` — local test fixtures, gitignored
- `out/` — build output (ignored by git)

## Verify

Windows (PowerShell):

```powershell
powershell -ExecutionPolicy Bypass -File scripts/verify.ps1
```

Mac/Linux:

```sh
sh scripts/verify.sh
```

Both scripts compile main + tests with `javac --release 14`, run the
tests, and run `aine.Main`.
