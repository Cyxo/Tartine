package android.text;

    public class TextPaint extends android.graphics.Paint {

    // AOSP core/java/android/text/TextPaint.java:30-47 field block; density
    // default 1.0f is a field initializer there (:37), covering every ctor.
    public int baselineShift;
    public int bgColor;
    public float density = 1.0f;
    public int[] drawableState;
    public int linkColor;
    // AOSP :39-42: "Special value 0 means no custom underline".
    public int underlineColor = 0;
    // AOSP :44-47 "Thickness of the underline, in pixels".
    public float underlineThickness;

    // AOSP TextPaint.java:49-59: all three ctors delegate straight to the
    // Paint ctor (field initializers above carry the defaults).
    public TextPaint() {
        super();
    }

    public TextPaint(android.graphics.Paint arg0) {
        super(arg0);
    }

    public TextPaint(int arg0) {
        super(arg0);
    }

    /**
     * AOSP TextPaint.java:61-75 ("Copy the fields from tp into this
     * TextPaint, including the fields inherited from Paint"): super.set
     * then the TextPaint-only fields verbatim.
     */
    public void set(android.text.TextPaint arg0) {
        super.set(arg0);
        bgColor = arg0.bgColor;
        baselineShift = arg0.baselineShift;
        linkColor = arg0.linkColor;
        drawableState = arg0.drawableState;
        density = arg0.density;
        underlineColor = arg0.underlineColor;
        underlineThickness = arg0.underlineThickness;
    }

    /**
     * AOSP TextPaint.java:83-87 (@UnsupportedAppUsage): custom underline
     * state in plain fields.
     */
    public void setUnderlineText(int color, float thickness) {
        underlineColor = color;
        underlineThickness = thickness;
    }

    /**
     * AOSP TextPaint.java:92-99: custom thickness only while a custom
     * underline color is set; otherwise the Paint text metric.
     */
    @Override
    public float getUnderlineThickness() {
        if (underlineColor != 0) {
            return underlineThickness;
        }
        return super.getUnderlineThickness();
    }
    }
