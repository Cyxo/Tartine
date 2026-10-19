#!/usr/bin/env bash
# GUI probe: headless onCreate, then show the activity window.
# Direct run: ensure 'mise run build' has been run first.
# Prefer: mise run gui [apk] [timeout]
set -euo pipefail

ROOT="${MISE_PROJECT_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"
CLASSES="$ROOT/build/classes/java/main"
CP_FILE="$ROOT/build/runtime-classpath.txt"

if [ ! -d "$CLASSES" ] || [ ! -f "$CP_FILE" ]; then
    echo "Build output missing. Run 'mise run build' first." >&2
    exit 1
fi

case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*) SEP=";" ;;
    *) SEP=":" ;;
esac

JARS=$(tr '\n' "$SEP" < "$CP_FILE" | sed "s/${SEP}*$//")
TARGET="${usage_apk:-${1:-apks/app-debug.apk}}"
TIMEOUT="${usage_timeout:-${2:-0}}"

# JVM flags (mise-pinned JDK, needs >= 23 for the last one): grant LWJGL native
# access (JEP 472); steer LWJGL to its jdk.internal.misc memory backend instead
# of the deprecated sun.misc.Unsafe legacy default on JDK 25+ (LWJGL
# Configuration.MEMORY_BACKEND); let our interpreter's sun.misc.Unsafe
# allocateInstance failover run without JEP 498 terminal-deprecation warnings.
# GLFW owns the process's main thread on macOS, so the window must be created
# there: -XstartOnFirstThread (Windows/Linux dispatch to main either way).
JVM_FLAGS=(--enable-native-access=ALL-UNNAMED
    --add-exports java.base/jdk.internal.misc=ALL-UNNAMED
    --sun-misc-unsafe-memory-access=allow)
case "$(uname -s)" in
    Darwin*) JVM_FLAGS+=(-XstartOnFirstThread) ;;
esac

exec java \
    "${JVM_FLAGS[@]}" \
    -cp "${CLASSES}${SEP}${JARS}" tartine.Main gui "$TARGET" "$TIMEOUT"
