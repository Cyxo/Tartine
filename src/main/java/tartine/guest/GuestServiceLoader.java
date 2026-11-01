package tartine.guest;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Empty, guest-safe replacement for {@code java.util.ServiceLoader}.
 *
 * <p>Service discovery would instantiate provider classes the guest dex set
 * may not contain (and the host classpath must not expose). The translator
 * routes the loader chain here so libraries that probe for optional services
 * see "no providers" and take their documented fallback instead of failing
 * the whole enclosing method. The classloader argument is accepted but
 * ignored; guest code only ever passes the guest loader.
 */
public final class GuestServiceLoader implements Iterable<Object> {
    private static final GuestServiceLoader EMPTY = new GuestServiceLoader();

    private GuestServiceLoader() {
    }

    public static GuestServiceLoader load(Class<?> service) {
        return EMPTY;
    }

    public static GuestServiceLoader load(Class<?> service, Object loader) {
        return EMPTY;
    }

    public static GuestServiceLoader loadInstalled(Class<?> service) {
        return EMPTY;
    }

    @Override
    public Iterator<Object> iterator() {
        return Collections.emptyIterator();
    }

    public Stream<?> stream() {
        return Stream.empty();
    }

    public Optional<?> findFirst() {
        return Optional.empty();
    }

    public Optional<?> findNext() {
        return Optional.empty();
    }

    public void reload() {
        // no providers to reload
    }
}
