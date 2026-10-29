# ADR-0002: Android framework implemented as real Java classes

- **Status**: Accepted (2026-10 audit)
- **Date**: 2026-10-17

## Context

The framework was originally string-keyed builtins in `Interpreter`. Bodies in
`android.jar` are all `throw new RuntimeException("Stub!")`, so signatures
exist but no behavior. Growing builtins does not scale and blocks the
translation path, which invokes real framework methods.

## Decision

Generate exact-signature skeletons (from the local SDK `android.jar`) into a
`framework` Gradle module; every unimplemented body throws a named gap; behavior
is implemented in plain Java against the AOSP source (never from memory), citing
file + method. Skeletons refresh by deletion + regen.

## Consequences

- `android.*` becomes a normal library on the classpath.
- Builtins stop growing: new behavior is written in the framework module only.
- Framework tests live alongside (currently zero; tracked as deferred work).

## Update (2026-10-29)

The interpreter that this ADR described as an "adapter" was removed (ADR-0008).
Dispatch is now plain JVM virtual dispatch: a translated guest class either
overrides a framework method (its translated bytecode runs) or inherits the
skeleton. The named-gap contract survives as the skeleton's
`UnsupportedOperationException`.