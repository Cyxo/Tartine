#!/usr/bin/env pwsh
# Pre-commit gate (installed by `mise generate git-pre-commit --task=pre-commit`).
#
# The hook calls `mise run pre-commit`, which puts the pinned toolchain on PATH
# and passes the staged paths in $env:STAGED (one per line). Three steps:
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
$ErrorActionPreference = "Stop"

$root = $env:MISE_PROJECT_ROOT
if (-not $root) { $root = Split-Path (Split-Path $PSCommandPath) }
Set-Location $root

# Staged paths. The generated hook also exports STAGED, but mise's template
# joins it with spaces, which splits names that contain them; git is
# authoritative here (one path per line, quoted by git when needed).
$staged = git diff --cached --name-only --diff-filter=ACMR
$paths = @($staged | Where-Object { $_ })

$testChanged = @($paths | Where-Object { $_ -match '(^|/)src/test/' })
$javaFiles = @($paths | Where-Object {
        $_ -like "*.java" -and (Test-Path -LiteralPath $_ -PathType Leaf)
    })

if ($testChanged.Count -gt 0) {
    Write-Host "pre-commit: test sources staged; running the suite"
    gradle test --console=plain
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

if ($javaFiles.Count -eq 0) {
    Write-Host "pre-commit: no staged Java; static analysis skipped"
    exit 0
}

$checkstyle = Join-Path $root "tools/checkstyle-14.4.0-all.jar"
if (-not (Test-Path -LiteralPath $checkstyle -PathType Leaf)) {
    throw "missing $checkstyle. Run 'mise run fetch-tools' once to download the static-analysis tools."
}

Write-Host "pre-commit: Checkstyle on $($javaFiles.Count) staged Java file(s)"
& java -jar $checkstyle -c config/checkstyle/checkstyle.xml @javaFiles
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "pre-commit: PMD + SpotBugs (bugs of medium severity or higher, security)"
gradle pmdMain pmdTest spotbugsMain spotbugsTest --console=plain
exit $LASTEXITCODE