package android.graphics;

public class Typeface {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    public static final int ITALIC = 2;
    public static final int NORMAL = 0;

    private static final Typeface INSTANCE = new Typeface(0, NORMAL);

    public static final Typeface DEFAULT = INSTANCE;
    public static final Typeface DEFAULT_BOLD = new Typeface(0, BOLD);
    public static final Typeface MONOSPACE = INSTANCE;
    public static final Typeface SANS_SERIF = INSTANCE;
    public static final Typeface SERIF = INSTANCE;

    private final int style;
    private final int weight;

    private Typeface() { this.style = NORMAL; this.weight = 400; }

    private Typeface(int style, int weight) { this.style = style; this.weight = weight; }

    public static Typeface create(Typeface family, int style) {
        if (family == null) family = DEFAULT;
        return new Typeface(style, family.weight);
    }

    public static Typeface create(Typeface family, int style, boolean system) {
        return create(family, style);
    }

    public static Typeface create(String familyName, int style) {
        return new Typeface(style, 400);
    }

    public static Typeface createFromAsset(android.content.res.AssetManager mgr, String path) {
        return DEFAULT;
    }

    public static Typeface createFromFile(java.io.File file) { return DEFAULT; }

    public static Typeface createFromFile(String path) { return DEFAULT; }

    public static Typeface defaultFromStyle(int style) {
        switch (style) {
            case BOLD: return DEFAULT_BOLD;
            default: return DEFAULT;
        }
    }

    public int getStyle() { return style; }

    public int getWeight() { return weight; }

    public final boolean isBold() { return (style & BOLD) != 0; }

    public final boolean isItalic() { return (style & ITALIC) != 0; }

    public final String getSystemFontFamilyName() { return "sans-serif"; }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Typeface)) return false;
        Typeface o = (Typeface) other;
        return style == o.style && weight == o.weight;
    }

    public int hashCode() {
        return 31 * style + weight;
    }
}
