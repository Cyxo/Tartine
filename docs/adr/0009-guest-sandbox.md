# ADR-0009: Guest sandbox — restricted classloader plus translation-time File bridge

- **Status**: Accepted
- **Date**: 2026-10-31
- **Updated**: 2026-11-01 (facades, declaring-class gate, linkage split)

## Context

Translation links guest DEX bytecode to the host JDK by ordinary JVM
linkage. That makes every host class the parent classpath carries reachable
from an untrusted APK: `java.io` streams and `java.nio.file` read and write
the real disk, `java.net` opens sockets, `Runtime`/`ProcessBuilder` spawn
processes, reflection reaches host class loaders, and the Tartine host
internals (`tartine.*`, ASM, Skija) are one `Class.forName` away. The
2026-10 audit's "virtual filesystem" finding asked for a real boundary, not
another dispatch table.

The interpreter's allowlist/deny-set cannot be reused: it decided at each
instruction boundary and consulted its own host-API table, and ADR-0008
removed the interpreter, so no dispatch seam remains. Re-deriving a
name-only deny list over plain JVM linkage would either over-deny (a
framework descriptor may mention a denied type such as `ClassLoader`) or
under-deny (one leaked `Class` object reopens the host). The replacement is
a policy table shared by two enforcement points plus narrow guest facades.

## Decision

Three decisions shape the boundary. One policy table
(`tartine.tx.SandboxPolicy`) feeds all of them.

### 1. Guest facades instead of type denial

Where a host surface is denied but libraries legitimately probe it, the
translator rewrites the call site to a narrow `tartine.guest.*` facade and
the policy lists the exact allowed members. This keeps the host type itself
unreachable (a guest name lookup still fails) while libraries see a
documented guest-safe answer:

- `GuestRuntime`: read-only `getRuntime`, `availableProcessors`,
  `totalMemory`, `freeMemory`, `maxMemory`, and a no-op `gc`.
  `exec`, `exit`, `halt`, shutdown hooks, and `load`/`loadLibrary` stay
  denied.
- `GuestSystem.getProperty`: a value allowlist (runtime identity, OS,
  separators, encodings, locale/timezone). Unknown keys return the caller's
  default because libraries catch exceptions differently; `setProperty`,
  `clearProperty`, and `getenv` stay denied.
- `GuestReflect`: reflection lookups and member access over guest-declared
  members only (details below).
- `GuestClassLoader` and `GuestSandbox`: `Class.getClassLoader`,
  `ClassLoader.loadClass`/`getResources`/`getParent`, and the one-, two-,
  and three-argument `Class.forName`. Resources are empty, the parent is
  `null`, and a host loader is refused.
- `GuestServiceLoader`: always reports no providers.

`java.io.File` remains the one guest filesystem type: `NEW java/io/File` and
every instance call are rewritten to `GuestFile`/`GuestFiles` over `Vfs`;
deeper path/handle surfaces (`toPath`, `toURI`, `toURL`, `deleteOnExit`,
`createTempFile`, `listRoots`, stream/channel/`FileDescriptor` classes) stay
denied.

### 2. The declaring-class gate for reflection

A reflection member is allowed only when its declaring class was loaded by
the guest `TranslatedLoader`. Host-declared members are outside the guest
universe:

- `getDeclared*` and `newInstance` require a guest receiver, because they
  enumerate the receiver's own members; a host receiver is a named denial.
- Plural lookups (`getMethods`, `getFields`, `getClasses`) filter
  host-declared results.
- The inheritance-aware singular lookups (`getMethod`, `getField`) answer
  Java's "not found" (`NoSuchMethodException`/`NoSuchFieldException`) so a
  guest class that inherits `Object.toString` behaves as if the host member
  does not exist.
- Member access (`Method.invoke`, `Constructor.newInstance`,
  `Field.get*`/`set*`, `setAccessible`/`trySetAccessible`) refuses a host
  member with a named `SandboxDenied`, and re-checks at the point of use
  because a `Method` object can outlive the lookup that produced it.

Metadata types (`Method`, `Field`, `Constructor`, `Parameter`, `Modifier`,
`Array`, `InvocationTargetException`) stay loadable because guest signatures
mention them.

### 3. Linkage without exposure

The loader serves two audiences with different rules:

- Plain `loadClass` is the JVM linkage path. It may resolve any host type
  through the parent, because the verifier must resolve every descriptor a
  translated class mentions — including denied types such as `ClassLoader`
  in a framework method signature. Without this, linking androidx/Compose
  classes fails with `NoClassDefFoundError`.
- Every guest-reachable lookup (`loadGuestClass`, `GuestClassLoader`,
  `GuestSandbox`) applies the allowlist first and raises a named
  `ClassNotFoundException` (`Tartine: sandbox denied: …`). Guest code cannot
  obtain the loader itself: `Class.getClassLoader` is rewritten to
  `GuestReflect`, `tartine.*` is denied to the guest, and `SandboxPass`
  rewrites `ClassLoader` type instructions to `GuestClassLoader` while
  denying `NEW`.

Guest dex classes may never define reserved names (`java.*`, `javax.*`,
`android.*`, `tartine.*`, `org.xmlpull.*`, `dalvik.*`, `libcore.*`, `jdk.*`,
`sun.*`), and class-level metadata (superclass, interfaces, field
descriptors, guest `java.io.File` subclasses) is checked before define.

### Enforcement points

1. **`tartine.tx.TranslatedLoader`** is guest-first: a name the dex set
   defines always resolves by translation; other names follow decision 3.
2. **`tartine.tx.SandboxPass`** sits between the method writer and the
   frame-computing `ClassWriter`, so rewrites are what `COMPUTE_FRAMES`
   verifies. Denied instructions (network, processes, raw streams,
   `java.nio.file`/`channels`, denied reflection operations, host internals,
   method handles, dynamic constants, denied return types) raise
   `SandboxDenied`, which the two-pass translator turns into the method's
   named `UnsupportedOperationException` gap stub; class-level denials
   surface as a named `ClassNotFoundException`.

## Consequences

- An APK's file access can only reach `Vfs` mounts; every other host
  capability fails loudly at first use with a named gap, preserving the
  project's gap-driven loop. The Compose probe (`mise run gui`) reaches
  `window ready` with no sandbox denial in the trace.
- The sandbox is a linkage boundary, not a capability system: the
  `framework` module runs as trusted host code and may use the full JDK on
  the guest's behalf. Native/ARM code is still out of scope.
- `java.lang.invoke` stays denied, including method handles. Dynamic proxies
  (`java.lang.reflect.Proxy`) are denied as a type; a guest-interface-only
  `GuestProxy` facade is the path to Retrofit-style support if it becomes a
  probe wall, and must itself require guest-loaded interfaces.
- The policy table is deliberately conservative; adding a host API means
  adding it to `SandboxPolicy` (and usually a loader/pass test) rather than
  relying on the parent classpath.
