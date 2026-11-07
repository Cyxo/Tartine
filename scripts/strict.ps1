#!/usr/bin/env pwsh
# Strict translation audit: fail when any method degrades to a translator gap.
# Sandbox denials are expected boundaries and are aggregated separately.
# Direct runs need build output; prefer `mise run strict`.
$ErrorActionPreference = "Stop"
$root = $env:MISE_PROJECT_ROOT
if (-not $root) { $root = Split-Path (Split-Path $PSCommandPath) }
$classes = Join-Path $root "build/classes/java/main"
$cpFile = Join-Path $root "build/runtime-classpath.txt"
if (-not (Test-Path $classes) -or -not (Test-Path $cpFile)) {
    throw "Build output missing. Run 'mise run build' first."
}
foreach ($entry in Get-Content $cpFile) {
    if ($entry -and -not (Test-Path -LiteralPath $entry -PathType Leaf)) {
        throw "Classpath entry missing: $entry. Run 'mise run build' first."
    }
}
$sep = [System.IO.Path]::PathSeparator
$jars = (Get-Content $cpFile) -join $sep
$target = $env:usage_apk
if (-not $target -and $args.Count -gt 0) { $target = $args[0] }
if (-not $target) { $target = "apks/app-debug.apk" }
$filter = $env:usage_filter
if (-not $filter -and $args.Count -gt 1) { $filter = $args[1] }
$jvm = @(
    "--enable-native-access=ALL-UNNAMED"
    "--add-exports", "java.base/jdk.internal.misc=ALL-UNNAMED"
    "--sun-misc-unsafe-memory-access=allow"
)
if ($filter) {
    & java @jvm -cp ($classes + $sep + $jars) tartine.Main strict $target $filter
} else {
    & java @jvm -cp ($classes + $sep + $jars) tartine.Main strict $target
}
exit $LASTEXITCODE
