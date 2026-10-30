package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.textclassifier.TextLinks;

    public class ClipData implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public ClipData(ClipData arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;-><init>(Landroid/content/ClipData;)V");
    }

    public ClipData(ClipDescription arg0, ClipData.Item arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;-><init>(Landroid/content/ClipDescription;Landroid/content/ClipData$Item;)V");
    }

    public ClipData(CharSequence arg0, String[] arg1, ClipData.Item arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;-><init>(Ljava/lang/CharSequence;[Ljava/lang/String;Landroid/content/ClipData$Item;)V");
    }

    public void addItem(ClipData.Item arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->addItem(Landroid/content/ClipData$Item;)V");
    }

    public void addItem(ContentResolver arg0, ClipData.Item arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->addItem(Landroid/content/ContentResolver;Landroid/content/ClipData$Item;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->describeContents()I");
    }

    public ClipDescription getDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->getDescription()Landroid/content/ClipDescription;");
    }

    public ClipData.Item getItemAt(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->getItemAt(I)Landroid/content/ClipData$Item;");
    }

    public int getItemCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->getItemCount()I");
    }

    public static ClipData newHtmlText(CharSequence arg0, CharSequence arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newHtmlText(Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/String;)Landroid/content/ClipData;");
    }

    public static ClipData newIntent(CharSequence arg0, Intent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newIntent(Ljava/lang/CharSequence;Landroid/content/Intent;)Landroid/content/ClipData;");
    }

    public static ClipData newPlainText(CharSequence arg0, CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newPlainText(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Landroid/content/ClipData;");
    }

    public static ClipData newRawUri(CharSequence arg0, Uri arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newRawUri(Ljava/lang/CharSequence;Landroid/net/Uri;)Landroid/content/ClipData;");
    }

    public static ClipData newUri(ContentResolver arg0, CharSequence arg1, Uri arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->newUri(Landroid/content/ContentResolver;Ljava/lang/CharSequence;Landroid/net/Uri;)Landroid/content/ClipData;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ClipData() {
        }
        public static class Item {

        public Item(Intent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Landroid/content/Intent;)V");
        }

        public Item(Uri arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Landroid/net/Uri;)V");
        }

        public Item(CharSequence arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;)V");
        }

        public Item(CharSequence arg0, Intent arg1, Uri arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;Landroid/content/Intent;Landroid/net/Uri;)V");
        }

        public Item(CharSequence arg0, String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;Ljava/lang/String;)V");
        }

        public Item(CharSequence arg0, String arg1, Intent arg2, Uri arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;-><init>(Ljava/lang/CharSequence;Ljava/lang/String;Landroid/content/Intent;Landroid/net/Uri;)V");
        }

        public String coerceToHtmlText(Context arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->coerceToHtmlText(Landroid/content/Context;)Ljava/lang/String;");
        }

        public CharSequence coerceToStyledText(Context arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->coerceToStyledText(Landroid/content/Context;)Ljava/lang/CharSequence;");
        }

        public CharSequence coerceToText(Context arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->coerceToText(Landroid/content/Context;)Ljava/lang/CharSequence;");
        }

        public String getHtmlText() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getHtmlText()Ljava/lang/String;");
        }

        public Intent getIntent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getIntent()Landroid/content/Intent;");
        }

        public IntentSender getIntentSender() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getIntentSender()Landroid/content/IntentSender;");
        }

        public CharSequence getText() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getText()Ljava/lang/CharSequence;");
        }

        public TextLinks getTextLinks() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getTextLinks()Landroid/view/textclassifier/TextLinks;");
        }

        public Uri getUri() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->getUri()Landroid/net/Uri;");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipData$Item;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Item() {
            }
        }
    }
