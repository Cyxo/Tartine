package tartine.dex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Proves the dexlib2 dependency is on the compile and runtime classpath.
 * See THIRD-PARTY.md.
 */
class DexLibPresenceTest {
    @Test
    void dexlib2TypesLink() {
        assertEquals("com.android.tools.smali.dexlib2.DexFileFactory",
                com.android.tools.smali.dexlib2.DexFileFactory.class.getName());
        assertEquals("com.android.tools.smali.dexlib2.Opcodes",
                com.android.tools.smali.dexlib2.Opcodes.class.getName());
    }
}
