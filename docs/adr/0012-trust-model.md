# ADR-0012: Wine trust model — run apps you trust, not a security boundary

- **Status**: Accepted
- **Date**: 2026-11-09

## Context

ADR-0009 introduced a restricted classloader, translation-time rewrites, and a
guest file bridge. Its context described the guest as "an untrusted APK" and
the 2026-10 audit framed the work as a security boundary. The architecture
cannot support that claim: translated code, the ported framework, and the
Tartine host share one JVM, one process, and one OS user. There is no address
space, process, or UID boundary between an app and the host, and a policy
table over JVM linkage cannot contain code that already executes in the same
runtime. The mismatch generated findings that assumed isolation the runtime
does not have.

Stating the trust model is not a retreat from the sandbox work. The guest
isolation layer still serves compatibility: it keeps app code on the virtual
filesystem, makes host-capability use an explicit, named decision, and stops
ported framework code from accidentally reaching host APIs that would not
exist on Android. What changes is the claim made about it.

## Decision

Tartine uses Wine's trust model: **it runs Android apps you trust.** It is a
compatibility layer, not a security sandbox, and does not isolate apps from
the system.

- A malicious APK can do anything the user's account can do — read and write
  user files, open the network, spawn processes, and so on. Users should run
  only apps they would run as a normal desktop program.
- The guest sandbox (ADR-0009) is a linkage and accident boundary, not a
  security boundary. It prevents translated code from silently reaching host
  internals, routes file access through the virtual filesystem, and turns
  unsupported host capabilities into named gaps. It is not designed to, and
  does not, contain hostile code.
- The Android-style permission layer is **consent, not enforcement**. Permission
  prompts exist for fidelity and user expectations; they tell the user what an
  app is asking for, but they do not stop code that bypasses the framework
  APIs. A hostile app is not constrained by answering "deny".
- The JVM verifier remains the memory-safety net for translation defects
  (malformed frames, type confusion introduced by the translator), not a
  containment mechanism for app code.

## Consequences

- The README states this trust model up front, and ADR-0009's "untrusted APK"
  framing is superseded by this ADR; its enforcement mechanism stands.
- Denials and gaps remain valuable: they surface missing compatibility work
  and make host access visible. They must not be documented or marketed as
  security controls.
- Running an app from an untrusted source is equivalent to running an
  untrusted desktop program with the same privileges. Tartine adds no
  protection against that.
- Real isolation, if it is ever wanted, needs an OS-level design (a separate
  process, container, or user account), not more JVM-level policy. That is
  out of scope for this ADR.
