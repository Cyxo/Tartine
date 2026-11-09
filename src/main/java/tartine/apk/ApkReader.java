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

    /** Reads one ZIP entry's bytes; throws when the entry is absent. */
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

    /**
     * Permissions named by {@code <uses-permission>} (including the legacy
     * {@code uses-permission-sdk-23}/{@code -sdk-m} spellings), in manifest
     * order and without duplicates.
     */
    public List<String> declaredPermissions() throws IOException {
        List<String> names = new ArrayList<>();
        for (BinaryXml.Element child : manifest().children) {
            if (!child.name.equals("uses-permission")
                    && !child.name.equals("uses-permission-sdk-23")
                    && !child.name.equals("uses-permission-sdk-m")) {
                continue;
            }
            String name = child.attr("name");
            if (name != null && !name.isEmpty() && !names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    /**
     * Manifest {@code targetSdkVersion}, falling back to
     * {@code minSdkVersion} and then API 1 (the pre-runtime-permission
     * default) when the attribute is absent or a preview codename.
     */
    public int targetSdk() throws IOException {
        BinaryXml.Element sdk = manifest().child("uses-sdk");
        if (sdk == null) {
            return 1;
        }
        int min = sdkInt(sdk.attr("minSdkVersion"), 1);
        return sdkInt(sdk.attr("targetSdkVersion"), min);
    }

    private static int sdkInt(String value, int fallback) {
        if (value == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return fallback;
        }
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
        return Integer.parseInt(
                name.substring("classes".length(), name.length() - ".dex".length()));
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
