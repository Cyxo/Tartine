package android.graphics;

    public class PathIterator implements java.util.Iterator<android.graphics.PathIterator.Segment> {

    public static final int VERB_CLOSE = 5;
    public static final int VERB_CONIC = 3;
    public static final int VERB_CUBIC = 4;
    public static final int VERB_DONE = 6;
    public static final int VERB_LINE = 1;
    public static final int VERB_MOVE = 0;
    public static final int VERB_QUAD = 2;

    public boolean hasNext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator;->hasNext()Z");
    }

    public android.graphics.PathIterator.Segment next() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator;->next()Landroid/graphics/PathIterator$Segment;");
    }

    public int next(float[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator;->next([FI)I");
    }

    public int peek() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator;->peek()I");
    }
        public static class Segment {

        public float getConicWeight() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator$Segment;->getConicWeight()F");
        }

        public float[] getPoints() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator$Segment;->getPoints()[F");
        }

        public int getVerb() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/PathIterator$Segment;->getVerb()I");
        }
        }
    }
