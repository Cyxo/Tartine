package android.database.sqlite;

import android.database.Cursor;

    public interface SQLiteCursorDriver {

    public abstract void cursorClosed();

    public abstract void cursorDeactivated();

    public abstract void cursorRequeried(Cursor arg0);

    public abstract Cursor query(SQLiteDatabase.CursorFactory arg0, String[] arg1);

    public abstract void setBindArguments(String[] arg0);
    }
