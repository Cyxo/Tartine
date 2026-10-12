#!/bin/sh
set -eu
rm -rf out
mkdir -p out
find src/main/java -name '*.java' > out/sources-main.txt
javac --release 14 -cp "libs/*" -d out @out/sources-main.txt
find src/test/java -name '*.java' > out/sources-test.txt
javac --release 14 -cp "out:libs/*" -d out @out/sources-test.txt
java -cp "out:libs/*" aine.dex.DexMagicTest
java -cp "out:libs/*" aine.dex.DexLibPresenceTest
java -cp "out:libs/*" aine.apk.ApkReaderTest
java -cp "out:libs/*" aine.Main
