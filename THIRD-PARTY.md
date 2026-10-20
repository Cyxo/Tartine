# Third-party components

Reuse-first per `AGENTS.md`. Compile deps are declared in `build.gradle`
and resolve from Maven Central. `tools/` holds dev-time CLI oracles
(downloaded on demand via `mise run fetch-tools`, gitignored, never on
the runtime classpath). `apks/` (gitignored) holds local test fixtures
only. Build/test toolchain pins live in `mise.toml` (Java, Gradle) and
`build.gradle` (JUnit).

## Runtime path (via Gradle / Central + Google Maven)

| Artifact | Version | Role | License |
|---|---|---|---|
| `com.android.tools.smali:smali-dexlib2` | 3.0.10 | DEX parse model (frontend for Step 2) | BSD 3-Clause |
| `com.google.guava:guava` (android flavor) | 31.1-android | dexlib2 runtime dep | Apache-2.0 |
| `com.google.code.findbugs:jsr305` | 3.0.2 | dexlib2 runtime dep (annotations) | Apache-2.0 |
| `org.lwjgl:lwjgl` + `lwjgl-glfw` + `lwjgl-opengl` (+ natives for windows/linux/macos/macos-arm64) | 3.4.3 | Desktop windowing (GLFW) + GL context for the GUI step | BSD-3-Clause |
| `io.github.humbleui:skija-shared` + `skija-<platform>` (windows x64/arm64, linux x64/arm64, macos x64/arm64) | 0.143.18 | Skia engine backing `android.graphics.Canvas` UI drawing (window blit via LWJGL GL) | Apache-2.0 |
| `com.github.weisj:jsvg` ([github.com/weisJ/jsvg](https://github.com/weisJ/jsvg)) | 2.2.0 | SVG renderer for Android vector drawables converted at icon resolution (window/taskbar icon) | MIT |
| `org.ow2.asm:asm` | 9.6 | JVM bytecode emitter for the DEX→JVM translation spike (`tartine.tx`) | BSD 3-Clause |

dexlib2 migrated 2026-10-17 from `org.smali:dexlib2` 2.5.2 (Central, `org.jf.*`
namespace) to the Google-maintained fork `com.android.tools.smali:smali-dexlib2`
3.0.10 (`com.android.tools.smali.*` namespace; requires the `google()`
repository). License verified at adoption time via the POM: BSD 3-Clause.
The fork reads newer DEX than 2.5.2's ceiling of 039; suite ran green with
byte-identical fixture output after the switch (pure namespace/coordinate
migration). Fact-check history 2026-10-17 (correcting this file and the
2026-10 audit both): the fork DOES publish Maven artifacts, but only on GOOGLE
Maven, not Maven Central, where `org.smali:dexlib2` tops out at 2.5.2 (last
update 2021).

## Test path (via Gradle / Maven Central)

| Artifact | Version | Role | License |
|---|---|---|---|
| `org.junit.jupiter:junit-jupiter` | 5.14.4 | JUnit 5 suite (`src/test/java`) | EPL-2.0 |

## Dev-time oracles (`tools/`)

Fetched by `mise run fetch-tools` (or `sh scripts/fetch-tools.sh` on Mac/Linux).
Files land in `tools/` which is gitignored. Size is verified on download.

| Artifact | Version | Role | Source | License |
|---|---|---|---|---|
| `baksmali-2.5.2.jar` | 2.5.2 | Golden disassembly oracle | Maven Central `org/smali/baksmali/2.5.2/` | BSD 3-Clause |
| `util-2.5.2.jar` | 2.5.2 | baksmali runtime dep | Maven Central `org/smali/util/2.5.2/` | BSD 3-Clause |
| `jcommander-1.64.jar` | 1.64 | baksmali runtime dep (CLI parsing) | Maven Central `com/beust/jcommander/1.64/` | Apache-2.0 |
| `apktool_3.0.3.jar` | 3.0.3 | Manifest/resource decode oracle (Step 1 cross-check) | `https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_3.0.3.jar` (no published checksum; size 15 478 013 bytes matched, `version` + fixture decode smoke-tested) | Apache-2.0 |

Example (Windows; `out/` is gitignored scratch):

```powershell
java -cp "tools/baksmali-2.5.2.jar;tools/util-2.5.2.jar;tools/jcommander-1.64.jar" `
  org.jf.baksmali.Main d out/fixture-classes2.dex -o out/baksmali-smoke
```

## Deferred (do not include yet)

- **`com.android.tools.build:apksig`** (Google Maven only, Apache-2.0;
  stable 8.13.2 as of 2026-10-17) — APK signature verification (v2/v3,
  v1 fallback) for the future install/daemon flow.
- **dex2jar** (Apache-2.0) / **Enjarify** (Apache-2.0, pure Python 3,
  archived; successor `Storyyeller/enjarify`) — translation oracles.
  Both translated the Step 3 fixture cleanly (fetch on demand, scratch-only;
  not included).
forge private classes. Analysis narrowly looks at request components.
- **AOSP ART** — reference only, nothing to embed. Skia's Java route is now
  embedded **via Skija** (`io.github.humbleui:skija-shared` +
  `skija-windows-x64` etc., 0.143.18, Apache-2.0 — POM license verified at
  adoption 2026-10-18), so binary Skia no longer sits in this deferred list;
  `skija-shared` transitively pulls `io.github.humbleui:types`.
