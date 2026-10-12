package tartine.fw;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Locates the platform {@code android.jar} in a local Android SDK.
 * Roots come from the environment ({@code ANDROID_HOME},
 * {@code ANDROID_SDK_ROOT}) plus OS-standard locations.
 */
public final class Sdk {
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
        String os = System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT);
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
