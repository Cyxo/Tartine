package android.provider;

import android.content.ContentResolver;
import android.net.Uri;

public final class Settings {
    public static final String AUTHORITY = "settings";

    public static final class Global {
        public static final String ANIMATOR_DURATION_SCALE =
                "animator_duration_scale";
        public static final String TRANSITION_ANIMATION_SCALE =
                "transition_animation_scale";
        public static final String WINDOW_ANIMATION_SCALE =
                "window_animation_scale";

        public static Uri getUriFor(String name) {
            return Uri.parse("content://" + AUTHORITY + "/global/" + name);
        }

        public static float getFloat(ContentResolver cr,
                String name, float def) {
            return def;
        }

        // Settings... methods often return int too
        public static int getInt(ContentResolver cr,
                String name, int def) {
            return def;
        }

        public static String getString(ContentResolver cr,
                String name) {
            return null;
        }
    }

    public static final class System {
        public static final String FONT_SCALE = "font_scale";

        public static Uri getUriFor(String name) {
            return Uri.parse("content://" + AUTHORITY + "/system/" + name);
        }
    }

    public static final class Secure {
        public static final Uri getUriFor(String name) {
            return Uri.parse("content://" + AUTHORITY + "/secure/" + name);
        }
    }
}
