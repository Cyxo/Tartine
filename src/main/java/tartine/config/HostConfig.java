package tartine.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tartine.fw.SystemColors;
import tartine.fw.SystemResourceNames;
import tartine.runtime.Vfs;

/**
 * Loads the optional host configuration file {@code config.properties}
 * from the per-user Tartine root (the same folder the virtual filesystem
 * uses; README Configuration documents every key). Host-only state: the
 * file is never guest-mapped, and the mount-table rules (AGENTS VFS
 * tiers) keep it that way when sdcard tiers land.
 *
 * <p>Keys — unknown keys and bad values are reported on stderr and
 * skipped (fail-open per key, never silently):
 * <ul>
 *   <li>{@code color.<key>} = 32-bit ARGB — Android notation
 *       ({@code #RRGGBB}, answered opaque, or {@code #AARRGGBB}), or
 *       literal {@code 0xAARRGGBB}, or decimal — overrides
 *       one android-system color resource for this run, keyed by the
 *       canonical {@code android.R$color} constant name (e.g.
 *       {@code color.system_accent1_10}) or a decimal/0x numeric id.</li>
 *   <li>{@code font.families} = comma-separated preferred desktop font
 *       families (config wins over a {@code TARTINE_FONT_FAMILIES} env
 *       value; the built-in list is last).</li>
 * </ul>
 *
 * <p>A missing or unreadable file loads defaults; values apply once at
 * process start and live for the whole run (fresh JVM per run, so stale
 * overrides never persist).
 */
public final class HostConfig {
    static final String FILE_NAME = "config.properties";

    private HostConfig() {
    }

    /** Loads {@code <root>/config.properties} (defaults when absent). */
    public static void load() {
        loadFrom(Vfs.root().resolve(FILE_NAME));
    }

    static void loadFrom(Path file) {
        if (!Files.isRegularFile(file)) {
            return;
        }
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(file,
                StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            System.err.println("Tartine: config " + file + " unreadable: " + e);
            return;
        }
        apply(props);
        if (!props.containsKey("font.families")) {
            // Env fallback: TARTINE_FONT_FAMILIES = same family list.
            String env = System.getenv("TARTINE_FONT_FAMILIES");
            List<String> families = parseFamilies(env);
            if (!families.isEmpty()) {
                android.graphics.Paint.tartineSetPreferredFamilies(
                        families.toArray(new String[0]));
            }
        }
    }

    /** Applies parsed config keys (stderr + skip on any bad value). */
    static void apply(Properties props) {
        for (String name : props.stringPropertyNames()) {
            String value = props.getProperty(name);
            if (name.startsWith("color.")) {
                applyColor(name, value);
            } else if (name.equals("font.families")) {
                List<String> families = parseFamilies(value);
                if (families.isEmpty()) {
                    System.err.println("Tartine: config ignored: "
                            + name + "=" + value
                            + " (need a comma-separated family list)");
                    continue;
                }
                android.graphics.Paint.tartineSetPreferredFamilies(
                        families.toArray(new String[0]));
            } else {
                System.err.println("Tartine: config ignored: unknown key '"
                        + name + "'");
            }
        }
    }

    private static void applyColor(String name, String value) {
        Integer id = namedColorId(name.substring("color.".length()));
        Integer argb = parseColor(value);
        if (id == null || argb == null) {
            System.err.println("Tartine: config ignored: " + name + "=" + value
                    + " (need a canonical android.R$color name like"
                    + " system_accent1_10, a decimal-or-0x color id, and a"
                    + " #RRGGBB (opaque) / #AARRGGBB / 0xAARRGGBB / decimal"
                    + " ARGB value)");
            return;
        }
        SystemColors.setOverride(id, argb);
    }

    /**
     * A color key is either a canonical {@code android.R$color} constant
     * name (the spelling every ROM overlay uses; canonical because it is
     * the jar constant, not an alias we invent) or a numeric id. Names
     * resolve through the generated
     * {@code tartine.fw.SystemResourceNames} table (extracted from the
     * LOCAL SDK jar); {@code android.R$color} itself lives only in the
     * dev-time jar, never at runtime (see the audit's SDK-free-runtime
     * finding).
     */
    private static Integer namedColorId(String raw) {
        String text = raw.trim();
        Integer id = SystemResourceNames.idForName(text);
        if (id != null) {
            return id;
        }
        return parseId(text);
    }

    /** Decimal or {@code 0x}-hex id; positive, int-range. */
    private static Integer parseId(String raw) {
        return parseInt(raw, 0x7FFF_FFFFL);
    }

    /** {@code 0x}-hex or decimal 32-bit ARGB, literal; Android's
     * {@code #RRGGBB} notation answers opaque via {@link #parseColor}. */
    private static Integer parseColor(String raw) {
        if (raw == null) {
            return null;
        }
        String text = raw.trim();
        if (text.startsWith("#")) {
            String hex = text.substring(1);
            try {
                long value = Long.parseLong(hex, 16);
                if (hex.length() == 6) { // Android notation: #RRGGBB opaque
                    value |= 0xFF000000L;
                }
                if (value < 0 || value > 0xFFFF_FFFFL) {
                    return null;
                }
                return (int) value;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return parseInt(text, 0xFFFF_FFFFL);
    }

    private static Integer parseInt(String raw, long max) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String text = raw.trim();
        int skip = 0;
        if (text.startsWith("0x") || text.startsWith("0X")) {
            skip = 2; // otherwise decimal; colors with # parse first
        }
        try {
            long value = Long.parseLong(text.substring(skip), skip > 0 ? 16 : 10);
            if (value < 0 || value > max) {
                return null;
            }
            return (int) value;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Comma-separated, trimmed, non-blank family names. */
    private static List<String> parseFamilies(String raw) {
        List<String> out = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return out;
        }
        for (String part : raw.split(",")) {
            String family = part.trim();
            if (!family.isEmpty()) {
                out.add(family);
            }
        }
        return out;
    }
}
