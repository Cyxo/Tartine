package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.util.ArrayList;

    public class ContentProviderClient implements AutoCloseable {

    public ContentProviderResult[] applyBatch(String arg0, ArrayList<ContentProviderOperation> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->applyBatch(Ljava/lang/String;Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;");
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->applyBatch(Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;");
    }

    public int bulkInsert(Uri arg0, ContentValues[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)I");
    }

    public Bundle call(String arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->call(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public Bundle call(String arg0, String arg1, String arg2, Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->call(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public final Uri canonicalize(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->canonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->close()V");
    }

    public int delete(Uri arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->delete(Landroid/net/Uri;Landroid/os/Bundle;)I");
    }

    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->finalize()V");
    }

    public ContentProvider getLocalContentProvider() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->getLocalContentProvider()Landroid/content/ContentProvider;");
    }

    public String[] getStreamTypes(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->getStreamTypes(Landroid/net/Uri;Ljava/lang/String;)[Ljava/lang/String;");
    }

    public String getType(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->getType(Landroid/net/Uri;)Ljava/lang/String;");
    }

    public Uri insert(Uri arg0, ContentValues arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;");
    }

    public Uri insert(Uri arg0, ContentValues arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->insert(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)Landroid/net/Uri;");
    }

    public AssetFileDescriptor openAssetFile(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openAssetFile(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public AssetFileDescriptor openAssetFile(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public ParcelFileDescriptor openFile(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openFile(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
    }

    public ParcelFileDescriptor openFile(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;");
    }

    public final AssetFileDescriptor openTypedAssetFile(Uri arg0, String arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openTypedAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openTypedAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri arg0, String arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->openTypedAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public Cursor query(Uri arg0, String[] arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->query(Landroid/net/Uri;[Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4, CancellationSignal arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public boolean refresh(Uri arg0, Bundle arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->refresh(Landroid/net/Uri;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Z");
    }

    public boolean release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->release()Z");
    }

    public final Uri uncanonicalize(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->uncanonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public int update(Uri arg0, ContentValues arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->update(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)I");
    }

    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderClient;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
    }
    }
