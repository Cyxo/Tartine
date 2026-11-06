#!/usr/bin/env bash
# Pre-commit gate (installed by `mise generate git-pre-commit --task=pre-commit`).
#
# The hook calls `mise run pre-commit`, which puts the pinned toolchain on PATH
# and passes the staged paths in $STAGED (one per line). Three steps:
#
#   1. Tests, only when a test source is staged: the commit is refused unless
#      the whole suite passes.
#   2. Checkstyle (repo-tuned Google config) on the staged Java files. The
#      config skips generated framework skeletons.
#   3. PMD + SpotBugs (the SonarLint stand-in): bugs of medium severity or
#      higher and security findings, across every module.
#
# Tools: Checkstyle ships in tools/ (mise run fetch-tools). PMD and SpotBugs
# are Gradle plugins, so Gradle resolves them.
set -euo pipefail

ROOT="${MISE_PROJECT_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"
cd "$ROOT"

# Staged paths, NUL-separated in git so names with spaces survive. (The
# generated hook also exports STAGED, but mise's template joins it with
# spaces, which splits those names; git is authoritative here.)
staged=$(git diff --cached --name-only --diff-filter=ACMR -z | tr '\0' '\n')

java_files=()
test_changed=0
while IFS= read -r path; do
    [ -n "$path" ] || continue
    case "$path" in
        */src/test/* | src/test/*) test_changed=1 ;;
    esac
    if [ ! -f "$path" ]; then
        continue
    fi
    case "$path" in
        *.java) java_files+=("$path") ;;
    esac
done <<< "$staged"

if [ "$test_changed" -eq 1 ]; then
    echo "pre-commit: test sources staged; running the suite"
    gradle test --console=plain
fi

if [ "${#java_files[@]}" -eq 0 ]; then
    echo "pre-commit: no staged Java; static analysis skipped"
    exit 0
fi

echo "pre-commit: Checkstyle on ${#java_files[@]} staged Java file(s)"
CHECKSTYLE="$ROOT/tools/checkstyle-14.4.0-all.jar"
if [ ! -f "$CHECKSTYLE" ]; then
    echo "pre-commit: missing $CHECKSTYLE" >&2
    echo "pre-commit: run 'mise run fetch-tools' once to download the static-analysis tools" >&2
    exit 1
fi
java -jar "$CHECKSTYLE" -c config/checkstyle/checkstyle.xml "${java_files[@]}"

echo "pre-commit: PMD + SpotBugs (bugs of medium severity or higher, security)"
gradle pmdMain pmdTest spotbugsMain spotbugsTest --console=plain