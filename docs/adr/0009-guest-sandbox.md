# ADR-0009: Guest sandbox — restricted classloader plus translation-time File bridge

- **Status**: Accepted
- **Date**: 2026-10-31

## Context

Translation links guest DEX bytecode to the host JDK by ordinary JVM
linkage. That makes every host class the parent classpath carries reachable
from an untrusted APK: `java.io` streams and `java.nio.file` read and write
the real disk, `java.net` opens sockets, `Runtime`/`ProcessBuilder` spawn
processes, reflection reaches host class loaders, and the Tartine host
internals (`tartine.*`, ASM, Skija) are one `Class.forName` away. The
2026-10 audit's "virtual filesystem" finding asked for a real boundary, not
another dispatch table; the interpreter's allowlist/deny-set died with the
interpreter (ADR-0008), and `Vfs` existed without a guest bridge.

## Decision

Two enforcement points share one policy table (`tartine.tx.SandboxPolicy`):

1. **`tartine.tx.TranslatedLoader`** is guest-first: a name the dex set
   defines always resolves by translation; every guest-reachable lookup
   (`loadGuestClass`, `GuestClassLoader`, `GuestSandbox`) goes through an
   allowlist (`java.*` subsets, `javax.*` crypto/cert/MIDP, framework
   `android.*`, `org.xmlpull.*`, `tartine.guest.*`) and a denied name raises
   a named `ClassNotFoundException` (`Tartine: sandbox denied: …`). Plain
   `loadClass` is the JVM linkage path: it may resolve a denied host type
   that appears only in a translated signature (for example `ClassLoader` in
   a framework method), because the verifier needs it to link, but guest
   code cannot obtain the loader or the type. Guest dex classes may never
   define reserved names (`java.*`, `javax.*`, `android.*`, `tartine.*`,
   `org.xmlpull.*`, `dalvik.*`, `libcore.*`, `jdk.*`, `sun.*`), and
   class-level metadata (superclass, interfaces, field descriptors, guest
   `java.io.File` subclasses) is checked before define.

2. **`tartine.tx.SandboxPass`** sits between the method writer and the
   frame-computing `ClassWriter`, so rewrites are what `COMPUTE_FRAMES`
   verifies. It rewrites `NEW java/io/File` to `tartine.guest.GuestFile` and
   every instance call on `java.io.File` to `INVOKESTATIC
   tartine.guest.GuestFiles.<name>(File, …)`; `Vfs` maps or denies each
   guest path. Denied instructions (network, processes, raw streams,
   `java.nio.file`/`channels`, reflection operations, host internals, method
   handles, dynamic constants, denied return types) raise `SandboxDenied`,
   which the existing two-pass translator turns into the method's named
   `UnsupportedOperationException` gap stub. Class-level denials surface as
   a named `ClassNotFoundException`.

Supporting decisions:

- `java.io.File` stays the one guest filesystem type. Deeper path/handle
  surfaces (`toPath`, `toURI`, `toURL`, `deleteOnExit`, `createTempFile`,
  `listRoots`, stream/channel/`FileDescriptor` classes) are denied; reads on
  unmapped paths answer nonexistent, writes raise `SandboxDenied`, and
  read-only mounts refuse every write-shaped call.
- `GuestFile` overrides the inherited path and filesystem surface so a
  missed rewrite cannot expose the host working directory; `GuestFiles`
  reverse-maps framework-returned `File` objects through
  `Vfs.Policy.guestPath(Path)` and never shows a host path.
- Guest facades widen the boundary without exposing the host:
  `GuestRuntime` (read-only memory/CPU queries plus a no-op `gc`),
  `GuestSystem.getProperty` (a value allowlist; unknown keys return the
  caller's default), `GuestReflect` (reflection over guest-declared members
  only), `GuestClassLoader`/`GuestSandbox` (guest class lookups and
  `Class.forName`), and `GuestServiceLoader` (always "no providers").
  `SandboxPolicy`'s javadoc and `docs/architecture.md` list each allowlist.
- Reflection metadata types (`Method`, `Field`, `Constructor`, `Parameter`,
  `Modifier`, `Array`, `InvocationTargetException`) stay loadable because
  guest signatures mention them. `GuestReflect` allows `getDeclared*`,
  `getMethod(s)`, `getField(s)`, `getConstructor(s)`, `getClasses`,
  `newInstance`, `Method.invoke`, `Constructor.newInstance`,
  `Field.get*`/`set*`, and `setAccessible`/`trySetAccessible` only when the
  member's declaring class is guest-loaded; host-declared members are denied
  at lookup and again at invoke/access. The one-, two-, and three-argument
  `Class.forName` forms are rewritten to `GuestSandbox`, which gates the
  name through the guest loader and refuses a host loader.
- `System.out`/`err` and `System.in` follow the same split: the fields stay
  typed, `System.in` and output redirection are denied; `System.exit`,
  `getenv`, `setProperty`, `clearProperty`, `load`/`loadLibrary` are denied,
  and `getProperty` is the allowlisted `GuestSystem` facade. `Runtime.exec`,
  `exit`, `halt`, shutdown hooks, and `load`/`loadLibrary` are denied.
- The translator's own injected helper (`GuestArrays.coerce`) moved under
  `tartine.guest.*` and resolves component classes through the guest loader.

## Consequences

- An APK's file access can only reach `Vfs` mounts; every other host
  capability fails loudly at first use with a named gap, preserving the
  project's gap-driven loop. The probe (`mise run run`) still reaches
  `onCreate returned`.
- The sandbox is a linkage boundary, not a capability system: the
  `framework` module runs as trusted host code and may use the full JDK on
  the guest's behalf. Native/ARM code is still out of scope.
- The policy table is deliberately conservative; adding a host API means
  adding it to `SandboxPolicy` (and usually a loader/pass test) rather than
  relying on the parent classpath.
