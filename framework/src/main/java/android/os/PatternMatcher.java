package android.os;

    public class PatternMatcher implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int PATTERN_ADVANCED_GLOB = 3;
    public static final int PATTERN_LITERAL = 0;
    public static final int PATTERN_PREFIX = 1;
    public static final int PATTERN_SIMPLE_GLOB = 2;
    public static final int PATTERN_SUFFIX = 4;

    public PatternMatcher(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;-><init>(Landroid/os/Parcel;)V");
    }

    public PatternMatcher(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;-><init>(Ljava/lang/String;I)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;->describeContents()I");
    }

    public final java.lang.String getPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;->getPath()Ljava/lang/String;");
    }

    public final int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;->getType()I");
    }

    public boolean match(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;->match(Ljava/lang/String;)Z");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PatternMatcher;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public PatternMatcher() {
        }
    }
