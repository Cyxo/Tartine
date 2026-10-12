package aine.dex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DexMagicTest {
    @Test
    void acceptsDex035Magic() {
        assertTrue(DexMagic.hasDexMagic(new byte[]{0x64, 0x65, 0x78, 0x0A, '0', '3', '5', 0}));
    }

    @Test
    void acceptsDex039Magic() {
        assertTrue(DexMagic.hasDexMagic(new byte[]{0x64, 0x65, 0x78, 0x0A, '0', '3', '9', 0}));
    }

    @Test
    void rejectsZipHeader() {
        assertFalse(DexMagic.hasDexMagic(new byte[]{0x50, 0x4B, 0x03, 0x04, 0, 0, 0, 0}));
    }

    @Test
    void rejectsTruncatedInput() {
        assertFalse(DexMagic.hasDexMagic(new byte[]{0x64, 0x65, 0x78}));
    }

    @Test
    void rejectsNull() {
        assertFalse(DexMagic.hasDexMagic(null));
    }
}
