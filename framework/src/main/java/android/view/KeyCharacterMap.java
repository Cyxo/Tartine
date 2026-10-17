package android.view;

    public class KeyCharacterMap implements android.os.Parcelable {

    public static final int ALPHA = 3;
    public static final int BUILT_IN_KEYBOARD = 0;
    public static final int COMBINING_ACCENT = -2147483648;
    public static final int COMBINING_ACCENT_MASK = 2147483647;
    public static android.os.Parcelable.Creator CREATOR;
    public static final int FULL = 4;
    public static final char HEX_INPUT = 61184;
    public static final int MODIFIER_BEHAVIOR_CHORDED = 0;
    public static final int MODIFIER_BEHAVIOR_CHORDED_OR_TOGGLED = 1;
    public static final int NUMERIC = 1;
    public static final char PICKER_DIALOG_INPUT = 61185;
    public static final int PREDICTIVE = 2;
    public static final int SPECIAL_FUNCTION = 5;
    public static final int VIRTUAL_KEYBOARD = -1;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->describeContents()I");
    }

    public static boolean deviceHasKey(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->deviceHasKey(I)Z");
    }

    public static boolean[] deviceHasKeys(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->deviceHasKeys([I)[Z");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->equals(Ljava/lang/Object;)Z");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->finalize()V");
    }

    public int get(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->get(II)I");
    }

    public static int getDeadChar(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getDeadChar(II)I");
    }

    public char getDisplayLabel(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getDisplayLabel(I)C");
    }

    public android.view.KeyEvent[] getEvents(char[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getEvents([C)[Landroid/view/KeyEvent;");
    }

    public boolean getKeyData(int arg0, android.view.KeyCharacterMap.KeyData arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getKeyData(ILandroid/view/KeyCharacterMap$KeyData;)Z");
    }

    public int getKeyboardType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getKeyboardType()I");
    }

    public char getMatch(int arg0, char[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getMatch(I[C)C");
    }

    public char getMatch(int arg0, char[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getMatch(I[CI)C");
    }

    public int getModifierBehavior() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getModifierBehavior()I");
    }

    public char getNumber(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->getNumber(I)C");
    }

    public boolean isPrintingKey(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->isPrintingKey(I)Z");
    }

    public static android.view.KeyCharacterMap load(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->load(I)Landroid/view/KeyCharacterMap;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class KeyData {

        public static final int META_LENGTH = 4;
        public char displayLabel;
        public char[] meta;
        public char number;

        public KeyData() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyCharacterMap$KeyData;-><init>()V");
        }
        }
    }
