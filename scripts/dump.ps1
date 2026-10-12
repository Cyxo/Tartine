#!/usr/bin/env pwsh
# Direct runs need build output + tools on PATH; prefer `mise run dump`.
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
$target = $env:usage_dex
if (-not $target -and $args.Count -gt 0) { $target = $args[0] }
if (-not $target) { throw "Usage: mise run dump <dex-file>" }
& java -cp ($classes + $sep + $jars) tartine.Main dump $target
