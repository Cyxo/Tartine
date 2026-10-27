#!/usr/bin/env bash
# Translated headless probe: run launcher onCreate as translated bytecode
# until the first gap. Direct run: ensure 'mise run build' has been run first.
# Prefer: mise run txrun [apk]
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

# JVM flags (mise-pinned JDK, needs >= 23 for the last one): grant LWJGL native
# access (JEP 472); steer LWJGL to its jdk.internal.misc memory backend instead
# of the deprecated sun.misc.Unsafe legacy default on JDK 25+ (LWJGL
# Configuration.MEMORY_BACKEND); let our interpreter's sun.misc.Unsafe
# allocateInstance failover run without JEP 498 terminal-deprecation warnings.
exec java \
    --enable-native-access=ALL-UNNAMED \
    --add-exports java.base/jdk.internal.misc=ALL-UNNAMED \
    --sun-misc-unsafe-memory-access=allow \
    -cp "${CLASSES}${SEP}${JARS}" tartine.Main txrun "$TARGET"
