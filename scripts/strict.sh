#!/usr/bin/env bash
# Strict translation audit: fail when any method degrades to a translator gap.
# Sandbox denials are expected boundaries and are aggregated separately.
# Direct run: ensure 'mise run build' has been run first.
# Prefer: mise run strict [apk] [filter]
set -euo pipefail

ROOT="${MISE_PROJECT_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"
CLASSES="$ROOT/build/classes/java/main"
CP_FILE="$ROOT/build/runtime-classpath.txt"

if [ ! -d "$CLASSES" ] || [ ! -f "$CP_FILE" ]; then
    echo "Build output missing. Run 'mise run build' first." >&2
    exit 1
fi

while IFS= read -r entry; do
    if [ -n "$entry" ] && [ ! -f "$entry" ]; then
        echo "Classpath entry missing: $entry" >&2
        echo "Run 'mise run build' first." >&2
        exit 1
    fi
done < "$CP_FILE"

case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*) SEP=";" ;;
    *) SEP=":" ;;
esac

JARS=$(tr '\n' "$SEP" < "$CP_FILE" | sed "s/${SEP}*$//")
TARGET="${usage_apk:-${1:-apks/app-debug.apk}}"
FILTER="${usage_filter:-${2:-}}"

ARGS=(strict "$TARGET")
if [ -n "$FILTER" ]; then
    ARGS+=("$FILTER")
fi

exec java \
    --enable-native-access=ALL-UNNAMED \
    --add-exports java.base/jdk.internal.misc=ALL-UNNAMED \
    --sun-misc-unsafe-memory-access=allow \
    -cp "${CLASSES}${SEP}${JARS}" tartine.Main "${ARGS[@]}"
