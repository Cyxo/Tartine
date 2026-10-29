package android.os;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP parity for the immutable locale list. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/os/LocaleList.java (constructor copy/dedup rules,
 * get out-of-range -> null, toLanguageTags/toString, forLanguageTags,
 * getFirstMatch).
 */
class LocaleListTest {

    @Test
    void emptyListIsTrulyEmpty() {
        LocaleList empty = LocaleList.getEmptyLocaleList();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertEquals("", empty.toLanguageTags());
        assertEquals("[]", empty.toString());
        assertNull(empty.get(0));
        assertNull(empty.get(-1));
        assertNull(empty.getFirstMatch(new String[] { "en-US" }));
        assertSame(empty, LocaleList.forLanguageTags(null));
        assertSame(empty, LocaleList.forLanguageTags(""));
        assertEquals(empty, new LocaleList());
        assertTrue(new LocaleList().isEmpty());
    }

    @Test
    void constructorCopiesLocalesAndDropsDuplicates() {
        LocaleList list = new LocaleList(Locale.US, Locale.US, Locale.FRANCE);
        assertEquals(2, list.size());
        assertFalse(list.isEmpty());
        assertEquals("en-US,fr-FR", list.toLanguageTags());
        // Locale.toString uses underscores, unlike toLanguageTag.
        assertEquals("[en_US,fr_FR]", list.toString());
        assertEquals(Locale.US, list.get(0));
        assertEquals(Locale.FRANCE, list.get(1));
        assertNull(list.get(2));
    }

    @Test
    void constructorRejectsNulls() {
        assertThrows(NullPointerException.class,
                () -> new LocaleList((Locale[]) null));
        assertThrows(NullPointerException.class,
                () -> new LocaleList(Locale.US, null));
    }

    @Test
    void indexOfAndEqualityFollowTheListOrder() {
        LocaleList list = new LocaleList(Locale.US, Locale.FRANCE);
        assertEquals(0, list.indexOf(Locale.US));
        assertEquals(1, list.indexOf(Locale.FRANCE));
        assertEquals(-1, list.indexOf(Locale.GERMANY));

        LocaleList same = new LocaleList(Locale.US, Locale.FRANCE);
        assertEquals(list, same);
        assertEquals(list.hashCode(), same.hashCode());
        assertNotEquals(list, new LocaleList(Locale.FRANCE, Locale.US));
        assertNotEquals(list, null);
    }

    @Test
    void forLanguageTagsRoundTripsAndDropsTrailingEmptyTags() {
        LocaleList list = LocaleList.forLanguageTags("en-US,fr-FR");
        assertEquals(2, list.size());
        assertEquals("en-US,fr-FR", list.toLanguageTags());
        assertEquals(Locale.US, list.get(0));
        assertEquals(Locale.FRANCE, list.get(1));

        // AOSP String.split(",") drops trailing empty fields.
        assertEquals(1, LocaleList.forLanguageTags("en-US,").size());
    }

    @Test
    void getDefaultContainsTheHostDefaultLocale() {
        LocaleList defaults = LocaleList.getDefault();
        assertEquals(1, defaults.size());
        assertEquals(Locale.getDefault(), defaults.get(0));
    }

    @Test
    void getFirstMatchPrefersExactTagsAndFallsBackToTheFirstLocale() {
        LocaleList list = new LocaleList(Locale.FRANCE, Locale.US);
        assertEquals(Locale.US, list.getFirstMatch(new String[] { "en-US" }));
        assertEquals(Locale.FRANCE, list.getFirstMatch(new String[] { "de-DE" }));
        assertEquals(Locale.FRANCE, list.getFirstMatch(new String[0]));
    }
}
