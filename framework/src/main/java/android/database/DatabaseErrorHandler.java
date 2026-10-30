package android.database;

import android.database.sqlite.SQLiteDatabase;

    public interface DatabaseErrorHandler {

    public abstract void onCorruption(SQLiteDatabase arg0);
    }
