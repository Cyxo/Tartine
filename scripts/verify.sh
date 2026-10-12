#!/bin/sh
set -eu
rm -rf out
mkdir -p out
find src/main/java -name '*.java' > out/sources-main.txt
javac --release 14 -d out @out/sources-main.txt
find src/test/java -name '*.java' > out/sources-test.txt
javac --release 14 -cp out -d out @out/sources-test.txt
java -cp out aine.dex.DexMagicTest
java -cp out aine.Main
