package tartine.dex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.Opcodes;
import org.junit.jupiter.api.Test;

/**
 * Proves the dexlib2 dependency is on the compile and runtime classpath.
 * See THIRD-PARTY.md.
 */
class DexLibPresenceTest {
    @Test
    void dexlib2TypesLink() {
        assertEquals("com.android.tools.smali.dexlib2.DexFileFactory",
                DexFileFactory.class.getName());
        assertEquals("com.android.tools.smali.dexlib2.Opcodes",
                Opcodes.class.getName());
    }
}
