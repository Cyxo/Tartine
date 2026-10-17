package android.util;

    public interface AttributeSet {

    public abstract boolean getAttributeBooleanValue(int arg0, boolean arg1);

    public abstract boolean getAttributeBooleanValue(java.lang.String arg0, java.lang.String arg1, boolean arg2);

    public abstract int getAttributeCount();

    public abstract float getAttributeFloatValue(int arg0, float arg1);

    public abstract float getAttributeFloatValue(java.lang.String arg0, java.lang.String arg1, float arg2);

    public abstract int getAttributeIntValue(int arg0, int arg1);

    public abstract int getAttributeIntValue(java.lang.String arg0, java.lang.String arg1, int arg2);

    public abstract int getAttributeListValue(int arg0, java.lang.String[] arg1, int arg2);

    public abstract int getAttributeListValue(java.lang.String arg0, java.lang.String arg1, java.lang.String[] arg2, int arg3);

    public abstract java.lang.String getAttributeName(int arg0);

    public abstract int getAttributeNameResource(int arg0);

    public default java.lang.String getAttributeNamespace(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/AttributeSet;->getAttributeNamespace(I)Ljava/lang/String;");
    }

    public abstract int getAttributeResourceValue(int arg0, int arg1);

    public abstract int getAttributeResourceValue(java.lang.String arg0, java.lang.String arg1, int arg2);

    public abstract int getAttributeUnsignedIntValue(int arg0, int arg1);

    public abstract int getAttributeUnsignedIntValue(java.lang.String arg0, java.lang.String arg1, int arg2);

    public abstract java.lang.String getAttributeValue(int arg0);

    public abstract java.lang.String getAttributeValue(java.lang.String arg0, java.lang.String arg1);

    public abstract java.lang.String getClassAttribute();

    public abstract java.lang.String getIdAttribute();

    public abstract int getIdAttributeResourceValue(int arg0);

    public abstract java.lang.String getPositionDescription();

    public abstract int getStyleAttribute();
    }
