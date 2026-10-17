package android.database.sqlite;

    public class SQLiteRawStatement implements java.io.Closeable {

    public static final int SQLITE_DATA_TYPE_BLOB = 4;
    public static final int SQLITE_DATA_TYPE_FLOAT = 2;
    public static final int SQLITE_DATA_TYPE_INTEGER = 1;
    public static final int SQLITE_DATA_TYPE_NULL = 5;
    public static final int SQLITE_DATA_TYPE_TEXT = 3;

    public void bindBlob(int arg0, byte[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindBlob(I[B)V");
    }

    public void bindBlob(int arg0, byte[] arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindBlob(I[BII)V");
    }

    public void bindDouble(int arg0, double arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindDouble(ID)V");
    }

    public void bindInt(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindInt(II)V");
    }

    public void bindLong(int arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindLong(IJ)V");
    }

    public void bindNull(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindNull(I)V");
    }

    public void bindText(int arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->bindText(ILjava/lang/String;)V");
    }

    public void clearBindings() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->clearBindings()V");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->close()V");
    }

    public byte[] getColumnBlob(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnBlob(I)[B");
    }

    public double getColumnDouble(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnDouble(I)D");
    }

    public int getColumnInt(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnInt(I)I");
    }

    public int getColumnLength(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnLength(I)I");
    }

    public long getColumnLong(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnLong(I)J");
    }

    public java.lang.String getColumnName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnName(I)Ljava/lang/String;");
    }

    public java.lang.String getColumnText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnText(I)Ljava/lang/String;");
    }

    public int getColumnType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getColumnType(I)I");
    }

    public int getParameterCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getParameterCount()I");
    }

    public int getParameterIndex(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getParameterIndex(Ljava/lang/String;)I");
    }

    public java.lang.String getParameterName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getParameterName(I)Ljava/lang/String;");
    }

    public int getResultColumnCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->getResultColumnCount()I");
    }

    public boolean isOpen() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->isOpen()Z");
    }

    public int readColumnBlob(int arg0, byte[] arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->readColumnBlob(I[BIII)I");
    }

    public void reset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->reset()V");
    }

    public boolean step() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->step()Z");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/sqlite/SQLiteRawStatement;->toString()Ljava/lang/String;");
    }
    }
