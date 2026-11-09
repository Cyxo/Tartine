#!/bin/sh
# Download dev-time oracle JARs into tools/.
# Safe to re-run: existing files are skipped.
# Run via: mise run fetch-tools
set -eu

ROOT="${MISE_PROJECT_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"
TOOLS="$ROOT/tools"
mkdir -p "$TOOLS"

MAVEN="https://repo1.maven.org/maven2"

sha256_of() {
    if command -v sha256sum >/dev/null 2>&1; then
        sha256sum "$1" | cut -d' ' -f1
    elif command -v shasum >/dev/null 2>&1; then
        shasum -a 256 "$1" | cut -d' ' -f1
    else
        echo ""
    fi
}

fetch() {
    file="$1"; url="$2"; expected_bytes="$3"; expected_sha="$4"
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
    actual_sha=$(sha256_of "$dest")
    if [ -z "$actual_sha" ]; then
        echo "Warning: no sha256sum/shasum; skipped checksum for $file." >&2
    elif [ "$actual_sha" != "$expected_sha" ]; then
        rm -f "$dest"
        echo "SHA-256 mismatch for $file: got $actual_sha. Aborting." >&2
        exit 1
    fi
    printf "  ok    %s (%s bytes)\n" "$file" "$actual"
}

fetch "baksmali-2.5.2.jar"  "$MAVEN/org/smali/baksmali/2.5.2/baksmali-2.5.2.jar"  123309 \
      1ed236266d7dc4907aade0b19a34f77efac25342b63c8ace52e579039941b389
fetch "util-2.5.2.jar"      "$MAVEN/org/smali/util/2.5.2/util-2.5.2.jar"           27118 \
      4f580a9cff3ebb83cb3fd20bec88e37e4f183796ca652732992611620282daea
fetch "jcommander-1.64.jar" "$MAVEN/com/beust/jcommander/1.64/jcommander-1.64.jar" 65585 \
      156be736199c990321d9ff77090b199629cfc9865e2d6c13f7cd291bb1641817
fetch "apktool_3.0.3.jar"   "https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_3.0.3.jar" 15478013 \
      dbf930b076c6b9be08d57c449cacefc3bdd6b71ebd59b3066fc0e1f5b14f9423
# All-in-one jar: Checkstyle publishes it as a release asset, not on Central.
fetch "checkstyle-14.4.0-all.jar" \
      "https://github.com/checkstyle/checkstyle/releases/download/checkstyle-14.4.0/checkstyle-14.4.0-all.jar" \
      15225265 \
      c0a521e54eabd1ece4ff7bd84cf231b012615745ed3aa4fb27ab0ca73097250c

# dex2jar ships as a zip distribution (the differential oracle); extract it
# once. The inner jar is the presence marker.
DEX2JAR_JAR="$TOOLS/dex-tools-v2.4/lib/dex-tools-v2.4.jar"
if [ -f "$DEX2JAR_JAR" ]; then
    printf "  skip  %s (already extracted)\n" "dex-tools-v2.4"
else
    DEX2JAR_ZIP="$TOOLS/dex-tools-v2.4.zip"
    rm -rf "$TOOLS/dex-tools-v2.4"
    printf "  fetch %s ...\n" "dex-tools-v2.4.zip"
    if command -v curl >/dev/null 2>&1; then
        curl -fsSL -o "$DEX2JAR_ZIP" \
            "https://github.com/pxb1988/dex2jar/releases/download/v2.4/dex-tools-v2.4.zip"
    elif command -v wget >/dev/null 2>&1; then
        wget -q -O "$DEX2JAR_ZIP" \
            "https://github.com/pxb1988/dex2jar/releases/download/v2.4/dex-tools-v2.4.zip"
    else
        echo "Error: neither curl nor wget found." >&2; exit 1
    fi
    actual=$(wc -c < "$DEX2JAR_ZIP" | tr -d ' ')
    if [ "$actual" != "19105975" ]; then
        rm -f "$DEX2JAR_ZIP"
        echo "Size mismatch for dex-tools-v2.4.zip: expected 19105975 bytes, got $actual. Aborting." >&2
        exit 1
    fi
    expected="ee7c45eb3c1d2474a6145d8d447e651a736a22d9664b6d3d3be5a5a817dda23a"
    if command -v sha256sum >/dev/null 2>&1; then
        actual_sha=$(sha256sum "$DEX2JAR_ZIP" | cut -d' ' -f1)
    elif command -v shasum >/dev/null 2>&1; then
        actual_sha=$(shasum -a 256 "$DEX2JAR_ZIP" | cut -d' ' -f1)
    else
        actual_sha="$expected"
        echo "Warning: no sha256sum/shasum; skipped dex2jar checksum." >&2
    fi
    if [ "$actual_sha" != "$expected" ]; then
        rm -f "$DEX2JAR_ZIP"
        echo "SHA-256 mismatch for dex-tools-v2.4.zip: got $actual_sha. Aborting." >&2
        exit 1
    fi
    if command -v unzip >/dev/null 2>&1; then
        unzip -q "$DEX2JAR_ZIP" -d "$TOOLS"
    else
        (cd "$TOOLS" && jar xf "$DEX2JAR_ZIP")
    fi
    rm -f "$DEX2JAR_ZIP"
    printf "  ok    %s (extracted)\n" "dex-tools-v2.4"
fi

echo ""
echo "tools/ is ready."
