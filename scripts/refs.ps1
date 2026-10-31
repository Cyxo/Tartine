#!/usr/bin/env pwsh
# Reference census: unresolved method/field/type references before translation.
# Direct runs need build output + tools on PATH; prefer `mise run refs`.
$ErrorActionPreference = "Stop"
$root = $env:MISE_PROJECT_ROOT
if (-not $root) { $root = Split-Path (Split-Path $PSCommandPath) }
$classes = Join-Path $root "build/classes/java/main"
$cpFile = Join-Path $root "build/runtime-classpath.txt"
if (-not (Test-Path $classes) -or -not (Test-Path $cpFile)) {
    throw "Build output missing. Run 'mise run build' first."
}
$sep = [System.IO.Path]::PathSeparator
$jars = (Get-Content $cpFile) -join $sep
$target = $env:usage_apk
if (-not $target -and $args.Count -gt 0) { $target = $args[0] }
if (-not $target) { $target = "apks/app-debug.apk" }
$jvm = @(
    "--enable-native-access=ALL-UNNAMED"
    "--add-exports", "java.base/jdk.internal.misc=ALL-UNNAMED"
    "--sun-misc-unsafe-memory-access=allow"
)
& java @jvm -cp ($classes + $sep + $jars) tartine.Main refs $target
