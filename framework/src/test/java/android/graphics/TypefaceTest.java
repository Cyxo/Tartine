package android.graphics;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The shell Typeface mapping. AOSP Typeface is native-backed; the desktop
 * shell keeps the Java-facing style/weight contract and answers the default
 * face for asset/file factories until a host font manager lands. Source:
 * aosp-mirror/platform_frameworks_base
 * graphics/java/android/graphics/Typeface.java.
 */
class TypefaceTest {

    @Test
    void defaultFaceIsNormalStyle() {
        assertEquals(Typeface.NORMAL, Typeface.DEFAULT.getStyle());
        assertFalse(Typeface.DEFAULT.isBold());
        assertFalse(Typeface.DEFAULT.isItalic());
        assertEquals("sans-serif", Typeface.DEFAULT.getSystemFontFamilyName());
    }

    @Test
    void styleFactoriesSetTheStyleBits() {
        Typeface bold = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        assertEquals(Typeface.BOLD, bold.getStyle());
        assertTrue(bold.isBold());
        assertFalse(bold.isItalic());

        Typeface boldItalic = Typeface.create(Typeface.DEFAULT,
                Typeface.BOLD | Typeface.ITALIC);
        assertEquals(Typeface.BOLD | Typeface.ITALIC, boldItalic.getStyle());
        assertTrue(boldItalic.isBold());
        assertTrue(boldItalic.isItalic());

        Typeface fromNullFamily = Typeface.create((Typeface) null,
                Typeface.BOLD);
        assertEquals(Typeface.BOLD, fromNullFamily.getStyle());

        Typeface named = Typeface.create("serif", Typeface.ITALIC);
        assertEquals(Typeface.ITALIC, named.getStyle());
        assertTrue(named.isItalic());
    }

    @Test
    void defaultFromStyleUsesTheSharedInstances() {
        assertSame(Typeface.DEFAULT_BOLD,
                Typeface.defaultFromStyle(Typeface.BOLD));
        assertSame(Typeface.DEFAULT,
                Typeface.defaultFromStyle(Typeface.NORMAL));
        assertSame(Typeface.DEFAULT,
                Typeface.defaultFromStyle(Typeface.ITALIC));
    }

    @Test
    void assetAndFileFactoriesAnswerTheDefaultFace() {
        assertSame(Typeface.DEFAULT,
                Typeface.createFromAsset(null, "fonts/test.ttf"));
        assertSame(Typeface.DEFAULT,
                Typeface.createFromFile((File) null));
        assertSame(Typeface.DEFAULT,
                Typeface.createFromFile((String) null));
    }

    @Test
    void equalityFollowsStyleAndWeight() {
        Typeface a = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        Typeface b = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, Typeface.DEFAULT);
        assertNotEquals(Typeface.DEFAULT, Typeface.DEFAULT_BOLD);
        assertNotEquals(a, null);
    }
}
