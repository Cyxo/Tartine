package android.view;

    public abstract class ViewStructure {

    public static final java.lang.String EXTRA_VIRTUAL_STRUCTURE_TYPE = "android.view.extra.VIRTUAL_STRUCTURE_TYPE";
    public static final java.lang.String EXTRA_VIRTUAL_STRUCTURE_VERSION_NUMBER = "android.view.extra.VIRTUAL_STRUCTURE_VERSION_NUMBER";

    public ViewStructure() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;-><init>()V");
    }

    public abstract int addChildCount(int arg0);

    public abstract void asyncCommit();

    public abstract android.view.ViewStructure asyncNewChild(int arg0);

    public void clearCredentialManagerRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->clearCredentialManagerRequest()V");
    }

    public abstract android.view.autofill.AutofillId getAutofillId();

    public abstract int getChildCount();

    public abstract android.os.Bundle getExtras();

    public abstract java.lang.CharSequence getHint();

    public android.os.OutcomeReceiver<android.credentials.GetCredentialResponse, android.credentials.GetCredentialException> getPendingCredentialCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getPendingCredentialCallback()Landroid/os/OutcomeReceiver;");
    }

    public android.credentials.GetCredentialRequest getPendingCredentialRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->getPendingCredentialRequest()Landroid/credentials/GetCredentialRequest;");
    }

    public abstract java.lang.CharSequence getText();

    public abstract int getTextSelectionEnd();

    public abstract int getTextSelectionStart();

    public abstract boolean hasExtras();

    public abstract android.view.ViewStructure newChild(int arg0);

    public abstract android.view.ViewStructure.HtmlInfo.Builder newHtmlInfoBuilder(java.lang.String arg0);

    public abstract void setAccessibilityFocused(boolean arg0);

    public abstract void setActivated(boolean arg0);

    public abstract void setAlpha(float arg0);

    public abstract void setAutofillHints(java.lang.String[] arg0);

    public abstract void setAutofillId(android.view.autofill.AutofillId arg0);

    public abstract void setAutofillId(android.view.autofill.AutofillId arg0, int arg1);

    public abstract void setAutofillOptions(java.lang.CharSequence[] arg0);

    public abstract void setAutofillType(int arg0);

    public abstract void setAutofillValue(android.view.autofill.AutofillValue arg0);

    public abstract void setCheckable(boolean arg0);

    public abstract void setChecked(boolean arg0);

    public abstract void setChildCount(int arg0);

    public abstract void setClassName(java.lang.String arg0);

    public abstract void setClickable(boolean arg0);

    public abstract void setContentDescription(java.lang.CharSequence arg0);

    public abstract void setContextClickable(boolean arg0);

    public abstract void setDataIsSensitive(boolean arg0);

    public abstract void setDimens(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);

    public abstract void setElevation(float arg0);

    public abstract void setEnabled(boolean arg0);

    public abstract void setFocusable(boolean arg0);

    public abstract void setFocused(boolean arg0);

    public abstract void setHint(java.lang.CharSequence arg0);

    public void setHintIdEntry(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setHintIdEntry(Ljava/lang/String;)V");
    }

    public abstract void setHtmlInfo(android.view.ViewStructure.HtmlInfo arg0);

    public abstract void setId(int arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3);

    public void setImportantForAutofill(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setImportantForAutofill(I)V");
    }

    public abstract void setInputType(int arg0);

    public abstract void setLocaleList(android.os.LocaleList arg0);

    public abstract void setLongClickable(boolean arg0);

    public void setMaxTextEms(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setMaxTextEms(I)V");
    }

    public void setMaxTextLength(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setMaxTextLength(I)V");
    }

    public void setMinTextEms(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setMinTextEms(I)V");
    }

    public abstract void setOpaque(boolean arg0);

    public void setPendingCredentialRequest(android.credentials.GetCredentialRequest arg0, android.os.OutcomeReceiver<android.credentials.GetCredentialResponse, android.credentials.GetCredentialException> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setPendingCredentialRequest(Landroid/credentials/GetCredentialRequest;Landroid/os/OutcomeReceiver;)V");
    }

    public void setReceiveContentMimeTypes(java.lang.String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setReceiveContentMimeTypes([Ljava/lang/String;)V");
    }

    public abstract void setSelected(boolean arg0);

    public abstract void setText(java.lang.CharSequence arg0);

    public abstract void setText(java.lang.CharSequence arg0, int arg1, int arg2);

    public void setTextIdEntry(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure;->setTextIdEntry(Ljava/lang/String;)V");
    }

    public abstract void setTextLines(int[] arg0, int[] arg1);

    public abstract void setTextStyle(float arg0, int arg1, int arg2, int arg3);

    public abstract void setTransformation(android.graphics.Matrix arg0);

    public abstract void setVisibility(int arg0);

    public abstract void setWebDomain(java.lang.String arg0);
        public static abstract class HtmlInfo {

        public HtmlInfo() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo;-><init>()V");
        }

        public abstract java.util.List<android.util.Pair<java.lang.String, java.lang.String>> getAttributes();

        public abstract java.lang.String getTag();
            public static abstract class Builder {

            public Builder() {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewStructure$HtmlInfo$Builder;-><init>()V");
            }

            public abstract android.view.ViewStructure.HtmlInfo.Builder addAttribute(java.lang.String arg0, java.lang.String arg1);

            public abstract android.view.ViewStructure.HtmlInfo build();
            }
        }
    }
