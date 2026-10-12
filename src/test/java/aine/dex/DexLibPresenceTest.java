package aine.dex;

/**
 * Proves the vendored dexlib2 jar is on the compile and runtime classpath.
 * References dexlib2 types directly so both `javac -cp libs/*` and
 * `java -cp ...libs/*` are exercised. See THIRD-PARTY.md.
 */
public final class DexLibPresenceTest {
    private DexLibPresenceTest() {
    }

    public static void main(String[] args) {
        check(org.jf.dexlib2.DexFileFactory.class.getName().equals("org.jf.dexlib2.DexFileFactory"),
                "DexFileFactory linked");
        check(org.jf.dexlib2.Opcodes.class.getName().equals("org.jf.dexlib2.Opcodes"),
                "Opcodes linked");
        System.out.println("DexLibPresenceTest: PASS");
    }

    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError("FAILED: " + name);
        }
    }
}
