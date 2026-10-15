#!/bin/sh
# Download dev-time oracle JARs into tools/.
# Safe to re-run: existing files are skipped.
# Run via: mise run fetch-tools
set -eu

ROOT="${MISE_PROJECT_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"
TOOLS="$ROOT/tools"
mkdir -p "$TOOLS"

MAVEN="https://repo1.maven.org/maven2"

fetch() {
    file="$1"; url="$2"; expected_bytes="$3"
    dest="$TOOLS/$file"
    if [ -f "$dest" ]; then
        printf "  skip  %s (already present)\n" "$file"
        return
    fi
    printf "  fetch %s ...\n" "$file"
    if command -v curl >/dev/null 2>&1; then
        curl -fsSL -o "$dest" "$url"
    elif command -v wget >/dev/null 2>&1; then
        wget -q -O "$dest" "$url"
    else
        echo "Error: neither curl nor wget found." >&2; exit 1
    fi
    actual=$(wc -c < "$dest" | tr -d ' ')
    if [ "$actual" != "$expected_bytes" ]; then
        rm -f "$dest"
        echo "Size mismatch for $file: expected $expected_bytes bytes, got $actual. Aborting." >&2
        exit 1
    fi
    printf "  ok    %s (%s bytes)\n" "$file" "$actual"
}

fetch "baksmali-2.5.2.jar"  "$MAVEN/org/smali/baksmali/2.5.2/baksmali-2.5.2.jar"  123309
fetch "util-2.5.2.jar"      "$MAVEN/org/smali/util/2.5.2/util-2.5.2.jar"           27118
fetch "jcommander-1.64.jar" "$MAVEN/com/beust/jcommander/1.64/jcommander-1.64.jar" 65585
fetch "apktool_3.0.3.jar"   "https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_3.0.3.jar" 15478013

echo ""
echo "tools/ is ready."
