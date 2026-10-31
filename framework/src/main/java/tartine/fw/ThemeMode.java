package tartine.fw;

/**
 * Host light/dark preference seam. {@code HostConfig} resolves the
 * {@code theme.mode} key (or detects the desktop setting for {@code auto})
 * once at startup and writes the result here; {@code Resources} reads it
 * when building the guest {@code Configuration}. Defaults to light so
 * framework-only runs and tests keep the historical behavior.
 */
public final class ThemeMode {
    private static volatile boolean dark;

    private ThemeMode() {
    }

    /** Sets the host preference: {@code true} = dark, {@code false} = light. */
    public static void setDark(boolean dark) {
        ThemeMode.dark = dark;
    }

    /** Whether the host prefers a dark appearance. */
    public static boolean isDark() {
        return dark;
    }
}
