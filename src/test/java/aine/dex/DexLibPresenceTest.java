package aine.dex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Proves the dexlib2 dependency is on the compile and runtime classpath.
 * See THIRD-PARTY.md.
 */
class DexLibPresenceTest {
    @Test
    void dexlib2TypesLink() {
        assertEquals("org.jf.dexlib2.DexFileFactory",
                org.jf.dexlib2.DexFileFactory.class.getName());
        assertEquals("org.jf.dexlib2.Opcodes",
                org.jf.dexlib2.Opcodes.class.getName());
    }
}
