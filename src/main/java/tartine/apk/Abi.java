package tartine.apk;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

/**
 * Host ABI matching for {@code lib/<abi>/} selection.
 */
public final class Abi {
    private Abi() {
    }

    /** Maps {@code os.arch} to the closest Android ABI directory name. */
    public static String hostAbi() {
        String arch = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
        if (arch.equals("amd64") || arch.equals("x86_64")) {
            return "x86_64";
        }
        if (arch.equals("x86") || arch.equals("i386") || arch.equals("i686")) {
            return "x86";
        }
        if (arch.equals("aarch64") || arch.equals("arm64")) {
            return "arm64-v8a";
        }
        if (arch.startsWith("arm")) {
            return "armeabi-v7a";
        }
        return arch;
    }

    /**
     * Exact host match if present, else first in sorted order, else null.
     */
    public static String pickBest(Set<String> available) {
        if (available.isEmpty()) {
            return null;
        }
        String host = hostAbi();
        if (available.contains(host)) {
            return host;
        }
        return new TreeSet<>(available).first();
    }
}
