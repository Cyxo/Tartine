#!/usr/bin/env bash
# APK summary: manifest, code, libraries (headless).
# Direct run: ensure 'mise run build' has been run first.
# Prefer: mise run apk [apk]
set -euo pipefail

ROOT="${MISE_PROJECT_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"
CLASSES="$ROOT/build/classes/java/main"
CP_FILE="$ROOT/build/runtime-classpath.txt"

if [ ! -d "$CLASSES" ] || [ ! -f "$CP_FILE" ]; then
    echo "Build output missing. Run 'mise run build' first." >&2
    exit 1
fi

# Java on Windows (native exe) requires ';' as the classpath separator.
case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*) SEP=";" ;;
    *) SEP=":" ;;
esac

JARS=$(tr '\n' "$SEP" < "$CP_FILE" | sed "s/${SEP}*$//")
TARGET="${usage_apk:-${1:-apks/app-debug.apk}}"

exec java -cp "${CLASSES}${SEP}${JARS}" tartine.Main apk "$TARGET"
