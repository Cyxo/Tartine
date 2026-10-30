package android.view.animation;

import android.graphics.Matrix;

    public class Transformation {

    public static final int TYPE_ALPHA = 1;
    public static final int TYPE_BOTH = 3;
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_MATRIX = 2;
    protected float mAlpha;
    protected Matrix mMatrix;
    protected int mTransformationType;

    public Transformation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;-><init>()V");
    }

    public void clear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->clear()V");
    }

    public void compose(Transformation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->compose(Landroid/view/animation/Transformation;)V");
    }

    public float getAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->getAlpha()F");
    }

    public Matrix getMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->getMatrix()Landroid/graphics/Matrix;");
    }

    public int getTransformationType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->getTransformationType()I");
    }

    public void set(Transformation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->set(Landroid/view/animation/Transformation;)V");
    }

    public void setAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->setAlpha(F)V");
    }

    public void setTransformationType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->setTransformationType(I)V");
    }

    public String toShortString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->toShortString()Ljava/lang/String;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Transformation;->toString()Ljava/lang/String;");
    }
    }
