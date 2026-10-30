package android.animation;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;

    public class PropertyValuesHolder implements Cloneable {

    public PropertyValuesHolder clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->clone()Landroid/animation/PropertyValuesHolder;");
    }

    public String getPropertyName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->getPropertyName()Ljava/lang/String;");
    }

    public static PropertyValuesHolder ofFloat(Property<?, Float> arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofFloat(Landroid/util/Property;[F)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofFloat(String arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofFloat(Ljava/lang/String;[F)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofInt(Property<?, Integer> arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofInt(Landroid/util/Property;[I)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofInt(String arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofInt(Ljava/lang/String;[I)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofKeyframe(Property arg0, Keyframe[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofKeyframe(Landroid/util/Property;[Landroid/animation/Keyframe;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofKeyframe(String arg0, Keyframe[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofKeyframe(Ljava/lang/String;[Landroid/animation/Keyframe;)Landroid/animation/PropertyValuesHolder;");
    }

    public static <T> PropertyValuesHolder ofMultiFloat(String arg0, TypeConverter<T, float[]> arg1, TypeEvaluator<T> arg2, Keyframe[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiFloat(Ljava/lang/String;Landroid/animation/TypeConverter;Landroid/animation/TypeEvaluator;[Landroid/animation/Keyframe;)Landroid/animation/PropertyValuesHolder;");
    }

    public static <V> PropertyValuesHolder ofMultiFloat(String arg0, TypeConverter<V, float[]> arg1, TypeEvaluator<V> arg2, V[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiFloat(Ljava/lang/String;Landroid/animation/TypeConverter;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofMultiFloat(String arg0, Path arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiFloat(Ljava/lang/String;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofMultiFloat(String arg0, float[][] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiFloat(Ljava/lang/String;[[F)Landroid/animation/PropertyValuesHolder;");
    }

    public static <T> PropertyValuesHolder ofMultiInt(String arg0, TypeConverter<T, int[]> arg1, TypeEvaluator<T> arg2, Keyframe[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiInt(Ljava/lang/String;Landroid/animation/TypeConverter;Landroid/animation/TypeEvaluator;[Landroid/animation/Keyframe;)Landroid/animation/PropertyValuesHolder;");
    }

    public static <V> PropertyValuesHolder ofMultiInt(String arg0, TypeConverter<V, int[]> arg1, TypeEvaluator<V> arg2, V[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiInt(Ljava/lang/String;Landroid/animation/TypeConverter;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofMultiInt(String arg0, Path arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiInt(Ljava/lang/String;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofMultiInt(String arg0, int[][] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofMultiInt(Ljava/lang/String;[[I)Landroid/animation/PropertyValuesHolder;");
    }

    public static <V> PropertyValuesHolder ofObject(Property arg0, TypeEvaluator<V> arg1, V[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofObject(Landroid/util/Property;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;");
    }

    public static <V> PropertyValuesHolder ofObject(Property<?, V> arg0, TypeConverter<PointF, V> arg1, Path arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofObject(Landroid/util/Property;Landroid/animation/TypeConverter;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;");
    }

    public static <T, V> PropertyValuesHolder ofObject(Property<?, V> arg0, TypeConverter<T, V> arg1, TypeEvaluator<T> arg2, T[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofObject(Landroid/util/Property;Landroid/animation/TypeConverter;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofObject(String arg0, TypeConverter<PointF, ?> arg1, Path arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofObject(Ljava/lang/String;Landroid/animation/TypeConverter;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;");
    }

    public static PropertyValuesHolder ofObject(String arg0, TypeEvaluator arg1, Object[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->ofObject(Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/PropertyValuesHolder;");
    }

    public void setConverter(TypeConverter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setConverter(Landroid/animation/TypeConverter;)V");
    }

    public void setEvaluator(TypeEvaluator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setEvaluator(Landroid/animation/TypeEvaluator;)V");
    }

    public void setFloatValues(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setFloatValues([F)V");
    }

    public void setIntValues(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setIntValues([I)V");
    }

    public void setKeyframes(Keyframe[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setKeyframes([Landroid/animation/Keyframe;)V");
    }

    public void setObjectValues(Object[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setObjectValues([Ljava/lang/Object;)V");
    }

    public void setProperty(Property arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setProperty(Landroid/util/Property;)V");
    }

    public void setPropertyName(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->setPropertyName(Ljava/lang/String;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/PropertyValuesHolder;->toString()Ljava/lang/String;");
    }
    }
