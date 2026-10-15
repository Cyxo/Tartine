#!/usr/bin/env pwsh
# Download dev-time oracle JARs into tools/.
# Safe to re-run: existing files are skipped.
# Run via: mise run fetch-tools
$ErrorActionPreference = "Stop"

$root = $env:MISE_PROJECT_ROOT
if (-not $root) { $root = Split-Path (Split-Path $PSCommandPath) }
$toolsDir = Join-Path $root "tools"
New-Item -ItemType Directory -Force -Path $toolsDir | Out-Null

$maven = "https://repo1.maven.org/maven2"

$tools = @(
    @{
        File    = "baksmali-2.5.2.jar"
        Url     = "$maven/org/smali/baksmali/2.5.2/baksmali-2.5.2.jar"
        Sha1    = "1e95fe9f9bde43c2b8b7e5b7e2d35e48c8b2d3e4"   # placeholder — verified by size below
        Bytes   = 123309
    },
    @{
        File    = "util-2.5.2.jar"
        Url     = "$maven/org/smali/util/2.5.2/util-2.5.2.jar"
        Bytes   = 27118
    },
    @{
        File    = "jcommander-1.64.jar"
        Url     = "$maven/com/beust/jcommander/1.64/jcommander-1.64.jar"
        Bytes   = 65585
    },
    @{
        File    = "apktool_3.0.3.jar"
        Url     = "https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_3.0.3.jar"
        Bytes   = 15478013
    }
)

foreach ($t in $tools) {
    $dest = Join-Path $toolsDir $t.File
    if (Test-Path $dest) {
        Write-Host "  skip  $($t.File) (already present)"
        continue
    }
    Write-Host "  fetch $($t.File) ..."
    Invoke-WebRequest -Uri $t.Url -OutFile $dest -UseBasicParsing
    $actual = (Get-Item $dest).Length
    if ($t.ContainsKey("Bytes") -and $actual -ne $t.Bytes) {
        Remove-Item $dest
        throw "Size mismatch for $($t.File): expected $($t.Bytes) bytes, got $actual. Aborting."
    }
    Write-Host "  ok    $($t.File) ($actual bytes)"
}

Write-Host ""
Write-Host "tools/ is ready."
