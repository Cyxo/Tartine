package android.database.sqlite;

    public interface SQLiteCursorDriver {

    public abstract void cursorClosed();

    public abstract void cursorDeactivated();

    public abstract void cursorRequeried(android.database.Cursor arg0);

    public abstract android.database.Cursor query(android.database.sqlite.SQLiteDatabase.CursorFactory arg0, java.lang.String[] arg1);

    public abstract void setBindArguments(java.lang.String[] arg0);
    }
