package android.content;

    public class ContentResolver {

    public static final java.lang.String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";
    public static final java.lang.String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";
    public static final java.lang.String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";
    public static final java.lang.String EXTRA_HONORED_ARGS = "android.content.extra.HONORED_ARGS";
    public static final java.lang.String EXTRA_REFRESH_SUPPORTED = "android.content.extra.REFRESH_SUPPORTED";
    public static final java.lang.String EXTRA_SIZE = "android.content.extra.SIZE";
    public static final java.lang.String EXTRA_TOTAL_COUNT = "android.content.extra.TOTAL_COUNT";
    public static final int NOTIFY_DELETE = 16;
    public static final int NOTIFY_INSERT = 4;
    public static final int NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS = 2;
    public static final int NOTIFY_SYNC_TO_NETWORK = 1;
    public static final int NOTIFY_UPDATE = 8;
    public static final java.lang.String QUERY_ARG_GROUP_COLUMNS = "android:query-arg-group-columns";
    public static final java.lang.String QUERY_ARG_LIMIT = "android:query-arg-limit";
    public static final java.lang.String QUERY_ARG_OFFSET = "android:query-arg-offset";
    public static final java.lang.String QUERY_ARG_SORT_COLLATION = "android:query-arg-sort-collation";
    public static final java.lang.String QUERY_ARG_SORT_COLUMNS = "android:query-arg-sort-columns";
    public static final java.lang.String QUERY_ARG_SORT_DIRECTION = "android:query-arg-sort-direction";
    public static final java.lang.String QUERY_ARG_SORT_LOCALE = "android:query-arg-sort-locale";
    public static final java.lang.String QUERY_ARG_SQL_GROUP_BY = "android:query-arg-sql-group-by";
    public static final java.lang.String QUERY_ARG_SQL_HAVING = "android:query-arg-sql-having";
    public static final java.lang.String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";
    public static final java.lang.String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
    public static final java.lang.String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
    public static final java.lang.String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
    public static final int QUERY_SORT_DIRECTION_ASCENDING = 0;
    public static final int QUERY_SORT_DIRECTION_DESCENDING = 1;
    public static final java.lang.String SCHEME_ANDROID_RESOURCE = "android.resource";
    public static final java.lang.String SCHEME_CONTENT = "content";
    public static final java.lang.String SCHEME_FILE = "file";
    public static final java.lang.String SYNC_EXTRAS_ACCOUNT = "account";
    public static final java.lang.String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";
    public static final java.lang.String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";
    public static final java.lang.String SYNC_EXTRAS_EXPEDITED = "expedited";
    public static final java.lang.String SYNC_EXTRAS_FORCE = "force";
    public static final java.lang.String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";
    public static final java.lang.String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";
    public static final java.lang.String SYNC_EXTRAS_INITIALIZE = "initialize";
    public static final java.lang.String SYNC_EXTRAS_MANUAL = "force";
    public static final java.lang.String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";
    public static final java.lang.String SYNC_EXTRAS_REQUIRE_CHARGING = "require_charging";
    public static final java.lang.String SYNC_EXTRAS_SCHEDULE_AS_EXPEDITED_JOB = "schedule_as_expedited_job";
    public static final java.lang.String SYNC_EXTRAS_UPLOAD = "upload";
    public static final int SYNC_OBSERVER_TYPE_ACTIVE = 4;
    public static final int SYNC_OBSERVER_TYPE_PENDING = 2;
    public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1;

    public ContentResolver(android.content.Context arg0) {
        // no-op: host reads the resolver's queries against nothing
    }

    public final android.content.ContentProviderClient acquireContentProviderClient(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireContentProviderClient(Landroid/net/Uri;)Landroid/content/ContentProviderClient;");
    }

    public final android.content.ContentProviderClient acquireContentProviderClient(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireContentProviderClient(Ljava/lang/String;)Landroid/content/ContentProviderClient;");
    }

    public final android.content.ContentProviderClient acquireUnstableContentProviderClient(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireUnstableContentProviderClient(Landroid/net/Uri;)Landroid/content/ContentProviderClient;");
    }

    public final android.content.ContentProviderClient acquireUnstableContentProviderClient(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->acquireUnstableContentProviderClient(Ljava/lang/String;)Landroid/content/ContentProviderClient;");
    }

    public static void addPeriodicSync(android.accounts.Account arg0, java.lang.String arg1, android.os.Bundle arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->addPeriodicSync(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;J)V");
    }

    public static java.lang.Object addStatusChangeListener(int arg0, android.content.SyncStatusObserver arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->addStatusChangeListener(ILandroid/content/SyncStatusObserver;)Ljava/lang/Object;");
    }

    public android.content.ContentProviderResult[] applyBatch(java.lang.String arg0, java.util.ArrayList<android.content.ContentProviderOperation> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->applyBatch(Ljava/lang/String;Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;");
    }

    public final int bulkInsert(android.net.Uri arg0, android.content.ContentValues[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)I");
    }

    public final android.os.Bundle call(android.net.Uri arg0, java.lang.String arg1, java.lang.String arg2, android.os.Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->call(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public final android.os.Bundle call(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, android.os.Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->call(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;");
    }

    public static void cancelSync(android.accounts.Account arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->cancelSync(Landroid/accounts/Account;Ljava/lang/String;)V");
    }

    public static void cancelSync(android.content.SyncRequest arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->cancelSync(Landroid/content/SyncRequest;)V");
    }

    public void cancelSync(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->cancelSync(Landroid/net/Uri;)V");
    }

    public final android.net.Uri canonicalize(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->canonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public final int delete(android.net.Uri arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->delete(Landroid/net/Uri;Landroid/os/Bundle;)I");
    }

    public final int delete(android.net.Uri arg0, java.lang.String arg1, java.lang.String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public static android.content.SyncInfo getCurrentSync() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getCurrentSync()Landroid/content/SyncInfo;");
    }

    public static java.util.List<android.content.SyncInfo> getCurrentSyncs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getCurrentSyncs()Ljava/util/List;");
    }

    public static int getIsSyncable(android.accounts.Account arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getIsSyncable(Landroid/accounts/Account;Ljava/lang/String;)I");
    }

    public static boolean getMasterSyncAutomatically() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getMasterSyncAutomatically()Z");
    }

    public java.util.List<android.content.UriPermission> getOutgoingPersistedUriPermissions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getOutgoingPersistedUriPermissions()Ljava/util/List;");
    }

    public static java.util.List<android.content.PeriodicSync> getPeriodicSyncs(android.accounts.Account arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getPeriodicSyncs(Landroid/accounts/Account;Ljava/lang/String;)Ljava/util/List;");
    }

    public java.util.List<android.content.UriPermission> getPersistedUriPermissions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getPersistedUriPermissions()Ljava/util/List;");
    }

    public java.lang.String[] getStreamTypes(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getStreamTypes(Landroid/net/Uri;Ljava/lang/String;)[Ljava/lang/String;");
    }

    public static android.content.SyncAdapterType[] getSyncAdapterTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getSyncAdapterTypes()[Landroid/content/SyncAdapterType;");
    }

    public static boolean getSyncAutomatically(android.accounts.Account arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getSyncAutomatically(Landroid/accounts/Account;Ljava/lang/String;)Z");
    }

    public final java.lang.String getType(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getType(Landroid/net/Uri;)Ljava/lang/String;");
    }

    public final android.content.ContentResolver.MimeTypeInfo getTypeInfo(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->getTypeInfo(Ljava/lang/String;)Landroid/content/ContentResolver$MimeTypeInfo;");
    }

    public final android.net.Uri insert(android.net.Uri arg0, android.content.ContentValues arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;");
    }

    public final android.net.Uri insert(android.net.Uri arg0, android.content.ContentValues arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->insert(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)Landroid/net/Uri;");
    }

    public static boolean isSyncActive(android.accounts.Account arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->isSyncActive(Landroid/accounts/Account;Ljava/lang/String;)Z");
    }

    public static boolean isSyncPending(android.accounts.Account arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->isSyncPending(Landroid/accounts/Account;Ljava/lang/String;)Z");
    }

    public android.graphics.Bitmap loadThumbnail(android.net.Uri arg0, android.util.Size arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->loadThumbnail(Landroid/net/Uri;Landroid/util/Size;Landroid/os/CancellationSignal;)Landroid/graphics/Bitmap;");
    }

    public void notifyChange(android.net.Uri arg0, android.database.ContentObserver arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Landroid/net/Uri;Landroid/database/ContentObserver;)V");
    }

    public void notifyChange(android.net.Uri arg0, android.database.ContentObserver arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Landroid/net/Uri;Landroid/database/ContentObserver;Z)V");
    }

    public void notifyChange(android.net.Uri arg0, android.database.ContentObserver arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Landroid/net/Uri;Landroid/database/ContentObserver;I)V");
    }

    public void notifyChange(java.util.Collection<android.net.Uri> arg0, android.database.ContentObserver arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->notifyChange(Ljava/util/Collection;Landroid/database/ContentObserver;I)V");
    }

    public final android.content.res.AssetFileDescriptor openAssetFile(android.net.Uri arg0, java.lang.String arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final android.content.res.AssetFileDescriptor openAssetFileDescriptor(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final android.content.res.AssetFileDescriptor openAssetFileDescriptor(android.net.Uri arg0, java.lang.String arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final android.os.ParcelFileDescriptor openFile(android.net.Uri arg0, java.lang.String arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;");
    }

    public final android.os.ParcelFileDescriptor openFileDescriptor(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openFileDescriptor(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
    }

    public final android.os.ParcelFileDescriptor openFileDescriptor(android.net.Uri arg0, java.lang.String arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;");
    }

    public final java.io.InputStream openInputStream(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openInputStream(Landroid/net/Uri;)Ljava/io/InputStream;");
    }

    public final java.io.OutputStream openOutputStream(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openOutputStream(Landroid/net/Uri;)Ljava/io/OutputStream;");
    }

    public final java.io.OutputStream openOutputStream(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openOutputStream(Landroid/net/Uri;Ljava/lang/String;)Ljava/io/OutputStream;");
    }

    public final android.content.res.AssetFileDescriptor openTypedAssetFile(android.net.Uri arg0, java.lang.String arg1, android.os.Bundle arg2, android.os.CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openTypedAssetFile(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final android.content.res.AssetFileDescriptor openTypedAssetFileDescriptor(android.net.Uri arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openTypedAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final android.content.res.AssetFileDescriptor openTypedAssetFileDescriptor(android.net.Uri arg0, java.lang.String arg1, android.os.Bundle arg2, android.os.CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->openTypedAssetFileDescriptor(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;");
    }

    public final android.database.Cursor query(android.net.Uri arg0, java.lang.String[] arg1, android.os.Bundle arg2, android.os.CancellationSignal arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public final android.database.Cursor query(android.net.Uri arg0, java.lang.String[] arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public final android.database.Cursor query(android.net.Uri arg0, java.lang.String[] arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4, android.os.CancellationSignal arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public final boolean refresh(android.net.Uri arg0, android.os.Bundle arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->refresh(Landroid/net/Uri;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Z");
    }

    public final void registerContentObserver(android.net.Uri arg0, boolean arg1, android.database.ContentObserver arg2) {
        // no-op: settings observer tracking not wired to host
    }

    public void releasePersistableUriPermission(android.net.Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->releasePersistableUriPermission(Landroid/net/Uri;I)V");
    }

    public static void removePeriodicSync(android.accounts.Account arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->removePeriodicSync(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public static void removeStatusChangeListener(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->removeStatusChangeListener(Ljava/lang/Object;)V");
    }

    public static void requestSync(android.accounts.Account arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->requestSync(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public static void requestSync(android.content.SyncRequest arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->requestSync(Landroid/content/SyncRequest;)V");
    }

    public static void setIsSyncable(android.accounts.Account arg0, java.lang.String arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->setIsSyncable(Landroid/accounts/Account;Ljava/lang/String;I)V");
    }

    public static void setMasterSyncAutomatically(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->setMasterSyncAutomatically(Z)V");
    }

    public static void setSyncAutomatically(android.accounts.Account arg0, java.lang.String arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->setSyncAutomatically(Landroid/accounts/Account;Ljava/lang/String;Z)V");
    }

    public void startSync(android.net.Uri arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->startSync(Landroid/net/Uri;Landroid/os/Bundle;)V");
    }

    public void takePersistableUriPermission(android.net.Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->takePersistableUriPermission(Landroid/net/Uri;I)V");
    }

    public final android.net.Uri uncanonicalize(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->uncanonicalize(Landroid/net/Uri;)Landroid/net/Uri;");
    }

    public final void unregisterContentObserver(android.database.ContentObserver arg0) {
        // no-op
    }

    public final int update(android.net.Uri arg0, android.content.ContentValues arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->update(Landroid/net/Uri;Landroid/content/ContentValues;Landroid/os/Bundle;)I");
    }

    public final int update(android.net.Uri arg0, android.content.ContentValues arg1, java.lang.String arg2, java.lang.String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public static void validateSyncExtrasBundle(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->validateSyncExtrasBundle(Landroid/os/Bundle;)V");
    }

    public static android.content.ContentResolver wrap(android.content.ContentProvider arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->wrap(Landroid/content/ContentProvider;)Landroid/content/ContentResolver;");
    }

    public static android.content.ContentResolver wrap(android.content.ContentProviderClient arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver;->wrap(Landroid/content/ContentProviderClient;)Landroid/content/ContentResolver;");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ContentResolver() {
        }
        public static class MimeTypeInfo {

        public java.lang.CharSequence getContentDescription() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver$MimeTypeInfo;->getContentDescription()Ljava/lang/CharSequence;");
        }

        public android.graphics.drawable.Icon getIcon() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver$MimeTypeInfo;->getIcon()Landroid/graphics/drawable/Icon;");
        }

        public java.lang.CharSequence getLabel() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentResolver$MimeTypeInfo;->getLabel()Ljava/lang/CharSequence;");
        }
        }
    }
