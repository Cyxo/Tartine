# ADR-0001: Virtual machine strategy (interpreter-first)

- **Status**: Superseded by ADR-0008 (interpreter removed 2026-10-29); decision below kept for history
- **Date**: decided Step 3, revised 2026-10-19

## Context

Android DEX is register-based and differs enough from JVM bytecode that it
cannot be loaded directly. We either interpret DEX (or our IR) or translate it
to JVM bytecode. The `android.*` framework must be reimplemented either way.
A translation spike (ASM 9.6) ran; the first attempt failed in the JVM
verifier because of type inference / frames / exceptions.

## Decision

Interpreter-first: `tartine.vm.Interpreter` executes our IR; unimplemented
opcodes/methods throw `VmException` naming the gap. Translation was
re-evaluated after the first Activity renders/first frame blits; the
translation loader became the default under `run`/`gui`, and the interpreter
was later removed entirely (ADR-0008) — translation is now the only VM.

## Consequences

- `android.jar` is used for signatures/constants only, never behavior.
- Unknown opcodes and unimplemented methods surface as named gaps rather
  than silent no-ops.
- Translation remains built on the same resolution layer (pre-resolved IR); the
  translation spike delivered `tartine.tx`; verification (StackMapTable-free,
  v49 classfiles) confirmed feasibility on current HotSpot.
- The interpreter-only parts of this record (its loop, `VmException` gaps) are
  historical: `tartine.vm` no longer exists (ADR-0008). The same gap contract
  now surfaces as the framework skeleton's
  `UnsupportedOperationException("Tartine: not implemented: <signature>")`.