package android.content;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.IntConsumer;

    public class ContextWrapper extends Context {

    /** AOSP ContextWrapper: the package-visible base (file line 78). */
    Context mBase;

    /** AOSP ContextWrapper ctor (lines 95-96). */
    public ContextWrapper(Context arg0) {
        mBase = arg0;
    }

    /** AOSP ContextWrapper.attachBaseContext (lines 106-111). */
    protected void attachBaseContext(Context arg0) {
        if (mBase != null) {
            throw new IllegalStateException("Base context already set");
        }
        mBase = arg0;
    }

    /** AOSP ContextWrapper.getBaseContext (line 116-118). */
    public Context getBaseContext() {
        return mBase;
    }

    public boolean bindIsolatedService(Intent arg0, int arg1, String arg2, Executor arg3, ServiceConnection arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindIsolatedService(Landroid/content/Intent;ILjava/lang/String;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(Intent arg0, Context.BindServiceFlags arg1, Executor arg2, ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;Landroid/content/Context$BindServiceFlags;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(Intent arg0, ServiceConnection arg1, Context.BindServiceFlags arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;)Z");
    }

    public boolean bindService(Intent arg0, ServiceConnection arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;I)Z");
    }

    public boolean bindService(Intent arg0, int arg1, Executor arg2, ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;ILjava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindServiceAsUser(Intent arg0, ServiceConnection arg1, Context.BindServiceFlags arg2, UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;Landroid/os/UserHandle;)Z");
    }

    public boolean bindServiceAsUser(Intent arg0, ServiceConnection arg1, int arg2, UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;ILandroid/os/UserHandle;)Z");
    }

    public int checkCallingOrSelfPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingOrSelfPermission(Ljava/lang/String;)I");
    }

    public int checkCallingOrSelfUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingOrSelfUriPermission(Landroid/net/Uri;I)I");
    }

    public int[] checkCallingOrSelfUriPermissions(List<Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingOrSelfUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkCallingPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingPermission(Ljava/lang/String;)I");
    }

    public int checkCallingUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingUriPermission(Landroid/net/Uri;I)I");
    }

    public int[] checkCallingUriPermissions(List<Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkContentUriPermissionFull(Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkContentUriPermissionFull(Landroid/net/Uri;III)I");
    }

    public int checkPermission(String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkPermission(Ljava/lang/String;II)I");
    }

    public int checkSelfPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkSelfPermission(Ljava/lang/String;)I");
    }

    public int checkUriPermission(Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkUriPermission(Landroid/net/Uri;III)I");
    }

    public int checkUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkUriPermission(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;III)I");
    }

    public int[] checkUriPermissions(List<Uri> arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkUriPermissions(Ljava/util/List;III)[I");
    }

    public void clearWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->clearWallpaper()V");
    }

    public Context createAttributionContext(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createAttributionContext(Ljava/lang/String;)Landroid/content/Context;");
    }

    public Context createConfigurationContext(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createConfigurationContext(Landroid/content/res/Configuration;)Landroid/content/Context;");
    }

    public Context createContext(ContextParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createContext(Landroid/content/ContextParams;)Landroid/content/Context;");
    }

    public Context createContextForSplit(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createContextForSplit(Ljava/lang/String;)Landroid/content/Context;");
    }

    public Context createDeviceContext(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createDeviceContext(I)Landroid/content/Context;");
    }

    public Context createDeviceProtectedStorageContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createDeviceProtectedStorageContext()Landroid/content/Context;");
    }

    public Context createDisplayContext(Display arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createDisplayContext(Landroid/view/Display;)Landroid/content/Context;");
    }

    public Context createPackageContext(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createPackageContext(Ljava/lang/String;I)Landroid/content/Context;");
    }

    public Context createWindowContext(Display arg0, int arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createWindowContext(Landroid/view/Display;ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public Context createWindowContext(int arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createWindowContext(ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public String[] databaseList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->databaseList()[Ljava/lang/String;");
    }

    public boolean deleteDatabase(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->deleteDatabase(Ljava/lang/String;)Z");
    }

    public boolean deleteFile(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->deleteFile(Ljava/lang/String;)Z");
    }

    public boolean deleteSharedPreferences(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->deleteSharedPreferences(Ljava/lang/String;)Z");
    }

    public void enforceCallingOrSelfPermission(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingOrSelfPermission(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void enforceCallingOrSelfUriPermission(Uri arg0, int arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingOrSelfUriPermission(Landroid/net/Uri;ILjava/lang/String;)V");
    }

    public void enforceCallingPermission(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingPermission(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void enforceCallingUriPermission(Uri arg0, int arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingUriPermission(Landroid/net/Uri;ILjava/lang/String;)V");
    }

    public void enforcePermission(String arg0, int arg1, int arg2, String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforcePermission(Ljava/lang/String;IILjava/lang/String;)V");
    }

    public void enforceUriPermission(Uri arg0, int arg1, int arg2, int arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceUriPermission(Landroid/net/Uri;IIILjava/lang/String;)V");
    }

    public void enforceUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5, String arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceUriPermission(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;IIILjava/lang/String;)V");
    }

    public String[] fileList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->fileList()[Ljava/lang/String;");
    }

    public Context getApplicationContext() {
        // AOSP frameworks-base core/java/android/content/ContextWrapper.java:
        // getApplicationContext delegates to the base context; the shell
        // context is its own root, so a null base answers super
        // (which returns itself here).
        if (mBase != null) {
            return mBase.getApplicationContext();
        }
        return super.getApplicationContext();
    }

    public ApplicationInfo getApplicationInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;");
    }

    public AssetManager getAssets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getAssets()Landroid/content/res/AssetManager;");
    }

    public AttributionSource getAttributionSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getAttributionSource()Landroid/content/AttributionSource;");
    }

    public String getAttributionTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getAttributionTag()Ljava/lang/String;");
    }

    public File getCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getCacheDir()Ljava/io/File;");
    }

    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getClassLoader()Ljava/lang/ClassLoader;");
    }

    public File getCodeCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getCodeCacheDir()Ljava/io/File;");
    }

    public ContentResolver getContentResolver() {
        if (mBase != null) {
            return mBase.getContentResolver();
        }
        return new ContentResolver(this);
    }

    public File getDataDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDataDir()Ljava/io/File;");
    }

    public File getDatabasePath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDatabasePath(Ljava/lang/String;)Ljava/io/File;");
    }

    public int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDeviceId()I");
    }

    public File getDir(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDir(Ljava/lang/String;I)Ljava/io/File;");
    }

    public Display getDisplay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDisplay()Landroid/view/Display;");
    }

    public File getExternalCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalCacheDir()Ljava/io/File;");
    }

    public File[] getExternalCacheDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalCacheDirs()[Ljava/io/File;");
    }

    public File getExternalFilesDir(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalFilesDir(Ljava/lang/String;)Ljava/io/File;");
    }

    public File[] getExternalFilesDirs(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalFilesDirs(Ljava/lang/String;)[Ljava/io/File;");
    }

    public File[] getExternalMediaDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalMediaDirs()[Ljava/io/File;");
    }

    public File getFileStreamPath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getFileStreamPath(Ljava/lang/String;)Ljava/io/File;");
    }

    public File getFilesDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getFilesDir()Ljava/io/File;");
    }

    public Executor getMainExecutor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getMainExecutor()Ljava/util/concurrent/Executor;");
    }

    public Looper getMainLooper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getMainLooper()Landroid/os/Looper;");
    }

    public File getNoBackupFilesDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getNoBackupFilesDir()Ljava/io/File;");
    }

    public File getObbDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getObbDir()Ljava/io/File;");
    }

    public File[] getObbDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getObbDirs()[Ljava/io/File;");
    }

    public String getOpPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getOpPackageName()Ljava/lang/String;");
    }

    public String getPackageCodePath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageCodePath()Ljava/lang/String;");
    }

    public PackageManager getPackageManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageManager()Landroid/content/pm/PackageManager;");
    }

    public String getPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageName()Ljava/lang/String;");
    }

    public String getPackageResourcePath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageResourcePath()Ljava/lang/String;");
    }

    public ContextParams getParams() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getParams()Landroid/content/ContextParams;");
    }

    /**
     * AOSP ContextWrapper.getResources (lines 126-128) delegates to the
     * base; the shell deviates when a wrapper is its own context root —
     * shell-owned activities run without attachBaseContext (no
     * ActivityThread attach), so an unattached wrapper answers its own
     * Context.getResources() instead of NPE-ing on the null base (context
     * flows back for real once the launch pipeline fills attach()).
     */
    public Resources getResources() {
        if (mBase != null) {
            return mBase.getResources();
        }
        return super.getResources();
    }

    public SharedPreferences getSharedPreferences(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;");
    }

    public Object getSystemService(String arg0) {
        // AOSP frameworks-base core/java/android/content/ContextWrapper.java:
        // getSystemService delegates to the base context; the shell
        // context is its own root, so a null base answers super
        // (matching the getResources() shape above).
        if (mBase != null) {
            return mBase.getSystemService(arg0);
        }
        return super.getSystemService(arg0);
    }

    public String getSystemServiceName(Class<?> arg0) {
        // AOSP frameworks-base core/java/android/content/ContextWrapper.java:
        // getSystemServiceName delegates to the base context; the shell
        // context is its own root, so a null base answers super
        // (matching the getResources() shape above).
        if (mBase != null) {
            return mBase.getSystemServiceName(arg0);
        }
        return super.getSystemServiceName(arg0);
    }

    public Resources.Theme getTheme() {
        // AOSP ContextWrapper.getTheme: delegates to mBase.getTheme();
        // the shell has no base context theming, so it lazily owns one
        // Theme per context (matches the interpreter's per-context cached
        // placeholder).
        if (mTheme == null) {
            mTheme = new Resources.Theme();
        }
        return mTheme;
    }

    private Resources.Theme mTheme;

    public Drawable getWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getWallpaper()Landroid/graphics/drawable/Drawable;");
    }

    public int getWallpaperDesiredMinimumHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getWallpaperDesiredMinimumHeight()I");
    }

    public int getWallpaperDesiredMinimumWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getWallpaperDesiredMinimumWidth()I");
    }

    public void grantUriPermission(String arg0, Uri arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->grantUriPermission(Ljava/lang/String;Landroid/net/Uri;I)V");
    }

    public boolean isDeviceProtectedStorage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->isDeviceProtectedStorage()Z");
    }

    public boolean isRestricted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->isRestricted()Z");
    }

    public boolean isUiContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->isUiContext()Z");
    }

    public boolean moveDatabaseFrom(Context arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->moveDatabaseFrom(Landroid/content/Context;Ljava/lang/String;)Z");
    }

    public boolean moveSharedPreferencesFrom(Context arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->moveSharedPreferencesFrom(Landroid/content/Context;Ljava/lang/String;)Z");
    }

    public FileInputStream openFileInput(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openFileInput(Ljava/lang/String;)Ljava/io/FileInputStream;");
    }

    public FileOutputStream openFileOutput(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openFileOutput(Ljava/lang/String;I)Ljava/io/FileOutputStream;");
    }

    public SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openOrCreateDatabase(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2, DatabaseErrorHandler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openOrCreateDatabase(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;Landroid/database/DatabaseErrorHandler;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public Drawable peekWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->peekWallpaper()Landroid/graphics/drawable/Drawable;");
    }

    public void registerComponentCallbacks(ComponentCallbacks arg0) {
        // AOSP ContextWrapper.registerComponentCallbacks: delegates to
        // mBase.registerComponentCallbacks. No component lifecycle runs
        // headless, so callbacks never fire — the shell drops the
        // registration (a no-op, matching AOSP's headless behavior).
    }

    public void registerDeviceIdChangeListener(Executor arg0, IntConsumer arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerDeviceIdChangeListener(Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;I)Landroid/content/Intent;");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;)Landroid/content/Intent;");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;I)Landroid/content/Intent;");
    }

    public void removeStickyBroadcast(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->removeStickyBroadcast(Landroid/content/Intent;)V");
    }

    public void removeStickyBroadcastAsUser(Intent arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->removeStickyBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void revokeSelfPermissionsOnKill(Collection<String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->revokeSelfPermissionsOnKill(Ljava/util/Collection;)V");
    }

    public void revokeUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->revokeUriPermission(Landroid/net/Uri;I)V");
    }

    public void revokeUriPermission(String arg0, Uri arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->revokeUriPermission(Ljava/lang/String;Landroid/net/Uri;I)V");
    }

    public void sendBroadcast(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcast(Landroid/content/Intent;)V");
    }

    public void sendBroadcast(Intent arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;)V");
    }

    public void sendBroadcast(Intent arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendBroadcastAsUser(Intent arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void sendBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Ljava/lang/String;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, int arg1, String arg2, String arg3, BroadcastReceiver arg4, Handler arg5, String arg6, Bundle arg7, Bundle arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;ILjava/lang/String;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, BroadcastReceiver arg2, Handler arg3, int arg4, String arg5, Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, Bundle arg2, BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, String arg2, BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2, BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendStickyBroadcast(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyBroadcast(Landroid/content/Intent;)V");
    }

    public void sendStickyBroadcast(Intent arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyBroadcast(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public void sendStickyBroadcastAsUser(Intent arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void sendStickyOrderedBroadcast(Intent arg0, BroadcastReceiver arg1, Handler arg2, int arg3, String arg4, Bundle arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyOrderedBroadcast(Landroid/content/Intent;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendStickyOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, BroadcastReceiver arg2, Handler arg3, int arg4, String arg5, Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyOrderedBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void setTheme(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->setTheme(I)V");
    }

    public void setWallpaper(Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->setWallpaper(Landroid/graphics/Bitmap;)V");
    }

    public void setWallpaper(InputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->setWallpaper(Ljava/io/InputStream;)V");
    }

    public void startActivities(Intent[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivities([Landroid/content/Intent;)V");
    }

    public void startActivities(Intent[] arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivities([Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public void startActivity(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivity(Landroid/content/Intent;)V");
    }

    public void startActivity(Intent arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivity(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public ComponentName startForegroundService(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startForegroundService(Landroid/content/Intent;)Landroid/content/ComponentName;");
    }

    public boolean startInstrumentation(ComponentName arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startInstrumentation(Landroid/content/ComponentName;Ljava/lang/String;Landroid/os/Bundle;)Z");
    }

    public void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startIntentSender(Landroid/content/IntentSender;Landroid/content/Intent;III)V");
    }

    public void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4, Bundle arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startIntentSender(Landroid/content/IntentSender;Landroid/content/Intent;IIILandroid/os/Bundle;)V");
    }

    public ComponentName startService(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;");
    }

    public boolean stopService(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->stopService(Landroid/content/Intent;)Z");
    }

    public void unbindService(ServiceConnection arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unbindService(Landroid/content/ServiceConnection;)V");
    }

    public void unregisterComponentCallbacks(ComponentCallbacks arg0) {
        // AOSP ContextWrapper.unregisterComponentCallbacks: delegates to
        // mBase.unregisterComponentCallbacks. Nothing was registered, so
        // nothing to remove (matches the interpreter's no-op builtin).
    }

    public void unregisterDeviceIdChangeListener(IntConsumer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unregisterDeviceIdChangeListener(Ljava/util/function/IntConsumer;)V");
    }

    public void unregisterReceiver(BroadcastReceiver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unregisterReceiver(Landroid/content/BroadcastReceiver;)V");
    }

    public void updateServiceGroup(ServiceConnection arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->updateServiceGroup(Landroid/content/ServiceConnection;II)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ContextWrapper() {
        }
    }
