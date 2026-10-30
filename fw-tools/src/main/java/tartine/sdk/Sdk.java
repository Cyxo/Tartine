package tartine.sdk;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Locates the platform {@code android.jar} in a local Android SDK.
 * Roots come from the environment ({@code ANDROID_HOME},
 * {@code ANDROID_SDK_ROOT}) plus OS-standard locations.
 *
 * <p>Dev-time only (the {@code fw-tools} module): the runtime never looks for
 * an SDK. {@code android.jar} is a build input for regenerating the prebuilt
 * framework index and the framework skeletons.
 */
public final class Sdk {
    /**
     * Tartine's modeled Android API level. Regeneration defaults to the
     * {@code android-36} platform so an app targeting API 36 runs on Tartine
     * API 36.
     */
    public static final int TARGET_API = 36;

    private Sdk() {
    }

    /** Exact match for {@code targetSdk}, else the highest available, else null. */
    public static Path findJar(int targetSdk) {
        return findJar(targetSdk, defaultRoots());
    }

    static Path findJar(int targetSdk, List<Path> roots) {
        Path best = null;
        int bestApi = -1;
        for (Path root : roots) {
            Path platforms = root.resolve("platforms");
            Path exact = platforms.resolve("android-" + targetSdk).resolve("android.jar");
            if (Files.isRegularFile(exact)) {
                return exact;
            }
            try (DirectoryStream<Path> dirs = Files.newDirectoryStream(platforms, "android-*")) {
                for (Path dir : dirs) {
                    int api = apiLevel(dir.getFileName().toString());
                    Path jar = dir.resolve("android.jar");
                    if (api > bestApi && Files.isRegularFile(jar)) {
                        bestApi = api;
                        best = jar;
                    }
                }
            } catch (IOException | UnsupportedOperationException e) {
                continue;
            }
        }
        return best;
    }

    static int apiLevel(String name) {
        if (!name.startsWith("android-")) {
            return -1;
        }
        int end = "android-".length();
        while (end < name.length() && Character.isDigit(name.charAt(end))) {
            end++;
        }
        if (end == "android-".length()) {
            return -1;
        }
        try {
            return Integer.parseInt(name.substring("android-".length(), end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** API level encoded in a platform jar's parent directory ({@code android-36}), or -1. */
    static int apiLevelOf(Path jar) {
        Path platform = jar.getParent();
        return platform == null || platform.getFileName() == null
                ? -1 : apiLevel(platform.getFileName().toString());
    }

    static List<Path> defaultRoots() {
        List<Path> roots = new ArrayList<>();
        String home = System.getenv("ANDROID_HOME");
        if (home != null && !home.isEmpty()) {
            roots.add(Paths.get(home));
        }
        String sdkRoot = System.getenv("ANDROID_SDK_ROOT");
        if (sdkRoot != null && !sdkRoot.isEmpty()) {
            roots.add(Paths.get(sdkRoot));
        }
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String userHome = System.getProperty("user.home", "");
        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            if (localAppData != null && !localAppData.isEmpty()) {
                roots.add(Paths.get(localAppData, "Android", "Sdk"));
            }
        } else if (os.contains("mac")) {
            roots.add(Paths.get(userHome, "Library", "Android", "sdk"));
        } else {
            roots.add(Paths.get(userHome, "Android", "Sdk"));
            roots.add(Paths.get("/opt/android-sdk"));
            roots.add(Paths.get("/usr/lib/android-sdk"));
        }
        return roots;
    }
}