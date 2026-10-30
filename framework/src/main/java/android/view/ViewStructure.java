package android.view;

import android.credentials.GetCredentialException;
import android.credentials.GetCredentialRequest;
import android.credentials.GetCredentialResponse;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.OutcomeReceiver;
import android.util.Pair;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import java.util.List;

    public class ViewStructure {

    public static final String EXTRA_VIRTUAL_STRUCTURE_TYPE = "android.view.extra.VIRTUAL_STRUCTURE_TYPE";
    public static final String EXTRA_VIRTUAL_STRUCTURE_VERSION_NUMBER = "android.view.extra.VIRTUAL_STRUCTURE_VERSION_NUMBER";

    public ViewStructure() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;-><init>()V");
    }

    public int addChildCount(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->addChildCount(I)I");
    }

    public void asyncCommit() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->asyncCommit()V");
    }

    public ViewStructure asyncNewChild(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->asyncNewChild(I)Landroid/view/ViewStructure;");
    }

    public void clearCredentialManagerRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->clearCredentialManagerRequest()V");
    }

    public AutofillId getAutofillId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getAutofillId()Landroid/view/autofill/AutofillId;");
    }

    public int getChildCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getChildCount()I");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getExtras()Landroid/os/Bundle;");
    }

    public CharSequence getHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getHint()Ljava/lang/CharSequence;");
    }

    public OutcomeReceiver<GetCredentialResponse, GetCredentialException> getPendingCredentialCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getPendingCredentialCallback()Landroid/os/OutcomeReceiver;");
    }

    public GetCredentialRequest getPendingCredentialRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getPendingCredentialRequest()Landroid/credentials/GetCredentialRequest;");
    }

    public CharSequence getText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getText()Ljava/lang/CharSequence;");
    }

    public int getTextSelectionEnd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getTextSelectionEnd()I");
    }

    public int getTextSelectionStart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getTextSelectionStart()I");
    }

    public boolean hasExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->hasExtras()Z");
    }

    public ViewStructure newChild(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->newChild(I)Landroid/view/ViewStructure;");
    }

    public ViewStructure.HtmlInfo.Builder newHtmlInfoBuilder(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->newHtmlInfoBuilder(Ljava/lang/String;)Landroid/view/ViewStructure$HtmlInfo$Builder;");
    }

    public void setAccessibilityFocused(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAccessibilityFocused(Z)V");
    }

    public void setActivated(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setActivated(Z)V");
    }

    public void setAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAlpha(F)V");
    }

    public void setAutofillHints(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAutofillHints([Ljava/lang/String;)V");
    }

    public void setAutofillId(AutofillId arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAutofillId(Landroid/view/autofill/AutofillId;)V");
    }

    public void setAutofillId(AutofillId arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAutofillId(Landroid/view/autofill/AutofillId;I)V");
    }

    public void setAutofillOptions(CharSequence[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAutofillOptions([Ljava/lang/CharSequence;)V");
    }

    public void setAutofillType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAutofillType(I)V");
    }

    public void setAutofillValue(AutofillValue arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setAutofillValue(Landroid/view/autofill/AutofillValue;)V");
    }

    public void setCheckable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setCheckable(Z)V");
    }

    public void setChecked(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setChecked(Z)V");
    }

    public void setChildCount(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setChildCount(I)V");
    }

    public void setClassName(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setClassName(Ljava/lang/String;)V");
    }

    public void setClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setClickable(Z)V");
    }

    public void setContentDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setContentDescription(Ljava/lang/CharSequence;)V");
    }

    public void setContextClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setContextClickable(Z)V");
    }

    public void setDataIsSensitive(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setDataIsSensitive(Z)V");
    }

    public void setDimens(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setDimens(IIIIII)V");
    }

    public void setElevation(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setElevation(F)V");
    }

    public void setEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setEnabled(Z)V");
    }

    public void setFocusable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setFocusable(Z)V");
    }

    public void setFocused(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setFocused(Z)V");
    }

    public void setHint(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setHint(Ljava/lang/CharSequence;)V");
    }

    public void setHintIdEntry(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setHintIdEntry(Ljava/lang/String;)V");
    }

    public void setHtmlInfo(ViewStructure.HtmlInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setHtmlInfo(Landroid/view/ViewStructure$HtmlInfo;)V");
    }

    public void setId(int arg0, String arg1, String arg2, String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setId(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void setImportantForAutofill(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setImportantForAutofill(I)V");
    }

    public void setInputType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setInputType(I)V");
    }

    public void setLocaleList(LocaleList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setLocaleList(Landroid/os/LocaleList;)V");
    }

    public void setLongClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setLongClickable(Z)V");
    }

    public void setMaxTextEms(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setMaxTextEms(I)V");
    }

    public void setMaxTextLength(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setMaxTextLength(I)V");
    }

    public void setMinTextEms(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setMinTextEms(I)V");
    }

    public void setOpaque(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setOpaque(Z)V");
    }

    public void setPendingCredentialRequest(GetCredentialRequest arg0, OutcomeReceiver<GetCredentialResponse, GetCredentialException> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setPendingCredentialRequest(Landroid/credentials/GetCredentialRequest;Landroid/os/OutcomeReceiver;)V");
    }

    public void setReceiveContentMimeTypes(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setReceiveContentMimeTypes([Ljava/lang/String;)V");
    }

    public void setSelected(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setSelected(Z)V");
    }

    public void setText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setText(Ljava/lang/CharSequence;)V");
    }

    public void setText(CharSequence arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setText(Ljava/lang/CharSequence;II)V");
    }

    public void setTextIdEntry(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setTextIdEntry(Ljava/lang/String;)V");
    }

    public void setTextLines(int[] arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setTextLines([I[I)V");
    }

    public void setTextStyle(float arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setTextStyle(FIII)V");
    }

    public void setTransformation(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setTransformation(Landroid/graphics/Matrix;)V");
    }

    public void setVisibility(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setVisibility(I)V");
    }

    public void setWebDomain(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setWebDomain(Ljava/lang/String;)V");
    }
        public static class HtmlInfo {

        public HtmlInfo() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo;-><init>()V");
        }

        public List<Pair<String, String>> getAttributes() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo;->getAttributes()Ljava/util/List;");
        }

        public String getTag() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo;->getTag()Ljava/lang/String;");
        }
            public static class Builder {

            public Builder() {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo$Builder;-><init>()V");
            }

            public ViewStructure.HtmlInfo.Builder addAttribute(String arg0, String arg1) {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo$Builder;->addAttribute(Ljava/lang/String;Ljava/lang/String;)Landroid/view/ViewStructure$HtmlInfo$Builder;");
            }

            public ViewStructure.HtmlInfo build() {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo$Builder;->build()Landroid/view/ViewStructure$HtmlInfo;");
            }
            }
        }
    }
