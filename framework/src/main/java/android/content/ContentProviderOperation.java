package android.content;

    public class ContentProviderOperation implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;

    public android.content.ContentProviderResult apply(android.content.ContentProvider arg0, android.content.ContentProviderResult[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->apply(Landroid/content/ContentProvider;[Landroid/content/ContentProviderResult;I)Landroid/content/ContentProviderResult;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->describeContents()I");
    }

    public android.net.Uri getUri() {
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

    public static android.content.ContentProviderOperation.Builder newAssertQuery(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newAssertQuery(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static android.content.ContentProviderOperation.Builder newCall(android.net.Uri arg0, java.lang.String arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newCall(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static android.content.ContentProviderOperation.Builder newDelete(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newDelete(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static android.content.ContentProviderOperation.Builder newInsert(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newInsert(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public static android.content.ContentProviderOperation.Builder newUpdate(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->newUpdate(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;");
    }

    public android.os.Bundle resolveExtrasBackReferences(android.content.ContentProviderResult[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->resolveExtrasBackReferences([Landroid/content/ContentProviderResult;I)Landroid/os/Bundle;");
    }

    public java.lang.String[] resolveSelectionArgsBackReferences(android.content.ContentProviderResult[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->resolveSelectionArgsBackReferences([Landroid/content/ContentProviderResult;I)[Ljava/lang/String;");
    }

    public android.content.ContentValues resolveValueBackReferences(android.content.ContentProviderResult[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->resolveValueBackReferences([Landroid/content/ContentProviderResult;I)Landroid/content/ContentValues;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class Builder {

        public android.content.ContentProviderOperation build() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->build()Landroid/content/ContentProviderOperation;");
        }

        public android.content.ContentProviderOperation.Builder withExceptionAllowed(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExceptionAllowed(Z)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withExpectedCount(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExpectedCount(I)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withExtra(java.lang.String arg0, java.lang.Object arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtra(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withExtraBackReference(java.lang.String arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtraBackReference(Ljava/lang/String;I)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withExtraBackReference(java.lang.String arg0, int arg1, java.lang.String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtraBackReference(Ljava/lang/String;ILjava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withExtras(android.os.Bundle arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withExtras(Landroid/os/Bundle;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withSelection(java.lang.String arg0, java.lang.String[] arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withSelection(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withSelectionBackReference(int arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withSelectionBackReference(II)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withSelectionBackReference(int arg0, int arg1, java.lang.String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withSelectionBackReference(IILjava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withValue(java.lang.String arg0, java.lang.Object arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValue(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withValueBackReference(java.lang.String arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValueBackReference(Ljava/lang/String;I)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withValueBackReference(java.lang.String arg0, int arg1, java.lang.String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValueBackReference(Ljava/lang/String;ILjava/lang/String;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withValueBackReferences(android.content.ContentValues arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValueBackReferences(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withValues(android.content.ContentValues arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withValues(Landroid/content/ContentValues;)Landroid/content/ContentProviderOperation$Builder;");
        }

        public android.content.ContentProviderOperation.Builder withYieldAllowed(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderOperation$Builder;->withYieldAllowed(Z)Landroid/content/ContentProviderOperation$Builder;");
        }
        }
    }
