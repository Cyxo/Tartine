package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import java.io.Closeable;
import java.util.List;

    public interface Cursor extends Closeable {

    public static final int FIELD_TYPE_BLOB = 4;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_INTEGER = 1;
    public static final int FIELD_TYPE_NULL = 0;
    public static final int FIELD_TYPE_STRING = 3;

    public abstract void close();

    public abstract void copyStringToBuffer(int arg0, CharArrayBuffer arg1);

    public abstract void deactivate();

    public abstract byte[] getBlob(int arg0);

    public abstract int getColumnCount();

    public abstract int getColumnIndex(String arg0);

    public abstract int getColumnIndexOrThrow(String arg0);

    public abstract String getColumnName(int arg0);

    public abstract String[] getColumnNames();

    public abstract int getCount();

    public abstract double getDouble(int arg0);

    public abstract Bundle getExtras();

    public abstract float getFloat(int arg0);

    public abstract int getInt(int arg0);

    public abstract long getLong(int arg0);

    public abstract Uri getNotificationUri();

    public default List<Uri> getNotificationUris() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/Cursor;->getNotificationUris()Ljava/util/List;");
    }

    public abstract int getPosition();

    public abstract short getShort(int arg0);

    public abstract String getString(int arg0);

    public abstract int getType(int arg0);

    public abstract boolean getWantsAllOnMoveCalls();

    public abstract boolean isAfterLast();

    public abstract boolean isBeforeFirst();

    public abstract boolean isClosed();

    public abstract boolean isFirst();

    public abstract boolean isLast();

    public abstract boolean isNull(int arg0);

    public abstract boolean move(int arg0);

    public abstract boolean moveToFirst();

    public abstract boolean moveToLast();

    public abstract boolean moveToNext();

    public abstract boolean moveToPosition(int arg0);

    public abstract boolean moveToPrevious();

    public abstract void registerContentObserver(ContentObserver arg0);

    public abstract void registerDataSetObserver(DataSetObserver arg0);

    public abstract boolean requery();

    public abstract Bundle respond(Bundle arg0);

    public abstract void setExtras(Bundle arg0);

    public abstract void setNotificationUri(ContentResolver arg0, Uri arg1);

    public default void setNotificationUris(ContentResolver arg0, List<Uri> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/Cursor;->setNotificationUris(Landroid/content/ContentResolver;Ljava/util/List;)V");
    }

    public abstract void unregisterContentObserver(ContentObserver arg0);

    public abstract void unregisterDataSetObserver(DataSetObserver arg0);
    }
