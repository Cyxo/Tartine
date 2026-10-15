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

exec java -cp "${CLASSES}${SEP}${JARS}" tartine.Main gui "$TARGET" "$TIMEOUT"
