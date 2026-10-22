package android.content;

    public class ContextWrapper extends android.content.Context {

    /** AOSP ContextWrapper: the package-visible base (file line 78). */
    android.content.Context mBase;

    /** AOSP ContextWrapper ctor (lines 95-96). */
    public ContextWrapper(android.content.Context arg0) {
        mBase = arg0;
    }

    /** AOSP ContextWrapper.attachBaseContext (lines 106-111). */
    protected void attachBaseContext(android.content.Context arg0) {
        if (mBase != null) {
            throw new IllegalStateException("Base context already set");
        }
        mBase = arg0;
    }

    /** AOSP ContextWrapper.getBaseContext (line 116-118). */
    public android.content.Context getBaseContext() {
        return mBase;
    }

    public boolean bindIsolatedService(android.content.Intent arg0, int arg1, java.lang.String arg2, java.util.concurrent.Executor arg3, android.content.ServiceConnection arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindIsolatedService(Landroid/content/Intent;ILjava/lang/String;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(android.content.Intent arg0, android.content.Context.BindServiceFlags arg1, java.util.concurrent.Executor arg2, android.content.ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;Landroid/content/Context$BindServiceFlags;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(android.content.Intent arg0, android.content.ServiceConnection arg1, android.content.Context.BindServiceFlags arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;)Z");
    }

    public boolean bindService(android.content.Intent arg0, android.content.ServiceConnection arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;I)Z");
    }

    public boolean bindService(android.content.Intent arg0, int arg1, java.util.concurrent.Executor arg2, android.content.ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindService(Landroid/content/Intent;ILjava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindServiceAsUser(android.content.Intent arg0, android.content.ServiceConnection arg1, android.content.Context.BindServiceFlags arg2, android.os.UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;Landroid/os/UserHandle;)Z");
    }

    public boolean bindServiceAsUser(android.content.Intent arg0, android.content.ServiceConnection arg1, int arg2, android.os.UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;ILandroid/os/UserHandle;)Z");
    }

    public int checkCallingOrSelfPermission(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingOrSelfPermission(Ljava/lang/String;)I");
    }

    public int checkCallingOrSelfUriPermission(android.net.Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingOrSelfUriPermission(Landroid/net/Uri;I)I");
    }

    public int[] checkCallingOrSelfUriPermissions(java.util.List<android.net.Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingOrSelfUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkCallingPermission(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingPermission(Ljava/lang/String;)I");
    }

    public int checkCallingUriPermission(android.net.Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingUriPermission(Landroid/net/Uri;I)I");
    }

    public int[] checkCallingUriPermissions(java.util.List<android.net.Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkCallingUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkContentUriPermissionFull(android.net.Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkContentUriPermissionFull(Landroid/net/Uri;III)I");
    }

    public int checkPermission(java.lang.String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkPermission(Ljava/lang/String;II)I");
    }

    public int checkSelfPermission(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkSelfPermission(Ljava/lang/String;)I");
    }

    public int checkUriPermission(android.net.Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkUriPermission(Landroid/net/Uri;III)I");
    }

    public int checkUriPermission(android.net.Uri arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkUriPermission(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;III)I");
    }

    public int[] checkUriPermissions(java.util.List<android.net.Uri> arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->checkUriPermissions(Ljava/util/List;III)[I");
    }

    public void clearWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->clearWallpaper()V");
    }

    public android.content.Context createAttributionContext(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createAttributionContext(Ljava/lang/String;)Landroid/content/Context;");
    }

    public android.content.Context createConfigurationContext(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createConfigurationContext(Landroid/content/res/Configuration;)Landroid/content/Context;");
    }

    public android.content.Context createContext(android.content.ContextParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createContext(Landroid/content/ContextParams;)Landroid/content/Context;");
    }

    public android.content.Context createContextForSplit(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createContextForSplit(Ljava/lang/String;)Landroid/content/Context;");
    }

    public android.content.Context createDeviceContext(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createDeviceContext(I)Landroid/content/Context;");
    }

    public android.content.Context createDeviceProtectedStorageContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createDeviceProtectedStorageContext()Landroid/content/Context;");
    }

    public android.content.Context createDisplayContext(android.view.Display arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createDisplayContext(Landroid/view/Display;)Landroid/content/Context;");
    }

    public android.content.Context createPackageContext(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createPackageContext(Ljava/lang/String;I)Landroid/content/Context;");
    }

    public android.content.Context createWindowContext(android.view.Display arg0, int arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createWindowContext(Landroid/view/Display;ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public android.content.Context createWindowContext(int arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->createWindowContext(ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public java.lang.String[] databaseList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->databaseList()[Ljava/lang/String;");
    }

    public boolean deleteDatabase(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->deleteDatabase(Ljava/lang/String;)Z");
    }

    public boolean deleteFile(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->deleteFile(Ljava/lang/String;)Z");
    }

    public boolean deleteSharedPreferences(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->deleteSharedPreferences(Ljava/lang/String;)Z");
    }

    public void enforceCallingOrSelfPermission(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingOrSelfPermission(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void enforceCallingOrSelfUriPermission(android.net.Uri arg0, int arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingOrSelfUriPermission(Landroid/net/Uri;ILjava/lang/String;)V");
    }

    public void enforceCallingPermission(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingPermission(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void enforceCallingUriPermission(android.net.Uri arg0, int arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceCallingUriPermission(Landroid/net/Uri;ILjava/lang/String;)V");
    }

    public void enforcePermission(java.lang.String arg0, int arg1, int arg2, java.lang.String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforcePermission(Ljava/lang/String;IILjava/lang/String;)V");
    }

    public void enforceUriPermission(android.net.Uri arg0, int arg1, int arg2, int arg3, java.lang.String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceUriPermission(Landroid/net/Uri;IIILjava/lang/String;)V");
    }

    public void enforceUriPermission(android.net.Uri arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, int arg5, java.lang.String arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->enforceUriPermission(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;IIILjava/lang/String;)V");
    }

    public java.lang.String[] fileList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->fileList()[Ljava/lang/String;");
    }

    public android.content.Context getApplicationContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getApplicationContext()Landroid/content/Context;");
    }

    public android.content.pm.ApplicationInfo getApplicationInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;");
    }

    public android.content.res.AssetManager getAssets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getAssets()Landroid/content/res/AssetManager;");
    }

    public android.content.AttributionSource getAttributionSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getAttributionSource()Landroid/content/AttributionSource;");
    }

    public java.lang.String getAttributionTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getAttributionTag()Ljava/lang/String;");
    }

    public java.io.File getCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getCacheDir()Ljava/io/File;");
    }

    public java.lang.ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getClassLoader()Ljava/lang/ClassLoader;");
    }

    public java.io.File getCodeCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getCodeCacheDir()Ljava/io/File;");
    }

    public android.content.ContentResolver getContentResolver() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getContentResolver()Landroid/content/ContentResolver;");
    }

    public java.io.File getDataDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDataDir()Ljava/io/File;");
    }

    public java.io.File getDatabasePath(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDatabasePath(Ljava/lang/String;)Ljava/io/File;");
    }

    public int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDeviceId()I");
    }

    public java.io.File getDir(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDir(Ljava/lang/String;I)Ljava/io/File;");
    }

    public android.view.Display getDisplay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getDisplay()Landroid/view/Display;");
    }

    public java.io.File getExternalCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalCacheDir()Ljava/io/File;");
    }

    public java.io.File[] getExternalCacheDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalCacheDirs()[Ljava/io/File;");
    }

    public java.io.File getExternalFilesDir(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalFilesDir(Ljava/lang/String;)Ljava/io/File;");
    }

    public java.io.File[] getExternalFilesDirs(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalFilesDirs(Ljava/lang/String;)[Ljava/io/File;");
    }

    public java.io.File[] getExternalMediaDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getExternalMediaDirs()[Ljava/io/File;");
    }

    public java.io.File getFileStreamPath(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getFileStreamPath(Ljava/lang/String;)Ljava/io/File;");
    }

    public java.io.File getFilesDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getFilesDir()Ljava/io/File;");
    }

    public java.util.concurrent.Executor getMainExecutor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getMainExecutor()Ljava/util/concurrent/Executor;");
    }

    public android.os.Looper getMainLooper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getMainLooper()Landroid/os/Looper;");
    }

    public java.io.File getNoBackupFilesDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getNoBackupFilesDir()Ljava/io/File;");
    }

    public java.io.File getObbDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getObbDir()Ljava/io/File;");
    }

    public java.io.File[] getObbDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getObbDirs()[Ljava/io/File;");
    }

    public java.lang.String getOpPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getOpPackageName()Ljava/lang/String;");
    }

    public java.lang.String getPackageCodePath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageCodePath()Ljava/lang/String;");
    }

    public android.content.pm.PackageManager getPackageManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageManager()Landroid/content/pm/PackageManager;");
    }

    public java.lang.String getPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageName()Ljava/lang/String;");
    }

    public java.lang.String getPackageResourcePath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getPackageResourcePath()Ljava/lang/String;");
    }

    public android.content.ContextParams getParams() {
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
    public android.content.res.Resources getResources() {
        if (mBase != null) {
            return mBase.getResources();
        }
        return super.getResources();
    }

    public android.content.SharedPreferences getSharedPreferences(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;");
    }

    public java.lang.Object getSystemService(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;");
    }

    public java.lang.String getSystemServiceName(java.lang.Class<?> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getSystemServiceName(Ljava/lang/Class;)Ljava/lang/String;");
    }

    public android.content.res.Resources.Theme getTheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getTheme()Landroid/content/res/Resources$Theme;");
    }

    public android.graphics.drawable.Drawable getWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getWallpaper()Landroid/graphics/drawable/Drawable;");
    }

    public int getWallpaperDesiredMinimumHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getWallpaperDesiredMinimumHeight()I");
    }

    public int getWallpaperDesiredMinimumWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->getWallpaperDesiredMinimumWidth()I");
    }

    public void grantUriPermission(java.lang.String arg0, android.net.Uri arg1, int arg2) {
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

    public boolean moveDatabaseFrom(android.content.Context arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->moveDatabaseFrom(Landroid/content/Context;Ljava/lang/String;)Z");
    }

    public boolean moveSharedPreferencesFrom(android.content.Context arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->moveSharedPreferencesFrom(Landroid/content/Context;Ljava/lang/String;)Z");
    }

    public java.io.FileInputStream openFileInput(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openFileInput(Ljava/lang/String;)Ljava/io/FileInputStream;");
    }

    public java.io.FileOutputStream openFileOutput(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openFileOutput(Ljava/lang/String;I)Ljava/io/FileOutputStream;");
    }

    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String arg0, int arg1, android.database.sqlite.SQLiteDatabase.CursorFactory arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openOrCreateDatabase(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String arg0, int arg1, android.database.sqlite.SQLiteDatabase.CursorFactory arg2, android.database.DatabaseErrorHandler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->openOrCreateDatabase(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;Landroid/database/DatabaseErrorHandler;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public android.graphics.drawable.Drawable peekWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->peekWallpaper()Landroid/graphics/drawable/Drawable;");
    }

    public void registerComponentCallbacks(android.content.ComponentCallbacks arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerComponentCallbacks(Landroid/content/ComponentCallbacks;)V");
    }

    public void registerDeviceIdChangeListener(java.util.concurrent.Executor arg0, java.util.function.IntConsumer arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerDeviceIdChangeListener(Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;");
    }

    public android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;I)Landroid/content/Intent;");
    }

    public android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1, java.lang.String arg2, android.os.Handler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;)Landroid/content/Intent;");
    }

    public android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1, java.lang.String arg2, android.os.Handler arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;I)Landroid/content/Intent;");
    }

    public void removeStickyBroadcast(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->removeStickyBroadcast(Landroid/content/Intent;)V");
    }

    public void removeStickyBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->removeStickyBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void revokeSelfPermissionsOnKill(java.util.Collection<java.lang.String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->revokeSelfPermissionsOnKill(Ljava/util/Collection;)V");
    }

    public void revokeUriPermission(android.net.Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->revokeUriPermission(Landroid/net/Uri;I)V");
    }

    public void revokeUriPermission(java.lang.String arg0, android.net.Uri arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->revokeUriPermission(Ljava/lang/String;Landroid/net/Uri;I)V");
    }

    public void sendBroadcast(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcast(Landroid/content/Intent;)V");
    }

    public void sendBroadcast(android.content.Intent arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;)V");
    }

    public void sendBroadcast(android.content.Intent arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void sendBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Ljava/lang/String;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, int arg1, java.lang.String arg2, java.lang.String arg3, android.content.BroadcastReceiver arg4, android.os.Handler arg5, java.lang.String arg6, android.os.Bundle arg7, android.os.Bundle arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;ILjava/lang/String;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, android.content.BroadcastReceiver arg2, android.os.Handler arg3, int arg4, java.lang.String arg5, android.os.Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, android.os.Bundle arg2, android.content.BroadcastReceiver arg3, android.os.Handler arg4, int arg5, java.lang.String arg6, android.os.Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, java.lang.String arg2, android.content.BroadcastReceiver arg3, android.os.Handler arg4, int arg5, java.lang.String arg6, android.os.Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1, java.lang.String arg2, android.content.BroadcastReceiver arg3, android.os.Handler arg4, int arg5, java.lang.String arg6, android.os.Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendOrderedBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendStickyBroadcast(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyBroadcast(Landroid/content/Intent;)V");
    }

    public void sendStickyBroadcast(android.content.Intent arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyBroadcast(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public void sendStickyBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void sendStickyOrderedBroadcast(android.content.Intent arg0, android.content.BroadcastReceiver arg1, android.os.Handler arg2, int arg3, java.lang.String arg4, android.os.Bundle arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyOrderedBroadcast(Landroid/content/Intent;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendStickyOrderedBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1, android.content.BroadcastReceiver arg2, android.os.Handler arg3, int arg4, java.lang.String arg5, android.os.Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->sendStickyOrderedBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void setTheme(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->setTheme(I)V");
    }

    public void setWallpaper(android.graphics.Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->setWallpaper(Landroid/graphics/Bitmap;)V");
    }

    public void setWallpaper(java.io.InputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->setWallpaper(Ljava/io/InputStream;)V");
    }

    public void startActivities(android.content.Intent[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivities([Landroid/content/Intent;)V");
    }

    public void startActivities(android.content.Intent[] arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivities([Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public void startActivity(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivity(Landroid/content/Intent;)V");
    }

    public void startActivity(android.content.Intent arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startActivity(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public android.content.ComponentName startForegroundService(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startForegroundService(Landroid/content/Intent;)Landroid/content/ComponentName;");
    }

    public boolean startInstrumentation(android.content.ComponentName arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startInstrumentation(Landroid/content/ComponentName;Ljava/lang/String;Landroid/os/Bundle;)Z");
    }

    public void startIntentSender(android.content.IntentSender arg0, android.content.Intent arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startIntentSender(Landroid/content/IntentSender;Landroid/content/Intent;III)V");
    }

    public void startIntentSender(android.content.IntentSender arg0, android.content.Intent arg1, int arg2, int arg3, int arg4, android.os.Bundle arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startIntentSender(Landroid/content/IntentSender;Landroid/content/Intent;IIILandroid/os/Bundle;)V");
    }

    public android.content.ComponentName startService(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;");
    }

    public boolean stopService(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->stopService(Landroid/content/Intent;)Z");
    }

    public void unbindService(android.content.ServiceConnection arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unbindService(Landroid/content/ServiceConnection;)V");
    }

    public void unregisterComponentCallbacks(android.content.ComponentCallbacks arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unregisterComponentCallbacks(Landroid/content/ComponentCallbacks;)V");
    }

    public void unregisterDeviceIdChangeListener(java.util.function.IntConsumer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unregisterDeviceIdChangeListener(Ljava/util/function/IntConsumer;)V");
    }

    public void unregisterReceiver(android.content.BroadcastReceiver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->unregisterReceiver(Landroid/content/BroadcastReceiver;)V");
    }

    public void updateServiceGroup(android.content.ServiceConnection arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContextWrapper;->updateServiceGroup(Landroid/content/ServiceConnection;II)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ContextWrapper() {
        }
    }
