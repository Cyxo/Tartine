package tartine.config;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Host light/dark probes, pinned with canned command output (no OS calls). */
final class SystemThemeTest {
    private static SystemTheme.Probe probe(int exit, String output) {
        return new SystemTheme.Probe(exit, output);
    }

    private static SystemTheme.Runner fixed(int exit, String output) {
        return command -> probe(exit, output);
    }

    @Test
    void windowsReadsAppsUseLightTheme() {
        String header = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\"
                + "CurrentVersion\\Themes\\Personalize\n";
        assertEquals(Boolean.FALSE, SystemTheme.detect("Windows 11", fixed(0,
                header + "    AppsUseLightTheme    REG_DWORD    0x1\n")));
        assertEquals(Boolean.TRUE, SystemTheme.detect("Windows 11", fixed(0,
                header + "    AppsUseLightTheme    REG_DWORD    0x0\n")));
        assertNull(SystemTheme.detect("Windows 11", fixed(1, "")));
        assertNull(SystemTheme.detect("Windows 11", fixed(0, "no such value")));
    }

    @Test
    void macReadsAppleInterfaceStyle() {
        assertEquals(Boolean.TRUE,
                SystemTheme.detect("Mac OS X", fixed(0, "Dark\n")));
        // The key is absent in light mode, so defaults exits non-zero.
        assertEquals(Boolean.FALSE,
                SystemTheme.detect("Mac OS X", fixed(1, "")));
        assertNull(SystemTheme.detect("Mac OS X", fixed(0, "")));
    }

    @Test
    void linuxReadsGnomeColorSchemeThenGtkTheme() {
        SystemTheme.Runner darkScheme = command -> command.contains("color-scheme")
                ? probe(0, "'prefer-dark'\n")
                : probe(1, "");
        assertEquals(Boolean.TRUE,
                SystemTheme.detect("Linux", darkScheme, null));

        SystemTheme.Runner lightScheme = command -> command.contains("color-scheme")
                ? probe(0, "'default'\n")
                : probe(1, "");
        assertEquals(Boolean.FALSE,
                SystemTheme.detect("Linux", lightScheme, null));

        SystemTheme.Runner darkGtkTheme = command -> command.contains("gtk-theme")
                ? probe(0, "'Adwaita-dark'\n")
                : probe(1, "");
        assertEquals(Boolean.TRUE,
                SystemTheme.detect("Linux", darkGtkTheme, null));
    }

    @Test
    void linuxFallsBackToGtkEnvThenKde() {
        assertEquals(Boolean.TRUE,
                SystemTheme.detect("Linux", command -> probe(1, ""),
                        "Adwaita:dark"));
        assertEquals(Boolean.FALSE,
                SystemTheme.detect("Linux", command -> probe(1, ""),
                        "Adwaita"));

        SystemTheme.Runner kde = command -> command.get(0).equals("kreadconfig6")
                ? probe(0, "BreezeDark\n")
                : probe(1, "");
        assertEquals(Boolean.TRUE,
                SystemTheme.detect("Linux", kde, null));
    }

    @Test
    void unknownPlatformOrFailedProbeIsUnknown() {
        assertNull(SystemTheme.detect("Plan 9", fixed(0, "Dark")));
        assertNull(SystemTheme.detect("Linux", command -> probe(1, ""), null));
        assertNull(SystemTheme.detect("Windows 11",
                command -> null));
    }

    @Test
    void runnerReceivesTheExpectedCommands() {
        List<List<String>> seen = new ArrayList<>();
        SystemTheme.Runner recording = command -> {
            seen.add(command);
            return probe(1, "");
        };
        SystemTheme.detect("Linux", recording, null);
        assertEquals(List.of("gsettings", "get", "org.gnome.desktop.interface",
                "color-scheme"), seen.get(0));
        assertEquals(List.of("gsettings", "get", "org.gnome.desktop.interface",
                "gtk-theme"), seen.get(1));
        assertEquals(List.of("kreadconfig6", "--file", "kdeglobals",
                "--group", "General", "--key", "ColorScheme"), seen.get(2));
    }
}
