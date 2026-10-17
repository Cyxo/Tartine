package android.database.sqlite;

    public abstract class SQLiteClosable implements java.io.Closeable {

    public SQLiteClosable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteClosable;-><init>()V");
    }

    public void acquireReference() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteClosable;->acquireReference()V");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteClosable;->close()V");
    }

    protected abstract void onAllReferencesReleased();

    protected void onAllReferencesReleasedFromContainer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteClosable;->onAllReferencesReleasedFromContainer()V");
    }

    public void releaseReference() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteClosable;->releaseReference()V");
    }

    public void releaseReferenceFromContainer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteClosable;->releaseReferenceFromContainer()V");
    }
    }
