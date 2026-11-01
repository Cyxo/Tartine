package tartine.guest;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import tartine.tx.TranslatedLoader;

/**
 * Guest-facing {@link ClassLoader} facade.
 *
 * <p>{@code Class.getClassLoader()} is rewritten by the translator to
 * {@link GuestReflect#getClassLoader}, which wraps the guest
 * {@code TranslatedLoader} in this class. The facade keeps the loader type
 * assignable where translated signatures expect a {@code ClassLoader}, while
 * every operation that could expose the host classpath answers the guest-safe
 * result: no resources, no parent, and class lookup through the sandboxed
 * guest loader.
 */
public final class GuestClassLoader extends ClassLoader {
    private final TranslatedLoader delegate;

    GuestClassLoader(TranslatedLoader delegate) {
        super(null);
        this.delegate = delegate;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        return delegate.loadGuestClass(name);
    }

    @Override
    public URL getResource(String name) {
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) {
        return Collections.emptyEnumeration();
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return null;
    }

    @Override
    public String toString() {
        return "GuestClassLoader";
    }
}
