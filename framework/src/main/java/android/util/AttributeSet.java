package android.util;

    public interface AttributeSet {

    public abstract boolean getAttributeBooleanValue(int arg0, boolean arg1);

    public abstract boolean getAttributeBooleanValue(String arg0, String arg1, boolean arg2);

    public abstract int getAttributeCount();

    public abstract float getAttributeFloatValue(int arg0, float arg1);

    public abstract float getAttributeFloatValue(String arg0, String arg1, float arg2);

    public abstract int getAttributeIntValue(int arg0, int arg1);

    public abstract int getAttributeIntValue(String arg0, String arg1, int arg2);

    public abstract int getAttributeListValue(int arg0, String[] arg1, int arg2);

    public abstract int getAttributeListValue(String arg0, String arg1, String[] arg2, int arg3);

    public abstract String getAttributeName(int arg0);

    public abstract int getAttributeNameResource(int arg0);

    public default String getAttributeNamespace(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/AttributeSet;->getAttributeNamespace(I)Ljava/lang/String;");
    }

    public abstract int getAttributeResourceValue(int arg0, int arg1);

    public abstract int getAttributeResourceValue(String arg0, String arg1, int arg2);

    public abstract int getAttributeUnsignedIntValue(int arg0, int arg1);

    public abstract int getAttributeUnsignedIntValue(String arg0, String arg1, int arg2);

    public abstract String getAttributeValue(int arg0);

    public abstract String getAttributeValue(String arg0, String arg1);

    public abstract String getClassAttribute();

    public abstract String getIdAttribute();

    public abstract int getIdAttributeResourceValue(int arg0);

    public abstract String getPositionDescription();

    public abstract int getStyleAttribute();
    }
