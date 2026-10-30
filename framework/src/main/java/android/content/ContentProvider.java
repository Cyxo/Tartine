package android.content;

import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

    public class ContentProvider implements ComponentCallbacks2 {

    public ContentProvider() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;-><init>()V");
    }

    public ContentProviderResult[] applyBatch(String arg0, ArrayList<ContentProviderOperation> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->applyBatch(Ljava/lang/String;Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;");
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->applyBatch(Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;");
    }

    public void attachInfo(Context arg0, ProviderInfo arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->attachInfo(Landroid/content/Context;Landroid/content/pm/ProviderInfo;)V");
    }

    public int bulkInsert(Uri arg0, ContentValues[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)I");
    }

    public Bundle call(String arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->call(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public Bundle call(String arg0, String arg1, String arg2, Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->call(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public Uri canonicalize(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->canonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public final ContentProvider.CallingIdentity clearCallingIdentity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->clearCallingIdentity()Landroid/content/ContentProvider$CallingIdentity;");
    }

    public int delete(Uri arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->delete(Landroid/net/Uri;Landroid/os/Bundle;)I");
    }

    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public void dump(FileDescriptor arg0, PrintWriter arg1, String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->dump(Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V");
    }

    public final AttributionSource getCallingAttributionSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getCallingAttributionSource()Landroid/content/AttributionSource;");
    }

    public final String getCallingAttributionTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getCallingAttributionTag()Ljava/lang/String;");
    }

    public final String getCallingPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getCallingPackage()Ljava/lang/String;");
    }

    public final String getCallingPackageUnchecked() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getCallingPackageUnchecked()Ljava/lang/String;");
    }

    public final Context getContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getContext()Landroid/content/Context;");
    }

    public final PathPermission[] getPathPermissions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getPathPermissions()[Landroid/content/pm/PathPermission;");
    }

    public final String getReadPermission() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getReadPermission()Ljava/lang/String;");
    }

    public String[] getStreamTypes(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getStreamTypes(Landroid/net/Uri;Ljava/lang/String;)[Ljava/lang/String;");
    }

    public String getType(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getType(Landroid/net/Uri;)Ljava/lang/String;");
    }

    public String getTypeAnonymous(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getTypeAnonymous(Landroid/net/Uri;)Ljava/lang/String;");
    }

    public final String getWritePermission() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->getWritePermission()Ljava/lang/String;");
    }

    public Uri insert(Uri arg0, ContentValues arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;");
    }

    public Uri insert(Uri arg0, ContentValues arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->insert(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)Landroid/net/Uri;");
    }

    protected boolean isTemporary() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->isTemporary()Z");
    }

    public void onCallingPackageChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->onCallingPackageChanged()V");
    }

    public void onConfigurationChanged(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->onConfigurationChanged(Landroid/content/res/Configuration;)V");
    }

    public boolean onCreate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->onCreate()Z");
    }

    public void onLowMemory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->onLowMemory()V");
    }

    public void onTrimMemory(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->onTrimMemory(I)V");
    }

    public AssetFileDescriptor openAssetFile(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openAssetFile(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public AssetFileDescriptor openAssetFile(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public ParcelFileDescriptor openFile(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openFile(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
    }

    public ParcelFileDescriptor openFile(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;");
    }

    protected final ParcelFileDescriptor openFileHelper(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openFileHelper(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
    }

    public <T> ParcelFileDescriptor openPipeHelper(Uri arg0, String arg1, Bundle arg2, T arg3, ContentProvider.PipeDataWriter<T> arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openPipeHelper(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Ljava/lang/Object;Landroid/content/ContentProvider$PipeDataWriter;)Landroid/os/ParcelFileDescriptor;");
    }

    public AssetFileDescriptor openTypedAssetFile(Uri arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openTypedAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;");
    }

    public AssetFileDescriptor openTypedAssetFile(Uri arg0, String arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->openTypedAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public Cursor query(Uri arg0, String[] arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->query(Landroid/net/Uri;[Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4, CancellationSignal arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public boolean refresh(Uri arg0, Bundle arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->refresh(Landroid/net/Uri;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Z");
    }

    public final Context requireContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->requireContext()Landroid/content/Context;");
    }

    public final void restoreCallingIdentity(ContentProvider.CallingIdentity arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->restoreCallingIdentity(Landroid/content/ContentProvider$CallingIdentity;)V");
    }

    protected final void setPathPermissions(PathPermission[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->setPathPermissions([Landroid/content/pm/PathPermission;)V");
    }

    protected final void setReadPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->setReadPermission(Ljava/lang/String;)V");
    }

    protected final void setWritePermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->setWritePermission(Ljava/lang/String;)V");
    }

    public void shutdown() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->shutdown()V");
    }

    public Uri uncanonicalize(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->uncanonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public int update(Uri arg0, ContentValues arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->update(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)I");
    }

    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProvider;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
    }
        public static class CallingIdentity {

            /** Scratch mirror constructor (SkeletonGen). */
            public CallingIdentity() {
            }
        }
        public static interface PipeDataWriter<T> {

        public abstract void writeDataToPipe(ParcelFileDescriptor arg0, Uri arg1, String arg2, Bundle arg3, T arg4);
        }
    }
