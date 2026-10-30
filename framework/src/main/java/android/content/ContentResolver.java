package android.content;

import android.accounts.Account;
import android.content.res.AssetFileDescriptor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Size;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

    public class ContentResolver {

    public static final String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";
    public static final String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";
    public static final String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";
    public static final String EXTRA_HONORED_ARGS = "android.content.extra.HONORED_ARGS";
    public static final String EXTRA_REFRESH_SUPPORTED = "android.content.extra.REFRESH_SUPPORTED";
    public static final String EXTRA_SIZE = "android.content.extra.SIZE";
    public static final String EXTRA_TOTAL_COUNT = "android.content.extra.TOTAL_COUNT";
    public static final int NOTIFY_DELETE = 16;
    public static final int NOTIFY_INSERT = 4;
    public static final int NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS = 2;
    public static final int NOTIFY_SYNC_TO_NETWORK = 1;
    public static final int NOTIFY_UPDATE = 8;
    public static final String QUERY_ARG_GROUP_COLUMNS = "android:query-arg-group-columns";
    public static final String QUERY_ARG_LIMIT = "android:query-arg-limit";
    public static final String QUERY_ARG_OFFSET = "android:query-arg-offset";
    public static final String QUERY_ARG_SORT_COLLATION = "android:query-arg-sort-collation";
    public static final String QUERY_ARG_SORT_COLUMNS = "android:query-arg-sort-columns";
    public static final String QUERY_ARG_SORT_DIRECTION = "android:query-arg-sort-direction";
    public static final String QUERY_ARG_SORT_LOCALE = "android:query-arg-sort-locale";
    public static final String QUERY_ARG_SQL_GROUP_BY = "android:query-arg-sql-group-by";
    public static final String QUERY_ARG_SQL_HAVING = "android:query-arg-sql-having";
    public static final String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";
    public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
    public static final String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
    public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
    public static final int QUERY_SORT_DIRECTION_ASCENDING = 0;
    public static final int QUERY_SORT_DIRECTION_DESCENDING = 1;
    public static final String SCHEME_ANDROID_RESOURCE = "android.resource";
    public static final String SCHEME_CONTENT = "content";
    public static final String SCHEME_FILE = "file";
    public static final String SYNC_EXTRAS_ACCOUNT = "account";
    public static final String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";
    public static final String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";
    public static final String SYNC_EXTRAS_EXPEDITED = "expedited";
    public static final String SYNC_EXTRAS_FORCE = "force";
    public static final String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";
    public static final String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";
    public static final String SYNC_EXTRAS_INITIALIZE = "initialize";
    public static final String SYNC_EXTRAS_MANUAL = "force";
    public static final String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";
    public static final String SYNC_EXTRAS_REQUIRE_CHARGING = "require_charging";
    public static final String SYNC_EXTRAS_SCHEDULE_AS_EXPEDITED_JOB = "schedule_as_expedited_job";
    public static final String SYNC_EXTRAS_UPLOAD = "upload";
    public static final int SYNC_OBSERVER_TYPE_ACTIVE = 4;
    public static final int SYNC_OBSERVER_TYPE_PENDING = 2;
    public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1;

    public ContentResolver(Context arg0) {
        // no-op: host reads the resolver's queries against nothing
    }

    public final ContentProviderClient acquireContentProviderClient(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireContentProviderClient(Landroid/net/Uri;)Landroid/content/ContentProviderClient;");
    }

    public final ContentProviderClient acquireContentProviderClient(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireContentProviderClient(Ljava/lang/String;)Landroid/content/ContentProviderClient;");
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireUnstableContentProviderClient(Landroid/net/Uri;)Landroid/content/ContentProviderClient;");
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireUnstableContentProviderClient(Ljava/lang/String;)Landroid/content/ContentProviderClient;");
    }

    public static void addPeriodicSync(Account arg0, String arg1, Bundle arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->addPeriodicSync(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;J)V");
    }

    public static Object addStatusChangeListener(int arg0, SyncStatusObserver arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->addStatusChangeListener(ILandroid/content/SyncStatusObserver;)Ljava/lang/Object;");
    }

    public ContentProviderResult[] applyBatch(String arg0, ArrayList<ContentProviderOperation> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->applyBatch(Ljava/lang/String;Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;");
    }

    public final int bulkInsert(Uri arg0, ContentValues[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)I");
    }

    public final Bundle call(Uri arg0, String arg1, String arg2, Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->call(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public final Bundle call(String arg0, String arg1, String arg2, Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->call(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public static void cancelSync(Account arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->cancelSync(Landroid/accounts/Account;Ljava/lang/String;)V");
    }

    public static void cancelSync(SyncRequest arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->cancelSync(Landroid/content/SyncRequest;)V");
    }

    public void cancelSync(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->cancelSync(Landroid/net/Uri;)V");
    }

    public final Uri canonicalize(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->canonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public final int delete(Uri arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->delete(Landroid/net/Uri;Landroid/os/Bundle;)I");
    }

    public final int delete(Uri arg0, String arg1, String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public static SyncInfo getCurrentSync() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getCurrentSync()Landroid/content/SyncInfo;");
    }

    public static List<SyncInfo> getCurrentSyncs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getCurrentSyncs()Ljava/util/List;");
    }

    public static int getIsSyncable(Account arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getIsSyncable(Landroid/accounts/Account;Ljava/lang/String;)I");
    }

    public static boolean getMasterSyncAutomatically() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getMasterSyncAutomatically()Z");
    }

    public List<UriPermission> getOutgoingPersistedUriPermissions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getOutgoingPersistedUriPermissions()Ljava/util/List;");
    }

    public static List<PeriodicSync> getPeriodicSyncs(Account arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getPeriodicSyncs(Landroid/accounts/Account;Ljava/lang/String;)Ljava/util/List;");
    }

    public List<UriPermission> getPersistedUriPermissions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getPersistedUriPermissions()Ljava/util/List;");
    }

    public String[] getStreamTypes(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getStreamTypes(Landroid/net/Uri;Ljava/lang/String;)[Ljava/lang/String;");
    }

    public static SyncAdapterType[] getSyncAdapterTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getSyncAdapterTypes()[Landroid/content/SyncAdapterType;");
    }

    public static boolean getSyncAutomatically(Account arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getSyncAutomatically(Landroid/accounts/Account;Ljava/lang/String;)Z");
    }

    public final String getType(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getType(Landroid/net/Uri;)Ljava/lang/String;");
    }

    public final ContentResolver.MimeTypeInfo getTypeInfo(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getTypeInfo(Ljava/lang/String;)Landroid/content/ContentResolver$MimeTypeInfo;");
    }

    public final Uri insert(Uri arg0, ContentValues arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;");
    }

    public final Uri insert(Uri arg0, ContentValues arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->insert(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)Landroid/net/Uri;");
    }

    public static boolean isSyncActive(Account arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->isSyncActive(Landroid/accounts/Account;Ljava/lang/String;)Z");
    }

    public static boolean isSyncPending(Account arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->isSyncPending(Landroid/accounts/Account;Ljava/lang/String;)Z");
    }

    public Bitmap loadThumbnail(Uri arg0, Size arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->loadThumbnail(Landroid/net/Uri;Landroid/util/Size;Landroid/os/CancellationSignal;)Landroid/graphics/Bitmap;");
    }

    public void notifyChange(Uri arg0, ContentObserver arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Landroid/net/Uri;Landroid/database/ContentObserver;)V");
    }

    public void notifyChange(Uri arg0, ContentObserver arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Landroid/net/Uri;Landroid/database/ContentObserver;Z)V");
    }

    public void notifyChange(Uri arg0, ContentObserver arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Landroid/net/Uri;Landroid/database/ContentObserver;I)V");
    }

    public void notifyChange(Collection<Uri> arg0, ContentObserver arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Ljava/util/Collection;Landroid/database/ContentObserver;I)V");
    }

    public final AssetFileDescriptor openAssetFile(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final ParcelFileDescriptor openFile(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;");
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openFileDescriptor(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri arg0, String arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;");
    }

    public final InputStream openInputStream(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openInputStream(Landroid/net/Uri;)Ljava/io/InputStream;");
    }

    public final OutputStream openOutputStream(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openOutputStream(Landroid/net/Uri;)Ljava/io/OutputStream;");
    }

    public final OutputStream openOutputStream(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openOutputStream(Landroid/net/Uri;Ljava/lang/String;)Ljava/io/OutputStream;");
    }

    public final AssetFileDescriptor openTypedAssetFile(Uri arg0, String arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openTypedAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openTypedAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri arg0, String arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openTypedAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final Cursor query(Uri arg0, String[] arg1, Bundle arg2, CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public final Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public final Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4, CancellationSignal arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public final boolean refresh(Uri arg0, Bundle arg1, CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->refresh(Landroid/net/Uri;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Z");
    }

    public final void registerContentObserver(Uri arg0, boolean arg1, ContentObserver arg2) {
        // no-op: settings observer tracking not wired to host
    }

    public void releasePersistableUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->releasePersistableUriPermission(Landroid/net/Uri;I)V");
    }

    public static void removePeriodicSync(Account arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->removePeriodicSync(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public static void removeStatusChangeListener(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->removeStatusChangeListener(Ljava/lang/Object;)V");
    }

    public static void requestSync(Account arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->requestSync(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public static void requestSync(SyncRequest arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->requestSync(Landroid/content/SyncRequest;)V");
    }

    public static void setIsSyncable(Account arg0, String arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->setIsSyncable(Landroid/accounts/Account;Ljava/lang/String;I)V");
    }

    public static void setMasterSyncAutomatically(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->setMasterSyncAutomatically(Z)V");
    }

    public static void setSyncAutomatically(Account arg0, String arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->setSyncAutomatically(Landroid/accounts/Account;Ljava/lang/String;Z)V");
    }

    public void startSync(Uri arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->startSync(Landroid/net/Uri;Landroid/os/Bundle;)V");
    }

    public void takePersistableUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->takePersistableUriPermission(Landroid/net/Uri;I)V");
    }

    public final Uri uncanonicalize(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->uncanonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public final void unregisterContentObserver(ContentObserver arg0) {
        // no-op
    }

    public final int update(Uri arg0, ContentValues arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->update(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)I");
    }

    public final int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public static void validateSyncExtrasBundle(Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->validateSyncExtrasBundle(Landroid/os/Bundle;)V");
    }

    public static ContentResolver wrap(ContentProvider arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->wrap(Landroid/content/ContentProvider;)Landroid/content/ContentResolver;");
    }

    public static ContentResolver wrap(ContentProviderClient arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->wrap(Landroid/content/ContentProviderClient;)Landroid/content/ContentResolver;");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ContentResolver() {
        }
        public static class MimeTypeInfo {

        public CharSequence getContentDescription() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver$MimeTypeInfo;->getContentDescription()Ljava/lang/CharSequence;");
        }

        public Icon getIcon() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver$MimeTypeInfo;->getIcon()Landroid/graphics/drawable/Icon;");
        }

        public CharSequence getLabel() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver$MimeTypeInfo;->getLabel()Ljava/lang/CharSequence;");
        }
        }
    }
