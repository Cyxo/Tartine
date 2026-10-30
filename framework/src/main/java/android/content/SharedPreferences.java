package android.content;

import java.util.Map;
import java.util.Set;

    public interface SharedPreferences {

    public abstract boolean contains(String arg0);

    public abstract SharedPreferences.Editor edit();

    public abstract Map<String, ?> getAll();

    public abstract boolean getBoolean(String arg0, boolean arg1);

    public abstract float getFloat(String arg0, float arg1);

    public abstract int getInt(String arg0, int arg1);

    public abstract long getLong(String arg0, long arg1);

    public abstract String getString(String arg0, String arg1);

    public abstract Set<String> getStringSet(String arg0, Set<String> arg1);

    public abstract void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener arg0);

    public abstract void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener arg0);
        public static interface Editor {

        public abstract void apply();

        public abstract SharedPreferences.Editor clear();

        public abstract boolean commit();

        public abstract SharedPreferences.Editor putBoolean(String arg0, boolean arg1);

        public abstract SharedPreferences.Editor putFloat(String arg0, float arg1);

        public abstract SharedPreferences.Editor putInt(String arg0, int arg1);

        public abstract SharedPreferences.Editor putLong(String arg0, long arg1);

        public abstract SharedPreferences.Editor putString(String arg0, String arg1);

        public abstract SharedPreferences.Editor putStringSet(String arg0, Set<String> arg1);

        public abstract SharedPreferences.Editor remove(String arg0);
        }
        public static interface OnSharedPreferenceChangeListener {

        public abstract void onSharedPreferenceChanged(SharedPreferences arg0, String arg1);
        }
    }
