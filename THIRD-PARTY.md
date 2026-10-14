# Third-party components

Reuse-first per `AGENTS.md`. Compile deps are declared in `build.gradle`
and resolve from Maven Central. `tools/` holds dev-time CLI oracles
(vendored, never on the runtime classpath). `apks/` (gitignored) holds
local test fixtures only. Build/test toolchain pins live in `mise.toml`
(Java, Gradle) and `build.gradle` (JUnit).

## Runtime path (via Gradle / Maven Central)

| Artifact | Version | Role | License |
|---|---|---|---|
| `org.smali:dexlib2` | 2.5.2 | DEX parse model (frontend for Step 2) | BSD 3-Clause |
| `com.google.guava:guava` (android flavor) | 27.1-android | dexlib2 runtime dep | Apache-2.0 |
| `com.google.code.findbugs:jsr305` | 3.0.2 | dexlib2 runtime dep (annotations) | Apache-2.0 |
| `org.lwjgl:lwjgl` + `lwjgl-glfw` + `lwjgl-opengl` (+ natives for windows/linux/macos/macos-arm64) | 3.4.3 | Desktop windowing (GLFW) + GL context for the GUI step | BSD-3-Clause |

dexlib2 2.5.2 reads DEX up to version 039 (verified: fixture `classes*.dex`
are 039 and disassemble cleanly). DEX 040+ needs smali 3.x, which publishes
no Maven artifacts — revisit if a fixture requires it.

## Test path (via Gradle / Maven Central)

| Artifact | Version | Role | License |
|---|---|---|---|
| `org.junit.jupiter:junit-jupiter` | 5.14.4 | JUnit 5 suite (`src/test/java`) | EPL-2.0 |

## Dev-time oracles (`tools/`)

| Artifact | Version | Role | Source | License |
|---|---|---|---|---|
| `baksmali-2.5.2.jar` | 2.5.2 | Golden disassembly oracle | Maven Central `org/smali/baksmali/2.5.2/` | BSD 3-Clause |
| `util-2.5.2.jar` | 2.5.2 | baksmali runtime dep | Maven Central `org/smali/util/2.5.2/` | BSD 3-Clause |
| `jcommander-1.64.jar` | 1.64 | baksmali runtime dep (CLI parsing) | Maven Central `com/beust/jcommander/1.64/` | Apache-2.0 |
| `apktool_3.0.3.jar` | 3.0.3 | Manifest/resource decode oracle (Step 1 cross-check) | `https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_3.0.3.jar` (no published checksum; size 15478013 bytes matched, `version` + fixture decode smoke-tested) | Apache-2.0 |

Example (Windows; `out/` is gitignored scratch):

```powershell
java -cp "tools/baksmali-2.5.2.jar;tools/util-2.5.2.jar;tools/jcommander-1.64.jar;libs/*" `
  org.jf.baksmali.Main d out/fixture-classes2.dex -o out/baksmali-smoke
```

## Deferred (do not vendor yet)

- **apktool** (Apache-2.0) — vendored as manifest/resource decode oracle.
- **dex2jar** (Apache-2.0) / **Enjarify** (Apache-2.0, pure Python 3,
  archived; successor `Storyyeller/enjarify`) — translation oracles.
  Both translated the Step 3 fixture cleanly (fetch on demand, scratch-only;
  not vendored).
- **AOSP ART / Skia** — reference only, nothing to embed.
