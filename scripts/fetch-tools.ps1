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
        Bytes   = 123309
        Sha256  = "1ED236266D7DC4907AADE0B19A34F77EFAC25342B63C8ACE52E579039941B389"
    },
    @{
        File    = "util-2.5.2.jar"
        Url     = "$maven/org/smali/util/2.5.2/util-2.5.2.jar"
        Bytes   = 27118
        Sha256  = "4F580A9CFF3EBB83CB3FD20BEC88E37E4F183796CA652732992611620282DAEA"
    },
    @{
        File    = "jcommander-1.64.jar"
        Url     = "$maven/com/beust/jcommander/1.64/jcommander-1.64.jar"
        Bytes   = 65585
        Sha256  = "156BE736199C990321D9FF77090B199629CFC9865E2D6C13F7CD291BB1641817"
    },
    @{
        File    = "apktool_3.0.3.jar"
        Url     = "https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_3.0.3.jar"
        Bytes   = 15478013
        Sha256  = "DBF930B076C6B9BE08D57C449CACEFC3BDD6B71EBD59B3066FC0E1F5B14F9423"
    },
    @{
        # All-in-one jar: Checkstyle publishes it as a release asset, not on
        # Maven Central (Central has the plain jar plus separate dependencies).
        File    = "checkstyle-14.4.0-all.jar"
        Url     = "https://github.com/checkstyle/checkstyle/releases/download/checkstyle-14.4.0/checkstyle-14.4.0-all.jar"
        Bytes   = 15225265
        Sha256  = "C0A521E54EABD1ECE4FF7BD84CF231B012615745ED3AA4FB27AB0CA73097250C"
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
    $sha = (Get-FileHash -Algorithm SHA256 $dest).Hash
    if ($sha -ne $t.Sha256) {
        Remove-Item $dest
        throw "SHA-256 mismatch for $($t.File): got $sha. Aborting."
    }
    Write-Host "  ok    $($t.File) ($actual bytes)"
}

# dex2jar ships as a zip distribution (the differential oracle); extract it
# once. The jar inside the distribution is the presence marker.
$dex2jarJar = Join-Path $toolsDir "dex-tools-v2.4/lib/dex-tools-v2.4.jar"
if (Test-Path $dex2jarJar) {
    Write-Host "  skip  dex-tools-v2.4 (already extracted)"
} else {
    $zip = Join-Path $toolsDir "dex-tools-v2.4.zip"
    $dex2jarZip = Join-Path $toolsDir "dex-tools-v2.4"
    if (Test-Path $dex2jarZip) { Remove-Item -Recurse -Force $dex2jarZip }
    Write-Host "  fetch dex-tools-v2.4.zip ..."
    Invoke-WebRequest -UseBasicParsing -OutFile $zip `
        -Uri "https://github.com/pxb1988/dex2jar/releases/download/v2.4/dex-tools-v2.4.zip"
    $actual = (Get-Item $zip).Length
    if ($actual -ne 19105975) {
        Remove-Item $zip
        throw "Size mismatch for dex-tools-v2.4.zip: expected 19105975 bytes, got $actual. Aborting."
    }
    $sha = (Get-FileHash -Algorithm SHA256 $zip).Hash
    if ($sha -ne "EE7C45EB3C1D2474A6145D8D447E651A736A22D9664B6D3D3BE5A5A817DDA23A") {
        Remove-Item $zip
        throw "SHA-256 mismatch for dex-tools-v2.4.zip: got $sha. Aborting."
    }
    Expand-Archive -Path $zip -DestinationPath $toolsDir -Force
    Remove-Item $zip
    Write-Host "  ok    dex-tools-v2.4 (extracted)"
}

Write-Host ""
Write-Host "tools/ is ready."
