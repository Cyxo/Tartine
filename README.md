# Tartine - Android Runtime

<p align="center">
  <img src="./logo.png" />
</p>

> **Tartine** *(noun)*: A slice of bread topped with sweet or savoury spreadable food such as butter, jam, honey, cream, or sauce.

### Tartine Android RunTime Is Not an Emulator.

Tartine is a tool to run Android apps on Desktop OSes natively, in a similar fashion to [Wine](https://www.winehq.org/).

## Scope

- **Supported target: pure-DEX apps.** Java/Kotlin/Compose apps whose APK ships no native code. Apps bundling ARM `.so` libraries (Flutter, most games) are out of reach for now: a desktop JVM cannot load Bionic-built ARM binaries.
- **Status:** the headless pipeline drives a real Compose APK through manifest → DEX → VM into lifecycle, composition, and the first measure pass. The first rendered frame now blits into a real desktop window (Skia raster through the LWJGL GL context — `mise run gui`); next milestone: the ASM translation spike. Design decisions and roadmap live in `AGENTS.md`.

## Prerequisites

- [mise](https://mise.jdx.dev/). JDK and Gradle install automatically
  from `mise.toml` on first verify (`mise install` is
  idempotent). No manual toolchain setup. Code must stay compatible with
  Java 21: `build.gradle` enforces `options.release = 21`.
- After editing `mise.toml`, run `mise trust` (mise distrusts modified
  configs).
- **Dev-time oracle JARs** (baksmali, apktool) are not in the repo.
  Fetch them once after cloning:

  ```sh
  sh scripts/fetch-tools.sh
  ```

## Verify

```sh
sh scripts/verify.sh
```

Trusts + installs toolchains via mise, then runs `gradle test`.
Single test/class:

```sh
mise exec -- gradle test --tests "tartine.apk.*"
```

## Headless APK probes (no GUI yet)

```sh
mise run apk [apks/app-debug.apk]              # manifest, code, and library summary
mise run run [apks/app-debug.apk]              # launcher onCreate until the first gap
mise run gui [apks/app-debug.apk] [timeout]   # GUI probe: show the activity window
mise run dump <file.dex>                       # instruction dump of one dex file
```

`mise tasks ls` lists everything; `mise run <task> --help` shows args.
Launchers reuse the last `mise run build` output via direct `java`
(no recompile when unchanged).

## Configuration

An optional `config.properties` lives in the per-user Tartine folder (the
same one the virtual filesystem uses). A missing file means all defaults.
It is host-side only: a guest never sees the file itself, only the values
it changes.

Location (whole folder moves with the `TARTINE_HOME` env or the
`tartine.home` system property):

| OS | Path |
|---|---|
| Windows | `%LOCALAPPDATA%\Tartine\config.properties` |
| macOS | `~/Library/Application Support/Tartine/config.properties` |
| Linux | `~/.local/share/tartine/config.properties` (respects `$XDG_DATA_HOME`) |

Keys (unknown keys or bad values are warned on stderr and skipped):

```properties
# One Android system color resource id per line: overrides what the host
# answers when an app calls getColor. Android hex notation works
# (#RRGGBB answers opaque, #AARRGGBB literal), as do 0xAARRGGBB or
# plain decimal. Ids are the android.R.color stub constants from the
# LOCAL SDK android.jar (for example `color.17170538` is the light
# surface tone the sample app paints its background with,
# `color.17170472` its light ink).
color.17170538 = #102433
color.17170472 = #E7EBEF

# Comma-separated desktop font families, tried in order when a Paint has
# no explicit typeface. Precedence: this key > TARTINE_FONT_FAMILIES env
# > built-in list (Roboto, Segoe UI, Noto Sans, DejaVu Sans,
# Liberation Sans, Helvetica Neue).
font.families = Segoe UI, Roboto
```

Environment knobs:

- `TARTINE_HOME` / `tartine.home` - move the root folder itself (config
  file included).
- `TARTINE_FONT_FAMILIES` - same family list as `font.families` (the
  config file wins).
- `TARTINE_MAX_STEPS` - caps the interpreter step counter for the
  headless probes (dev tool).

Planned keys (decided in `AGENTS.md`, not wired yet): `sdcard.*` tier
configuration (private virtual card default, per-folder read-only or
read-write maps, file-pick grants) lands with the sdcard tier work. Every
configuration knob documents here in the same commit that lands it
(`AGENTS.md` commits rule).
