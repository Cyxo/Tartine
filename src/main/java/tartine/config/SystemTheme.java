package tartine.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Best-effort detection of the desktop's light/dark preference, used when
 * {@code theme.mode=auto}. Returns {@code null} when the platform has no
 * readable setting (or the probe fails); callers then fall back to light,
 * the historical behavior.
 *
 * <p>Platform probes: Windows reads
 * {@code HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize}
 * {@code AppsUseLightTheme} (0 = dark); macOS asks
 * {@code defaults read -g AppleInterfaceStyle} ("Dark"); Linux tries the
 * GTK theme environment override, GNOME {@code gsettings}, then KDE's
 * {@code kreadconfig}. Each external command is bounded by a short timeout.
 */
final class SystemTheme {
    private static final long TIMEOUT_MS = 1500;
    private static final Pattern WINDOWS_LIGHT = Pattern.compile(
            "(?i)AppsUseLightTheme\\s+REG_DWORD\\s+0x([0-9a-f]+)");

    private SystemTheme() {
    }

    /** {@code TRUE} = dark, {@code FALSE} = light, {@code null} = unknown. */
    static Boolean detect() {
        return detect(System.getProperty("os.name", ""), SystemTheme::exec,
                System.getenv("GTK_THEME"));
    }

    static Boolean detect(String osName, Runner runner) {
        return detect(osName, runner, System.getenv("GTK_THEME"));
    }

    /** {@code gtkTheme}: explicit GTK_THEME override (null when unset). */
    static Boolean detect(String osName, Runner runner, String gtkTheme) {
        String os = osName == null ? "" : osName.toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return detectWindows(runner);
        }
        if (os.contains("mac") || os.contains("darwin")) {
            return detectMac(runner);
        }
        if (os.contains("linux") || os.contains("unix")) {
            return detectLinux(runner, gtkTheme);
        }
        return null;
    }

    private static Boolean detectWindows(Runner runner) {
        Probe probe = runner.run(List.of("reg", "query",
                "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                "/v", "AppsUseLightTheme"));
        if (probe == null || !probe.ok()) {
            return null;
        }
        Matcher matcher = WINDOWS_LIGHT.matcher(probe.text());
        if (!matcher.find()) {
            return null;
        }
        return Long.parseLong(matcher.group(1), 16) == 0;
    }

    private static Boolean detectMac(Runner runner) {
        Probe probe = runner.run(List.of("defaults", "read", "-g",
                "AppleInterfaceStyle"));
        if (probe == null) {
            return null;
        }
        if (!probe.ok()) {
            return Boolean.FALSE; // key absent: macOS is in light mode
        }
        String text = probe.text();
        return text.isEmpty() ? null : text.equalsIgnoreCase("Dark");
    }

    private static Boolean detectLinux(Runner runner, String gtkTheme) {
        Boolean explicit = parseGtkTheme(gtkTheme);
        if (explicit != null) {
            return explicit;
        }
        Boolean scheme = gsettings(runner, "color-scheme");
        if (scheme != null) {
            return scheme;
        }
        Boolean gtk = gsettings(runner, "gtk-theme");
        if (gtk != null) {
            return gtk;
        }
        return kreadconfig(runner);
    }

    /** {@code GTK_THEME=Adwaita:dark} is an explicit user preference. */
    static Boolean parseGtkTheme(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return raw.toLowerCase(Locale.ROOT).contains("dark");
    }

    private static Boolean gsettings(Runner runner, String key) {
        Probe probe = runner.run(List.of("gsettings", "get",
                "org.gnome.desktop.interface", key));
        if (probe == null || !probe.ok()) {
            return null;
        }
        String value = probe.text().toLowerCase(Locale.ROOT);
        if (value.isEmpty()) {
            return null;
        }
        if (value.contains("dark")) {
            return Boolean.TRUE;
        }
        if (value.contains("light") || value.contains("default")) {
            return Boolean.FALSE;
        }
        return null;
    }

    private static Boolean kreadconfig(Runner runner) {
        for (String tool : List.of("kreadconfig6", "kreadconfig5")) {
            Probe probe = runner.run(List.of(tool, "--file", "kdeglobals",
                    "--group", "General", "--key", "ColorScheme"));
            if (probe == null || !probe.ok() || probe.text().isEmpty()) {
                continue;
            }
            return probe.text().toLowerCase(Locale.ROOT).contains("dark");
        }
        return null;
    }

    private static Probe exec(List<String> command) {
        Process process = null;
        try {
            process = new ProcessBuilder(command)
                    .redirectErrorStream(true).start();
            if (!process.waitFor(TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                process.destroyForcibly();
                return null;
            }
            String output = new String(process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);
            return new Probe(process.exitValue(), output);
        } catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (process != null) {
                process.destroyForcibly();
            }
            return null;
        }
    }

    /** One command probe: exit status plus merged stdout/stderr. */
    record Probe(int exit, String output) {
        boolean ok() {
            return exit == 0;
        }

        String text() {
            return output == null ? "" : output.trim();
        }
    }

    /** Command runner seam (tests inject canned probe output). */
    @FunctionalInterface
    interface Runner {
        Probe run(List<String> command);
    }
}
