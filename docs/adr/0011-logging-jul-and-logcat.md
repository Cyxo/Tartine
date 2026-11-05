# ADR-0011: Logging through java.util.logging and android.util.Log

- **Status**: Accepted
- **Date**: 2026-11-05

## Context

Runtime diagnostics started as ad-hoc `System.out` prints. A later pass moved
them to SLF4J with Logback as the runtime binding, including the framework
module. That conflicts with two standing constraints:

- the framework is real Java code with no renderer/platform implementation
  leaked into it (ADR-0002), so it should not need an external logging API;
- only permissive licenses belong on the runtime path. Logback is dual
  licensed EPL-1.0/LGPL-2.1, not permissive. The SLF4J API itself is MIT, but
  keeping it would still add an external dependency and imply a binding.

Android's own `android.util.Log` writes through native liblog to the device
logcat. The desktop runtime has no OS logcat and deliberately avoids such a
native dependency.

There was also a functional split problem: command results (`apk`, `dump`,
`refs`) went through the same INFO logger as diagnostics. Selecting WARN or
ERROR correctly silenced diagnostics, but also silently hid the results.

## Decision

**JUL for diagnostics.** All runtime, framework, renderer, and SDK-tool
diagnostics use `java.util.logging` from `java.base`. No external logging
library remains on the runtime path. `HostConfig` maps
`log.level`/`--log-level` as TRACE→FINEST, DEBUG→FINE, INFO→INFO,
WARN→WARNING, ERROR→SEVERE, OFF→OFF; the command line wins over the config
file. A static initializer installs logging before config warnings are
emitted, and `configureLogging` installs a `ConsoleHandler` on stderr with
`LogFormat` (`P/logger: message`, throwable stack traces appended).
`ConsoleHandler` flushes after every record, keeping diagnostics ordered with
direct output. JDK-internal namespaces (`java`, `javax`, `jdk`, `sun`,
`com.sun`) stay at INFO so DEBUG/TRACE only amplifies Tartine and guest logs.

**Results and diagnostics are separate streams.** Command results are written
to stdout through a plain print helper in `Main`: `apk`, `dump`, `refs`,
`txdump`, and the usage line. Diagnostics stay on the JUL logger and therefore
stderr; window-icon resolution status is logged at FINE, and the render
loop's frame summary/draw failure text goes to stderr too. Piping stdout now
captures results regardless of `log.level`, and redirecting stderr captures
diagnostics without mixing them into the result text.

**`android.util.Log` maps onto JUL.** The framework provides the full API-36
surface (`v/d/i/w/e/wtf`, `println`, `isLoggable`, `getStackTraceString`).
Priorities map VERBOSE→FINEST, DEBUG→FINE, INFO→INFO, WARN→WARNING, and
ERROR/ASSERT→SEVERE. `isLoggable` checks the effective JUL level, so guest
calls obey the same `log.level` configuration as host diagnostics.
`StackWalker` resolves the calling class, which becomes the JUL logger name
and logcat-style prefix; the supplied tag is only a fallback when no caller
frame is found. There is no native liblog path.

## Consequences

- The framework stays pure Java with no logging dependency, and the runtime
  path no longer carries Logback's copyleft license.
- `refs apks/app-debug.apk > build/refs.txt` and similar pipelines capture
  only results; WARN/ERROR no longer hides them. Diagnostics remain available
  on stderr.
- Guest `Log.d`/`Log.i`/... output uses the host level and the calling class as
  the prefix rather than Android's tag-based native logcat format. Apps that
  pass an explicit tag get the class prefix instead; the tag fallback only
  applies when caller lookup fails.
- Caller-class resolution costs one `StackWalker` walk per guest log call.
  This is acceptable for diagnostics; a cache can be added if hot paths ever
  need it.
- Per-tag logcat properties (`log.tag.*`) and native logcat integration stay
  out of scope; the host configuration is the single level control.
