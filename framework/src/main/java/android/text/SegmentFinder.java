package android.text;

    public abstract class SegmentFinder {

    public static final int DONE = -1;

    public SegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/SegmentFinder;-><init>()V");
    }

    public abstract int nextEndBoundary(int arg0);

    public abstract int nextStartBoundary(int arg0);

    public abstract int previousEndBoundary(int arg0);

    public abstract int previousStartBoundary(int arg0);
    }
