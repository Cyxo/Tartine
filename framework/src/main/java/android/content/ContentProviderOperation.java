package android.content;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class ContentProviderOperation implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public ContentProviderResult apply(ContentProvider arg0, ContentProviderResult[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->apply(Landroid/content/ContentProvider;[Landroid/content/ContentProviderResult;I)Landroid/content/ContentProviderResult;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->describeContents()I");
    }

    public Uri getUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->getUri()Landroid/net/Uri;");
    }

    public boolean isAssertQuery() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isAssertQuery()Z");
    }

    public boolean isCall() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isCall()Z");
    }

    public boolean isDelete() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isDelete()Z");
    }

    public boolean isExceptionAllowed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isExceptionAllowed()Z");
    }

    public boolean isInsert() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isInsert()Z");
    }

    public boolean isReadOperation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isReadOperation()Z");
    }

    public boolean isUpdate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isUpdate()Z");
    }

    public boolean isWriteOperation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isWriteOperation()Z");
    }

    public boolean isYieldAllowed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->isYieldAllowed()Z");
    }

    public static ContentProviderOperation.Builder newAssertQuery(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newAssertQuery(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static ContentProviderOperation.Builder newCall(Uri arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newCall(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static ContentProviderOperation.Builder newDelete(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newDelete(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static ContentProviderOperation.Builder newInsert(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newInsert(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static ContentProviderOperation.Builder newUpdate(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newUpdate(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public Bundle resolveExtrasBackReferences(ContentProviderResult[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->resolveExtrasBackReferences([Landroid/content/ContentProviderResult;I)Landroid/os/Bundle;");
    }

    public String[] resolveSelectionArgsBackReferences(ContentProviderResult[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->resolveSelectionArgsBackReferences([Landroid/content/ContentProviderResult;I)[Ljava/lang/String;");
    }

    public ContentValues resolveValueBackReferences(ContentProviderResult[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->resolveValueBackReferences([Landroid/content/ContentProviderResult;I)Landroid/content/ContentValues;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class Builder {

        public ContentProviderOperation build() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->build()Landroid/content/ContentProviderOperation;");
        }

        public ContentProviderOperation.Builder withExceptionAllowed(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExceptionAllowed(Z)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withExpectedCount(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExpectedCount(I)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withExtra(String arg0, Object arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtra(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withExtraBackReference(String arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtraBackReference(Ljava/lang/String;I)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withExtraBackReference(String arg0, int arg1, String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtraBackReference(Ljava/lang/String;ILjava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withExtras(Bundle arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtras(Landroid/os/Bundle;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withSelection(String arg0, String[] arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withSelection(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withSelectionBackReference(int arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withSelectionBackReference(II)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withSelectionBackReference(int arg0, int arg1, String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withSelectionBackReference(IILjava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withValue(String arg0, Object arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValue(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withValueBackReference(String arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValueBackReference(Ljava/lang/String;I)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withValueBackReference(String arg0, int arg1, String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValueBackReference(Ljava/lang/String;ILjava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withValueBackReferences(ContentValues arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValueBackReferences(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withValues(ContentValues arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValues(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public ContentProviderOperation.Builder withYieldAllowed(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withYieldAllowed(Z)Landroid/content/ContentProviderOperation$Builder;");
        }
        }
    }
