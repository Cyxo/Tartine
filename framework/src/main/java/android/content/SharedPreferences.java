package android.content;

    public interface SharedPreferences {

    public abstract boolean contains(java.lang.String arg0);

    public abstract android.content.SharedPreferences.Editor edit();

    public abstract java.util.Map<java.lang.String, ?> getAll();

    public abstract boolean getBoolean(java.lang.String arg0, boolean arg1);

    public abstract float getFloat(java.lang.String arg0, float arg1);

    public abstract int getInt(java.lang.String arg0, int arg1);

    public abstract long getLong(java.lang.String arg0, long arg1);

    public abstract java.lang.String getString(java.lang.String arg0, java.lang.String arg1);

    public abstract java.util.Set<java.lang.String> getStringSet(java.lang.String arg0, java.util.Set<java.lang.String> arg1);

    public abstract void registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener arg0);

    public abstract void unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener arg0);
        public static interface Editor {

        public abstract void apply();

        public abstract android.content.SharedPreferences.Editor clear();

        public abstract boolean commit();

        public abstract android.content.SharedPreferences.Editor putBoolean(java.lang.String arg0, boolean arg1);

        public abstract android.content.SharedPreferences.Editor putFloat(java.lang.String arg0, float arg1);

        public abstract android.content.SharedPreferences.Editor putInt(java.lang.String arg0, int arg1);

        public abstract android.content.SharedPreferences.Editor putLong(java.lang.String arg0, long arg1);

        public abstract android.content.SharedPreferences.Editor putString(java.lang.String arg0, java.lang.String arg1);

        public abstract android.content.SharedPreferences.Editor putStringSet(java.lang.String arg0, java.util.Set<java.lang.String> arg1);

        public abstract android.content.SharedPreferences.Editor remove(java.lang.String arg0);
        }
        public static interface OnSharedPreferenceChangeListener {

        public abstract void onSharedPreferenceChanged(android.content.SharedPreferences arg0, java.lang.String arg1);
        }
    }
