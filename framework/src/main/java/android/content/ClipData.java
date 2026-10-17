package android.content;

    public class ClipData implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;

    public ClipData(android.content.ClipData arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;-><init>(Landroid/content/ClipData;)V");
    }

    public ClipData(android.content.ClipDescription arg0, android.content.ClipData.Item arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;-><init>(Landroid/content/ClipDescription;Landroid/content/ClipData$Item;)V");
    }

    public ClipData(java.lang.CharSequence arg0, java.lang.String[] arg1, android.content.ClipData.Item arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;-><init>(Ljava/lang/CharSequence;[Ljava/lang/String;Landroid/content/ClipData$Item;)V");
    }

    public void addItem(android.content.ClipData.Item arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->addItem(Landroid/content/ClipData$Item;)V");
    }

    public void addItem(android.content.ContentResolver arg0, android.content.ClipData.Item arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->addItem(Landroid/content/ContentResolver;Landroid/content/ClipData$Item;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->describeContents()I");
    }

    public android.content.ClipDescription getDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->getDescription()Landroid/content/ClipDescription;");
    }

    public android.content.ClipData.Item getItemAt(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->getItemAt(I)Landroid/content/ClipData$Item;");
    }

    public int getItemCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->getItemCount()I");
    }

    public static android.content.ClipData newHtmlText(java.lang.CharSequence arg0, java.lang.CharSequence arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newHtmlText(Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/String;)Landroid/content/ClipData;");
    }

    public static android.content.ClipData newIntent(java.lang.CharSequence arg0, android.content.Intent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newIntent(Ljava/lang/CharSequence;Landroid/content/Intent;)Landroid/content/ClipData;");
    }

    public static android.content.ClipData newPlainText(java.lang.CharSequence arg0, java.lang.CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newPlainText(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Landroid/content/ClipData;");
    }

    public static android.content.ClipData newRawUri(java.lang.CharSequence arg0, android.net.Uri arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newRawUri(Ljava/lang/CharSequence;Landroid/net/Uri;)Landroid/content/ClipData;");
    }

    public static android.content.ClipData newUri(android.content.ContentResolver arg0, java.lang.CharSequence arg1, android.net.Uri arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newUri(Landroid/content/ContentResolver;Ljava/lang/CharSequence;Landroid/net/Uri;)Landroid/content/ClipData;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ClipData() {
        }
        public static class Item {

        public Item(android.content.Intent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Landroid/content/Intent;)V");
        }

        public Item(android.net.Uri arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Landroid/net/Uri;)V");
        }

        public Item(java.lang.CharSequence arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;)V");
        }

        public Item(java.lang.CharSequence arg0, android.content.Intent arg1, android.net.Uri arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;Landroid/content/Intent;Landroid/net/Uri;)V");
        }

        public Item(java.lang.CharSequence arg0, java.lang.String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;Ljava/lang/String;)V");
        }

        public Item(java.lang.CharSequence arg0, java.lang.String arg1, android.content.Intent arg2, android.net.Uri arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;Ljava/lang/String;Landroid/content/Intent;Landroid/net/Uri;)V");
        }

        public java.lang.String coerceToHtmlText(android.content.Context arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->coerceToHtmlText(Landroid/content/Context;)Ljava/lang/String;");
        }

        public java.lang.CharSequence coerceToStyledText(android.content.Context arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->coerceToStyledText(Landroid/content/Context;)Ljava/lang/CharSequence;");
        }

        public java.lang.CharSequence coerceToText(android.content.Context arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->coerceToText(Landroid/content/Context;)Ljava/lang/CharSequence;");
        }

        public java.lang.String getHtmlText() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getHtmlText()Ljava/lang/String;");
        }

        public android.content.Intent getIntent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getIntent()Landroid/content/Intent;");
        }

        public android.content.IntentSender getIntentSender() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getIntentSender()Landroid/content/IntentSender;");
        }

        public java.lang.CharSequence getText() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getText()Ljava/lang/CharSequence;");
        }

        public android.view.textclassifier.TextLinks getTextLinks() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getTextLinks()Landroid/view/textclassifier/TextLinks;");
        }

        public android.net.Uri getUri() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getUri()Landroid/net/Uri;");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Item() {
            }
        }
    }
