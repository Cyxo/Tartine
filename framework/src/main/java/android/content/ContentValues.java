package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Map;
import java.util.Set;

    public class ContentValues implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String TAG = "ContentValues";

    public ContentValues() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;-><init>()V");
    }

    public ContentValues(ContentValues arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;-><init>(Landroid/content/ContentValues;)V");
    }

    public ContentValues(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;-><init>(I)V");
    }

    public void clear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->clear()V");
    }

    public boolean containsKey(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->containsKey(Ljava/lang/String;)Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->equals(Ljava/lang/Object;)Z");
    }

    public Object get(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->get(Ljava/lang/String;)Ljava/lang/Object;");
    }

    public Boolean getAsBoolean(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsBoolean(Ljava/lang/String;)Ljava/lang/Boolean;");
    }

    public Byte getAsByte(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsByte(Ljava/lang/String;)Ljava/lang/Byte;");
    }

    public byte[] getAsByteArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsByteArray(Ljava/lang/String;)[B");
    }

    public Double getAsDouble(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsDouble(Ljava/lang/String;)Ljava/lang/Double;");
    }

    public Float getAsFloat(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsFloat(Ljava/lang/String;)Ljava/lang/Float;");
    }

    public Integer getAsInteger(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsInteger(Ljava/lang/String;)Ljava/lang/Integer;");
    }

    public Long getAsLong(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsLong(Ljava/lang/String;)Ljava/lang/Long;");
    }

    public Short getAsShort(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsShort(Ljava/lang/String;)Ljava/lang/Short;");
    }

    public String getAsString(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->getAsString(Ljava/lang/String;)Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->hashCode()I");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->isEmpty()Z");
    }

    public Set<String> keySet() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->keySet()Ljava/util/Set;");
    }

    public void put(String arg0, byte[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;[B)V");
    }

    public void put(String arg0, Boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Boolean;)V");
    }

    public void put(String arg0, Byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Byte;)V");
    }

    public void put(String arg0, Double arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Double;)V");
    }

    public void put(String arg0, Float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Float;)V");
    }

    public void put(String arg0, Integer arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Integer;)V");
    }

    public void put(String arg0, Long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Long;)V");
    }

    public void put(String arg0, Short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/Short;)V");
    }

    public void put(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->put(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void putAll(ContentValues arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->putAll(Landroid/content/ContentValues;)V");
    }

    public void putNull(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->putNull(Ljava/lang/String;)V");
    }

    public void remove(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->remove(Ljava/lang/String;)V");
    }

    public int size() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->size()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->toString()Ljava/lang/String;");
    }

    public Set<Map.Entry<String, Object>> valueSet() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->valueSet()Ljava/util/Set;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentValues;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
