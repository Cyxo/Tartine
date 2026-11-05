#!/usr/bin/env bash
# Print the instruction dump of one dex file.
# Direct run: ensure 'mise run build' has been run first.
# Prefer: mise run dump <dex>
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
TARGET="${usage_dex:-${1:-}}"

if [ -z "$TARGET" ]; then
    echo "Usage: mise run dump <dex-file>" >&2
    exit 1
fi

exec java -cp "${CLASSES}${SEP}${JARS}" tartine.Main dump "$TARGET"
