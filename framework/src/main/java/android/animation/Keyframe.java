package android.animation;

    public abstract class Keyframe implements java.lang.Cloneable {

    public Keyframe() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;-><init>()V");
    }

    public abstract android.animation.Keyframe clone();

    public float getFraction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->getFraction()F");
    }

    public android.animation.TimeInterpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->getInterpolator()Landroid/animation/TimeInterpolator;");
    }

    public java.lang.Class getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->getType()Ljava/lang/Class;");
    }

    public abstract java.lang.Object getValue();

    public boolean hasValue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->hasValue()Z");
    }

    public static android.animation.Keyframe ofFloat(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->ofFloat(F)Landroid/animation/Keyframe;");
    }

    public static android.animation.Keyframe ofFloat(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->ofFloat(FF)Landroid/animation/Keyframe;");
    }

    public static android.animation.Keyframe ofInt(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->ofInt(F)Landroid/animation/Keyframe;");
    }

    public static android.animation.Keyframe ofInt(float arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->ofInt(FI)Landroid/animation/Keyframe;");
    }

    public static android.animation.Keyframe ofObject(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->ofObject(F)Landroid/animation/Keyframe;");
    }

    public static android.animation.Keyframe ofObject(float arg0, java.lang.Object arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->ofObject(FLjava/lang/Object;)Landroid/animation/Keyframe;");
    }

    public void setFraction(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->setFraction(F)V");
    }

    public void setInterpolator(android.animation.TimeInterpolator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Keyframe;->setInterpolator(Landroid/animation/TimeInterpolator;)V");
    }

    public abstract void setValue(java.lang.Object arg0);
    }
