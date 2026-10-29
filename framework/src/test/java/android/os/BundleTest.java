package android.os;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP parity for the host-backed Bundle table. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/os/BaseBundle.java (mMap, typed getters with the
 * documented default on absence or a wrong-typed value) and
 * core/java/android/os/Bundle.java (reference putters/getters, putAll,
 * copy constructor).
 */
class BundleTest {

    @Test
    void typedPrimitivesRoundTrip() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("bool", true);
        bundle.putInt("int", 42);
        bundle.putLong("long", 9_000_000_000L);
        bundle.putFloat("float", 2.5f);
        bundle.putString("string", "hello");
        bundle.putCharSequence("chars", "world");

        assertTrue(bundle.getBoolean("bool"));
        assertEquals(42, bundle.getInt("int"));
        assertEquals(9_000_000_000L, bundle.getLong("long"));
        assertEquals(2.5f, bundle.getFloat("float"));
        assertEquals("hello", bundle.getString("string"));
        assertEquals("world", bundle.getCharSequence("chars"));
        assertEquals(6, bundle.size());
    }

    @Test
    void missingKeysReturnTheDocumentedDefaults() {
        Bundle bundle = new Bundle();
        assertFalse(bundle.getBoolean("missing"));
        assertTrue(bundle.getBoolean("missing", true));
        assertEquals(0, bundle.getInt("missing"));
        assertEquals(7, bundle.getInt("missing", 7));
        assertEquals(0L, bundle.getLong("missing"));
        assertEquals(8L, bundle.getLong("missing", 8L));
        assertEquals(0.0f, bundle.getFloat("missing"));
        assertEquals(1.5f, bundle.getFloat("missing", 1.5f));
        assertNull(bundle.getString("missing"));
        assertEquals("fallback", bundle.getString("missing", "fallback"));
        assertNull(bundle.getCharSequence("missing"));
        assertEquals("fallback", bundle.getCharSequence("missing", "fallback"));
    }

    @Test
    void wrongTypedValuesFallBackToTheDefault() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", 5);

        assertEquals(5, bundle.getInt("value", 11));
        assertEquals(12L, bundle.getLong("value", 12L));
        assertEquals(1.25f, bundle.getFloat("value", 1.25f));
        assertFalse(bundle.getBoolean("value", false));
        assertTrue(bundle.getBoolean("value", true));
        assertEquals("fallback", bundle.getString("value", "fallback"));
        assertEquals("fallback",
                bundle.getCharSequence("value", "fallback"));
    }

    @Test
    void containerViewAndCopyConstructor() {
        Bundle bundle = new Bundle();
        assertTrue(bundle.isEmpty());
        assertFalse(bundle.containsKey("key"));
        bundle.putInt("key", 1);
        assertTrue(bundle.containsKey("key"));
        assertFalse(bundle.isEmpty());
        assertEquals(1, bundle.get("key"));
        assertEquals(1, bundle.size());
        assertTrue(bundle.keySet().contains("key"));

        Bundle copy = new Bundle(bundle);
        assertEquals(1, copy.getInt("key"));
        copy.putInt("key", 2);
        assertEquals(1, bundle.getInt("key"));
        assertEquals(2, copy.getInt("key"));

        Bundle other = new Bundle();
        other.putString("other", "value");
        copy.putAll(other);
        assertEquals("value", copy.getString("other"));
    }

    @Test
    void nestedBundlesAndTypedLists() {
        Bundle inner = new Bundle();
        inner.putString("name", "inner");

        Bundle outer = new Bundle();
        outer.putBundle("bundle", inner);
        assertSame(inner, outer.getBundle("bundle"));

        ArrayList<String> strings = new ArrayList<>(Arrays.asList("a", "b"));
        outer.putStringArrayList("strings", strings);
        assertEquals(strings, outer.getStringArrayList("strings"));

        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3));
        outer.putIntegerArrayList("integers", integers);
        assertEquals(integers, outer.getIntegerArrayList("integers"));

        // Missing list/bundle keys answer null. ArrayList erasure means a
        // String list and an Integer list are both accepted by the typed
        // getter; AOSP's unchecked cast behaves the same way.
        assertNull(outer.getBundle("strings"));
        assertNull(outer.getStringArrayList("missing"));
        assertNull(outer.getIntegerArrayList("missing"));
    }

    @Test
    void classLoaderIsNeverNull() {
        Bundle bundle = new Bundle();
        assertNotNull(bundle.getClassLoader());
        assertSame(bundle.getClassLoader(),
                new Bundle(bundle).getClassLoader());
    }

    @Test
    void baseBundleRemoveDeletesTheMapping() {
        // Bundle.remove is still a named gap; BaseBundle owns the host map
        // operation and is directly constructible from this package.
        BaseBundle bundle = new BaseBundle();
        bundle.putString("key", "value");
        assertEquals("value", bundle.getString("key"));
        bundle.remove("key");
        assertNull(bundle.getString("key"));
        assertTrue(bundle.isEmpty());
    }
}
