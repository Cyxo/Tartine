# ADR-0003: Virtual filesystem with tiered shared storage

- **Status**: Accepted (user-approved 2026-10-19)
- **Date**: decided 2026-10, tiers 2026-10-19

## Context

The original design mounted the user's real home directory read-write at
`/sdcard`, which breaks isolation (an app could read `~/.ssh` or overwrite
another app's installed APK) and violates Android's scoped-storage assumptions.

## Decision

`/sdcard` is a three-tier mount: (1) a private virtual card at
`<root>/sdcard` shared by all apps by default; (2) opt-in user-mapped
folders per app/globally with a read-only/read-write flag; (3) system file
pickers for everything else, with per-app grants. Whole-home mounting survives
only as an explicit, warn-then-allow, read-only-by-default advanced option.
The VFS seam enforces root exclusion, symlink/junction realpath checks, refusal
of dangerous roots, the read-only flag, and a fail-closed guest `java.io`/
`java.nio.file` bridge.

## Consequences

- Apps run isolated by default but can access shared media through mappings
  and pickers.
- `Vfs` lives in `tartine.runtime`; unmapped paths resolve to `null` and
  fail closed.
- Configuration via config file, with `TARTINE_SDCARD` / a CLI flag override.