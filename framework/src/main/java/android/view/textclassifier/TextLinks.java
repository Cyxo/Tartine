package android.view.textclassifier;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;
import java.util.Collection;
import java.util.function.Function;

    public class TextLinks implements Parcelable {

    public static final int APPLY_STRATEGY_IGNORE = 0;
    public static final int APPLY_STRATEGY_REPLACE = 1;
    public static Parcelable.Creator CREATOR;
    public static final int STATUS_DIFFERENT_TEXT = 3;
    public static final int STATUS_LINKS_APPLIED = 0;
    public static final int STATUS_NO_LINKS_APPLIED = 2;
    public static final int STATUS_NO_LINKS_FOUND = 1;
    public static final int STATUS_UNSUPPORTED_CHARACTER = 4;

    public int apply(Spannable arg0, int arg1, Function<TextLinks.TextLink, TextLinks.TextLinkSpan> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->apply(Landroid/text/Spannable;ILjava/util/function/Function;)I");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->describeContents()I");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->getExtras()Landroid/os/Bundle;");
    }

    public Collection<TextLinks.TextLink> getLinks() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->getLinks()Ljava/util/Collection;");
    }

    public CharSequence getText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->getText()Ljava/lang/CharSequence;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class TextLink implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->describeContents()I");
        }

        public float getConfidenceScore(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->getConfidenceScore(Ljava/lang/String;)F");
        }

        public int getEnd() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->getEnd()I");
        }

        public String getEntity(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->getEntity(I)Ljava/lang/String;");
        }

        public int getEntityCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->getEntityCount()I");
        }

        public Bundle getExtras() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->getExtras()Landroid/os/Bundle;");
        }

        public int getStart() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->getStart()I");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLink;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class TextLinkSpan extends ClickableSpan {

        public TextLinkSpan(TextLinks.TextLink arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLinkSpan;-><init>(Landroid/view/textclassifier/TextLinks$TextLink;)V");
        }

        public final TextLinks.TextLink getTextLink() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLinkSpan;->getTextLink()Landroid/view/textclassifier/TextLinks$TextLink;");
        }

        public void onClick(View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/textclassifier/TextLinks$TextLinkSpan;->onClick(Landroid/view/View;)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public TextLinkSpan() {
            }
        }
    }
