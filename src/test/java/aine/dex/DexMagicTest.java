package aine.dex;

/**
 * Hand-rolled test for {@link DexMagic}. Plain {@code main} on purpose:
 * no test dependencies yet (Java-only stack).
 */
public final class DexMagicTest {
    private DexMagicTest() {
    }

    public static void main(String[] args) {
        check(DexMagic.hasDexMagic(new byte[]{0x64, 0x65, 0x78, 0x0A, '0', '3', '5', 0}),
                "035 magic accepted");
        check(DexMagic.hasDexMagic(new byte[]{0x64, 0x65, 0x78, 0x0A, '0', '3', '9', 0}),
                "039 magic accepted");
        check(!DexMagic.hasDexMagic(new byte[]{0x50, 0x4B, 0x03, 0x04, 0, 0, 0, 0}),
                "ZIP header rejected");
        check(!DexMagic.hasDexMagic(new byte[]{0x64, 0x65, 0x78}),
                "truncated input rejected");
        check(!DexMagic.hasDexMagic(null), "null rejected");
        System.out.println("DexMagicTest: PASS");
    }

    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError("FAILED: " + name);
        }
    }
}
