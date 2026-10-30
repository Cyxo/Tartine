package tartine.apk;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import tartine.dex.Dex;
import tartine.dex.DexSet;

/**
 * Reads an APK as a ZIP archive and classifies its entries.
 *
 * <p>Layout reminder: {@code AndroidManifest.xml} (binary XML),
 * {@code classes.dex} (+{@code classes2.dex}...), {@code resources.arsc},
 * {@code res/}, {@code assets/}, {@code lib/<abi>/*.so}, {@code META-INF/}.
 */
public final class ApkReader implements AutoCloseable {
    private static final Pattern DEX_NAME = Pattern.compile("classes\\d*\\.dex");
    private static final String NATIVE_PREFIX = "lib/";

    private final ZipFile zip;

    private ApkReader(ZipFile zip) {
        this.zip = zip;
    }

    public static ApkReader open(Path apk) throws IOException {
        return new ApkReader(new ZipFile(apk.toFile()));
    }

    /** All entry names in sorted order. */
    public List<String> entries() {
        List<String> names = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            names.add(entries.nextElement().getName());
        }
        Collections.sort(names);
        return names;
    }

    public byte[] readEntry(String name) throws IOException {
        ZipEntry entry = zip.getEntry(name);
        if (entry == null) {
            throw new IOException("APK has no entry: " + name);
        }
        try (InputStream in = zip.getInputStream(entry)) {
            return in.readAllBytes();
        }
    }

    public boolean hasManifest() {
        return zip.getEntry("AndroidManifest.xml") != null;
    }

    public byte[] manifestBytes() throws IOException {
        return readEntry("AndroidManifest.xml");
    }

    /** Decoded manifest document. */
    public BinaryXml.Element manifest() throws IOException {
        return BinaryXml.parse(manifestBytes());
    }

    /** DEX entry names ({@code classes.dex}, {@code classes2.dex}, ...) in numeric order. */
    public List<String> dexEntries() {
        List<String> dex = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (DEX_NAME.matcher(name).matches()) {
                dex.add(name);
            }
        }
        dex.sort(Comparator.comparingInt(ApkReader::dexNumber));
        return dex;
    }

    private static int dexNumber(String name) {
        if (name.equals("classes.dex")) {
            return 1;
        }
        return Integer.parseInt(name.substring("classes".length(), name.length() - ".dex".length()));
    }

    public boolean hasResources() {
        return zip.getEntry("resources.arsc") != null;
    }

    /** Loads every DEX entry into a multidex set. */
    public DexSet dexSet() throws IOException {
        List<Dex> dexes = new ArrayList<>();
        for (String name : dexEntries()) {
            dexes.add(Dex.load(readEntry(name)));
        }
        return new DexSet(dexes);
    }

    /** ABI names from {@code lib/<abi>/} entries, sorted. */
    public Set<String> nativeAbis() {
        Set<String> abis = new TreeSet<>();
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(NATIVE_PREFIX)) {
                int slash = name.indexOf('/', NATIVE_PREFIX.length());
                if (slash > NATIVE_PREFIX.length()) {
                    abis.add(name.substring(NATIVE_PREFIX.length(), slash));
                }
            }
        }
        return abis;
    }

    @Override
    public void close() throws IOException {
        zip.close();
    }
}
