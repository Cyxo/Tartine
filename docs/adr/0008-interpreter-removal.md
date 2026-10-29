# ADR-0008: Remove the interpreter, keep translation as the only VM

- **Status**: Accepted
- **Date**: 2026-10-29

## Context

ADR-0001 chose interpreter-first, and ADR-0006 made translation the default
`run`/`gui` path while keeping the interpreter under `irun`/`igui` for
comparison. The translation path then reached parity for the fixture app and
became the only one that can run real APK code end to end (the interpreter's
`VmObject` field mirrors and single-universe reflection never matched how a
real JVM resolves android.* / java.* / bundled libraries).

Maintaining two VMs split the test signal: `VmTest` asserted interpreter
builtins (a behavior no device has), while the framework module is written
against real Java dispatch. Every new framework behavior had to satisfy both.

## Decision

Delete the interpreter (`tartine.vm`, its `Interpreter`/`Vm*` classes, `irun`/
`igui`, and `VmTest`). Translation (`tartine.tx`) is the sole VM.
`TranslatedVmTest` re-points the fixture coverage at translated JVM bytecode
and asserts real-JVM results.

Move the SDK-only tooling (`Sdk`, `FwIndex`, `FwIndexLoader`, `SkeletonGen`)
out of the runtime source set into a new `fw-tools` Gradle module. The runtime
never reads `android.jar`; the jar is a build input for skeleton regeneration
only.

## Consequences

- Interpreter-only seams (framework index placeholders, `VmObject` mirrors,
  the VFS `File` bridge, interpreter security denials, single-universe
  reflection) are gone; the fixture demos that only exercised them are not
  asserted against real-JVM behavior they never matched (class-level rationale
  in `TranslatedVmTest`).
- `framework/` carries no `tartine.vm` import and no renderer import; the
  runtime carries no SDK dependency.
- `android.jar` (local face matching API 36) is a regeneration input:
  `fw-tools` parses it into skeletons; the shipped runtime reads only the
  generated `framework` classes, never the jar.
- The gap loop is unchanged: an unimplemented framework body throws
  `UnsupportedOperationException("Tartine: not implemented: <signature>")`.
- Tartine targets **API 36** (Android 16): `android.os.Build.VERSION.SDK_INT`
  reports 36.
