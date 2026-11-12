package android.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * Tartine: a context that carries a resource theme, mirroring AOSP
 * {@code android.app.ContextThemeWrapper}. {@code Activity} and the platform
 * view hierarchy build their UI through this wrapper: the theme resource id
 * set here is applied to a {@link Resources.Theme} that {@code getTheme()}
 * hands to {@code obtainStyledAttributes} and {@code LayoutInflater}.
 */
public class ContextThemeWrapper extends ContextWrapper {

    private int mThemeResource;
    private Resources.Theme mTheme;
    private Configuration mOverrideConfiguration;

    public ContextThemeWrapper() {
        super(null);
    }

    public ContextThemeWrapper(Context base, Resources.Theme theme) {
        super(base);
        mTheme = theme;
    }

    public ContextThemeWrapper(Context base, int themeResId) {
        super(base);
        mThemeResource = themeResId;
    }

    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (mTheme != null) {
            throw new IllegalStateException(
                    "getResources() or getAssets() has already been called");
        }
        mOverrideConfiguration = overrideConfiguration;
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        if (mThemeResource != 0) {
            initializeTheme();
        }
    }

    public AssetManager getAssets() {
        return getBaseContext() == null
                ? super.getAssets() : getBaseContext().getAssets();
    }

    public Resources getResources() {
        return getBaseContext() == null
                ? super.getResources() : getBaseContext().getResources();
    }

    public Object getSystemService(String name) {
        return getBaseContext() == null
                ? super.getSystemService(name)
                : getBaseContext().getSystemService(name);
    }

    public Resources.Theme getTheme() {
        if (mTheme != null) {
            return mTheme;
        }
        initializeTheme();
        return mTheme;
    }

    public int getThemeResId() {
        return mThemeResource;
    }

    private void initializeTheme() {
        if (mTheme == null) {
            mTheme = getResources().newTheme();
            Context base = getBaseContext();
            if (base != null && base.getTheme() != null) {
                mTheme.setTo(base.getTheme());
            }
        }
        onApplyThemeResource(mTheme, mThemeResource, false);
    }

    protected void onApplyThemeResource(Resources.Theme theme, int resid,
            boolean first) {
        theme.applyStyle(resid, true);
    }

    public void setTheme(Resources.Theme theme) {
        mTheme = theme;
    }

    public void setTheme(int resid) {
        mThemeResource = resid;
        mTheme = null;
        initializeTheme();
    }

    /** The override configuration requested before first resource access. */
    public Configuration getOverrideConfiguration() {
        return mOverrideConfiguration;
    }
}
