# ADR-0001: Virtual machine strategy (interpreter-first)

- **Status**: Accepted (Step 3; translation spike evaluated 2026-10-19 and adopted)
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
translation loader is now the default under `run`/`gui`, with the
interpreter retained as the reference/debug tool (`irun`/`igui`).

## Consequences

- `android.jar` is used for signatures/constants only, never behavior.
- Unknown opcodes and unimplemented methods surface as named gaps rather
  than silent no-ops.
- Translation remains built on the same resolution layer (pre-resolved IR); the
  translation spike delivered `tartine.tx`; verification (StackMapTable-free,
  v49 classfiles) confirmed feasibility on current HotSpot.