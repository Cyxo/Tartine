$ErrorActionPreference = "Stop"
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out | Out-Null
$mainSources = Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName
javac --release 14 -d out $mainSources
$testSources = Get-ChildItem -Recurse -Filter *.java -Path src/test/java | ForEach-Object FullName
javac --release 14 -cp out -d out $testSources
java -cp out aine.dex.DexMagicTest
java -cp out aine.Main
