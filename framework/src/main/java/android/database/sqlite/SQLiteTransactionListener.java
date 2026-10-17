package android.database.sqlite;

    public interface SQLiteTransactionListener {

    public abstract void onBegin();

    public abstract void onCommit();

    public abstract void onRollback();
    }
