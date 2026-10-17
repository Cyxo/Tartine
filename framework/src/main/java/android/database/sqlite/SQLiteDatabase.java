package android.database.sqlite;

    public class SQLiteDatabase extends android.database.sqlite.SQLiteClosable {

    public static final int CONFLICT_ABORT = 2;
    public static final int CONFLICT_FAIL = 3;
    public static final int CONFLICT_IGNORE = 4;
    public static final int CONFLICT_NONE = 0;
    public static final int CONFLICT_REPLACE = 5;
    public static final int CONFLICT_ROLLBACK = 1;
    public static final int CREATE_IF_NECESSARY = 268435456;
    public static final int ENABLE_WRITE_AHEAD_LOGGING = 536870912;
    public static final java.lang.String JOURNAL_MODE_DELETE = "DELETE";
    public static final java.lang.String JOURNAL_MODE_MEMORY = "MEMORY";
    public static final java.lang.String JOURNAL_MODE_OFF = "OFF";
    public static final java.lang.String JOURNAL_MODE_PERSIST = "PERSIST";
    public static final java.lang.String JOURNAL_MODE_TRUNCATE = "TRUNCATE";
    public static final java.lang.String JOURNAL_MODE_WAL = "WAL";
    public static final int MAX_SQL_CACHE_SIZE = 100;
    public static final int NO_LOCALIZED_COLLATORS = 16;
    public static final int OPEN_READONLY = 1;
    public static final int OPEN_READWRITE = 0;
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
    public static final java.lang.String SYNC_MODE_EXTRA = "EXTRA";
    public static final java.lang.String SYNC_MODE_FULL = "FULL";
    public static final java.lang.String SYNC_MODE_NORMAL = "NORMAL";
    public static final java.lang.String SYNC_MODE_OFF = "OFF";

    public void beginTransaction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->beginTransaction()V");
    }

    public void beginTransactionNonExclusive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->beginTransactionNonExclusive()V");
    }

    public void beginTransactionReadOnly() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->beginTransactionReadOnly()V");
    }

    public void beginTransactionWithListener(android.database.sqlite.SQLiteTransactionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->beginTransactionWithListener(Landroid/database/sqlite/SQLiteTransactionListener;)V");
    }

    public void beginTransactionWithListenerNonExclusive(android.database.sqlite.SQLiteTransactionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->beginTransactionWithListenerNonExclusive(Landroid/database/sqlite/SQLiteTransactionListener;)V");
    }

    public void beginTransactionWithListenerReadOnly(android.database.sqlite.SQLiteTransactionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->beginTransactionWithListenerReadOnly(Landroid/database/sqlite/SQLiteTransactionListener;)V");
    }

    public android.database.sqlite.SQLiteStatement compileStatement(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->compileStatement(Ljava/lang/String;)Landroid/database/sqlite/SQLiteStatement;");
    }

    public static android.database.sqlite.SQLiteDatabase create(android.database.sqlite.SQLiteDatabase.CursorFactory arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->create(Landroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public static android.database.sqlite.SQLiteDatabase createInMemory(android.database.sqlite.SQLiteDatabase.OpenParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->createInMemory(Landroid/database/sqlite/SQLiteDatabase$OpenParams;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public android.database.sqlite.SQLiteRawStatement createRawStatement(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->createRawStatement(Ljava/lang/String;)Landroid/database/sqlite/SQLiteRawStatement;");
    }

    public int delete(java.lang.String arg0, java.lang.String arg1, java.lang.String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->delete(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public static boolean deleteDatabase(java.io.File arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->deleteDatabase(Ljava/io/File;)Z");
    }

    public void disableWriteAheadLogging() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->disableWriteAheadLogging()V");
    }

    public boolean enableWriteAheadLogging() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->enableWriteAheadLogging()Z");
    }

    public void endTransaction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->endTransaction()V");
    }

    public void execPerConnectionSQL(java.lang.String arg0, java.lang.Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->execPerConnectionSQL(Ljava/lang/String;[Ljava/lang/Object;)V");
    }

    public void execSQL(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->execSQL(Ljava/lang/String;)V");
    }

    public void execSQL(java.lang.String arg0, java.lang.Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->execSQL(Ljava/lang/String;[Ljava/lang/Object;)V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->finalize()V");
    }

    public static java.lang.String findEditTable(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->findEditTable(Ljava/lang/String;)Ljava/lang/String;");
    }

    public java.util.List<android.util.Pair<java.lang.String, java.lang.String>> getAttachedDbs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getAttachedDbs()Ljava/util/List;");
    }

    public long getLastChangedRowCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getLastChangedRowCount()J");
    }

    public long getLastInsertRowId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getLastInsertRowId()J");
    }

    public long getMaximumSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getMaximumSize()J");
    }

    public long getPageSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getPageSize()J");
    }

    public java.lang.String getPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getPath()Ljava/lang/String;");
    }

    public java.util.Map<java.lang.String, java.lang.String> getSyncedTables() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getSyncedTables()Ljava/util/Map;");
    }

    public long getTotalChangedRowCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getTotalChangedRowCount()J");
    }

    public int getVersion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->getVersion()I");
    }

    public boolean inTransaction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->inTransaction()Z");
    }

    public long insert(java.lang.String arg0, java.lang.String arg1, android.content.ContentValues arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->insert(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J");
    }

    public long insertOrThrow(java.lang.String arg0, java.lang.String arg1, android.content.ContentValues arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->insertOrThrow(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J");
    }

    public long insertWithOnConflict(java.lang.String arg0, java.lang.String arg1, android.content.ContentValues arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->insertWithOnConflict(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;I)J");
    }

    public boolean isDatabaseIntegrityOk() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->isDatabaseIntegrityOk()Z");
    }

    public boolean isDbLockedByCurrentThread() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->isDbLockedByCurrentThread()Z");
    }

    public boolean isDbLockedByOtherThreads() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->isDbLockedByOtherThreads()Z");
    }

    public boolean isOpen() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->isOpen()Z");
    }

    public boolean isReadOnly() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->isReadOnly()Z");
    }

    public boolean isWriteAheadLoggingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->isWriteAheadLoggingEnabled()Z");
    }

    public void markTableSyncable(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->markTableSyncable(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void markTableSyncable(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->markTableSyncable(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    }

    public boolean needUpgrade(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->needUpgrade(I)Z");
    }

    protected void onAllReferencesReleased() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->onAllReferencesReleased()V");
    }

    public static android.database.sqlite.SQLiteDatabase openDatabase(java.io.File arg0, android.database.sqlite.SQLiteDatabase.OpenParams arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->openDatabase(Ljava/io/File;Landroid/database/sqlite/SQLiteDatabase$OpenParams;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public static android.database.sqlite.SQLiteDatabase openDatabase(java.lang.String arg0, android.database.sqlite.SQLiteDatabase.CursorFactory arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->openDatabase(Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;I)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public static android.database.sqlite.SQLiteDatabase openDatabase(java.lang.String arg0, android.database.sqlite.SQLiteDatabase.CursorFactory arg1, int arg2, android.database.DatabaseErrorHandler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->openDatabase(Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;ILandroid/database/DatabaseErrorHandler;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public static android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.io.File arg0, android.database.sqlite.SQLiteDatabase.CursorFactory arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->openOrCreateDatabase(Ljava/io/File;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public static android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String arg0, android.database.sqlite.SQLiteDatabase.CursorFactory arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->openOrCreateDatabase(Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public static android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String arg0, android.database.sqlite.SQLiteDatabase.CursorFactory arg1, android.database.DatabaseErrorHandler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->openOrCreateDatabase(Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;Landroid/database/DatabaseErrorHandler;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public android.database.Cursor query(boolean arg0, java.lang.String arg1, java.lang.String[] arg2, java.lang.String arg3, java.lang.String[] arg4, java.lang.String arg5, java.lang.String arg6, java.lang.String arg7, java.lang.String arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->query(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public android.database.Cursor query(boolean arg0, java.lang.String arg1, java.lang.String[] arg2, java.lang.String arg3, java.lang.String[] arg4, java.lang.String arg5, java.lang.String arg6, java.lang.String arg7, java.lang.String arg8, android.os.CancellationSignal arg9) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->query(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public android.database.Cursor query(java.lang.String arg0, java.lang.String[] arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4, java.lang.String arg5, java.lang.String arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->query(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public android.database.Cursor query(java.lang.String arg0, java.lang.String[] arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4, java.lang.String arg5, java.lang.String arg6, java.lang.String arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->query(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public android.database.Cursor queryWithFactory(android.database.sqlite.SQLiteDatabase.CursorFactory arg0, boolean arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4, java.lang.String[] arg5, java.lang.String arg6, java.lang.String arg7, java.lang.String arg8, java.lang.String arg9) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->queryWithFactory(Landroid/database/sqlite/SQLiteDatabase$CursorFactory;ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public android.database.Cursor queryWithFactory(android.database.sqlite.SQLiteDatabase.CursorFactory arg0, boolean arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4, java.lang.String[] arg5, java.lang.String arg6, java.lang.String arg7, java.lang.String arg8, java.lang.String arg9, android.os.CancellationSignal arg10) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->queryWithFactory(Landroid/database/sqlite/SQLiteDatabase$CursorFactory;ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public android.database.Cursor rawQuery(java.lang.String arg0, java.lang.String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->rawQuery(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public android.database.Cursor rawQuery(java.lang.String arg0, java.lang.String[] arg1, android.os.CancellationSignal arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->rawQuery(Ljava/lang/String;[Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public android.database.Cursor rawQueryWithFactory(android.database.sqlite.SQLiteDatabase.CursorFactory arg0, java.lang.String arg1, java.lang.String[] arg2, java.lang.String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->rawQueryWithFactory(Landroid/database/sqlite/SQLiteDatabase$CursorFactory;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
    }

    public android.database.Cursor rawQueryWithFactory(android.database.sqlite.SQLiteDatabase.CursorFactory arg0, java.lang.String arg1, java.lang.String[] arg2, java.lang.String arg3, android.os.CancellationSignal arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->rawQueryWithFactory(Landroid/database/sqlite/SQLiteDatabase$CursorFactory;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;");
    }

    public static int releaseMemory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->releaseMemory()I");
    }

    public long replace(java.lang.String arg0, java.lang.String arg1, android.content.ContentValues arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->replace(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J");
    }

    public long replaceOrThrow(java.lang.String arg0, java.lang.String arg1, android.content.ContentValues arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->replaceOrThrow(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J");
    }

    public void setCustomAggregateFunction(java.lang.String arg0, java.util.function.BinaryOperator<java.lang.String> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setCustomAggregateFunction(Ljava/lang/String;Ljava/util/function/BinaryOperator;)V");
    }

    public void setCustomScalarFunction(java.lang.String arg0, java.util.function.UnaryOperator<java.lang.String> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setCustomScalarFunction(Ljava/lang/String;Ljava/util/function/UnaryOperator;)V");
    }

    public void setForeignKeyConstraintsEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setForeignKeyConstraintsEnabled(Z)V");
    }

    public void setLocale(java.util.Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setLocale(Ljava/util/Locale;)V");
    }

    public void setLockingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setLockingEnabled(Z)V");
    }

    public void setMaxSqlCacheSize(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setMaxSqlCacheSize(I)V");
    }

    public long setMaximumSize(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setMaximumSize(J)J");
    }

    public void setPageSize(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setPageSize(J)V");
    }

    public void setTransactionSuccessful() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setTransactionSuccessful()V");
    }

    public void setVersion(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->setVersion(I)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->toString()Ljava/lang/String;");
    }

    public int update(java.lang.String arg0, android.content.ContentValues arg1, java.lang.String arg2, java.lang.String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->update(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
    }

    public int updateWithOnConflict(java.lang.String arg0, android.content.ContentValues arg1, java.lang.String arg2, java.lang.String[] arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->updateWithOnConflict(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;I)I");
    }

    public void validateSql(java.lang.String arg0, android.os.CancellationSignal arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->validateSql(Ljava/lang/String;Landroid/os/CancellationSignal;)V");
    }

    public boolean yieldIfContended() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->yieldIfContended()Z");
    }

    public boolean yieldIfContendedSafely() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->yieldIfContendedSafely()Z");
    }

    public boolean yieldIfContendedSafely(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase;->yieldIfContendedSafely(J)Z");
    }
        public static interface CursorFactory {

        public abstract android.database.Cursor newCursor(android.database.sqlite.SQLiteDatabase arg0, android.database.sqlite.SQLiteCursorDriver arg1, java.lang.String arg2, android.database.sqlite.SQLiteQuery arg3);
        }
        public static class OpenParams {

        public android.database.sqlite.SQLiteDatabase.CursorFactory getCursorFactory() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getCursorFactory()Landroid/database/sqlite/SQLiteDatabase$CursorFactory;");
        }

        public android.database.DatabaseErrorHandler getErrorHandler() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getErrorHandler()Landroid/database/DatabaseErrorHandler;");
        }

        public long getIdleConnectionTimeout() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getIdleConnectionTimeout()J");
        }

        public java.lang.String getJournalMode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getJournalMode()Ljava/lang/String;");
        }

        public int getLookasideSlotCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getLookasideSlotCount()I");
        }

        public int getLookasideSlotSize() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getLookasideSlotSize()I");
        }

        public int getOpenFlags() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getOpenFlags()I");
        }

        public java.lang.String getSynchronousMode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteDatabase$OpenParams;->getSynchronousMode()Ljava/lang/String;");
        }
        }
    }
