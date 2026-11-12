package android.content.res;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import tartine.fw.AppResourceHost;
import tartine.fw.AppResources;

/**
 * Tartine: the asset manager seam. On a device this is the native
 * {@code AssetManager} over the APK's {@code assets/} directory; the shell
 * reads those entries through the installed {@link AppResourceHost}, keyed by
 * the {@code assets/}-prefixed APK path.
 */
public class AssetManager implements AutoCloseable {

    public static final int ACCESS_BUFFER = 3;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_UNKNOWN = 0;

    protected AssetManager() {
    }

    public void close() {
        // the host owns the APK reader; nothing to release here
    }

    protected void finalize() {
        close();
    }

    public String[] getLocales() {
        // No locale overlay pipeline yet; the shell serves one locale.
        return new String[0];
    }

    public String[] list(String path) {
        AppResourceHost host = AppResources.host();
        if (host == null) {
            return new String[0];
        }
        String normalized = path == null ? "" : path;
        String[] names = host.assetList("assets/"
                + (normalized.isEmpty() ? "" : normalized));
        return names == null ? new String[0] : names;
    }

    public InputStream open(String fileName) throws FileNotFoundException {
        return open(fileName, ACCESS_STREAMING);
    }

    public InputStream open(String fileName, int accessMode) throws FileNotFoundException {
        AppResourceHost host = AppResources.host();
        byte[] bytes = host == null ? null : host.asset(assetPath(fileName));
        if (bytes == null) {
            throw new FileNotFoundException("asset not found: " + fileName);
        }
        return new ByteArrayInputStream(bytes);
    }

    public AssetFileDescriptor openFd(String fileName) throws java.io.IOException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: AssetManager.openFd(String) "
                        + "(assets are not exposed as file descriptors)");
    }

    public AssetFileDescriptor openNonAssetFd(int cookie, String fileName)
            throws java.io.IOException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: AssetManager.openNonAssetFd(int, String)");
    }

    public AssetFileDescriptor openNonAssetFd(String fileName) throws java.io.IOException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: AssetManager.openNonAssetFd(String)");
    }

    public XmlResourceParser openXmlResourceParser(int cookie, String fileName)
            throws java.io.IOException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: AssetManager.openXmlResourceParser(int, String)");
    }

    public XmlResourceParser openXmlResourceParser(String fileName)
            throws java.io.IOException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: AssetManager.openXmlResourceParser(String)");
    }

    private static String assetPath(String fileName) {
        if (fileName == null) {
            return null;
        }
        return fileName.startsWith("assets/") ? fileName : "assets/" + fileName;
    }
}
