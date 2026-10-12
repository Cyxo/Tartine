$ErrorActionPreference = "Stop"
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out | Out-Null
$mainSources = Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName
javac --release 14 -cp "libs/*" -d out $mainSources
$testSources = Get-ChildItem -Recurse -Filter *.java -Path src/test/java | ForEach-Object FullName
javac --release 14 -cp "out;libs/*" -d out $testSources
java -cp "out;libs/*" aine.dex.DexMagicTest
java -cp "out;libs/*" aine.dex.DexLibPresenceTest
java -cp "out;libs/*" aine.Main
