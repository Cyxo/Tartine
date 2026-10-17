package android.graphics;

    public abstract class ColorSpace {

    public static float[] ILLUMINANT_A;
    public static float[] ILLUMINANT_B;
    public static float[] ILLUMINANT_C;
    public static float[] ILLUMINANT_D50;
    public static float[] ILLUMINANT_D55;
    public static float[] ILLUMINANT_D60;
    public static float[] ILLUMINANT_D65;
    public static float[] ILLUMINANT_D75;
    public static float[] ILLUMINANT_E;
    public static final int MAX_ID = 63;
    public static final int MIN_ID = -1;

    public static android.graphics.ColorSpace adapt(android.graphics.ColorSpace arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->adapt(Landroid/graphics/ColorSpace;[F)Landroid/graphics/ColorSpace;");
    }

    public static android.graphics.ColorSpace adapt(android.graphics.ColorSpace arg0, float[] arg1, android.graphics.ColorSpace.Adaptation arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->adapt(Landroid/graphics/ColorSpace;[FLandroid/graphics/ColorSpace$Adaptation;)Landroid/graphics/ColorSpace;");
    }

    public static float[] cctToXyz(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->cctToXyz(I)[F");
    }

    public static float[] chromaticAdaptation(android.graphics.ColorSpace.Adaptation arg0, float[] arg1, float[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->chromaticAdaptation(Landroid/graphics/ColorSpace$Adaptation;[F[F)[F");
    }

    public static android.graphics.ColorSpace.Connector connect(android.graphics.ColorSpace arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->connect(Landroid/graphics/ColorSpace;)Landroid/graphics/ColorSpace$Connector;");
    }

    public static android.graphics.ColorSpace.Connector connect(android.graphics.ColorSpace arg0, android.graphics.ColorSpace arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->connect(Landroid/graphics/ColorSpace;Landroid/graphics/ColorSpace;)Landroid/graphics/ColorSpace$Connector;");
    }

    public static android.graphics.ColorSpace.Connector connect(android.graphics.ColorSpace arg0, android.graphics.ColorSpace arg1, android.graphics.ColorSpace.RenderIntent arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->connect(Landroid/graphics/ColorSpace;Landroid/graphics/ColorSpace;Landroid/graphics/ColorSpace$RenderIntent;)Landroid/graphics/ColorSpace$Connector;");
    }

    public static android.graphics.ColorSpace.Connector connect(android.graphics.ColorSpace arg0, android.graphics.ColorSpace.RenderIntent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->connect(Landroid/graphics/ColorSpace;Landroid/graphics/ColorSpace$RenderIntent;)Landroid/graphics/ColorSpace$Connector;");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->equals(Ljava/lang/Object;)Z");
    }

    public float[] fromXyz(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->fromXyz(FFF)[F");
    }

    public abstract float[] fromXyz(float[] arg0);

    public static android.graphics.ColorSpace get(android.graphics.ColorSpace.Named arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->get(Landroid/graphics/ColorSpace$Named;)Landroid/graphics/ColorSpace;");
    }

    public int getComponentCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->getComponentCount()I");
    }

    public int getDataSpace() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->getDataSpace()I");
    }

    public static android.graphics.ColorSpace getFromDataSpace(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->getFromDataSpace(I)Landroid/graphics/ColorSpace;");
    }

    public int getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->getId()I");
    }

    public abstract float getMaxValue(int arg0);

    public abstract float getMinValue(int arg0);

    public android.graphics.ColorSpace.Model getModel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->getModel()Landroid/graphics/ColorSpace$Model;");
    }

    public java.lang.String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->getName()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->hashCode()I");
    }

    public boolean isSrgb() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->isSrgb()Z");
    }

    public abstract boolean isWideGamut();

    public static android.graphics.ColorSpace match(float[] arg0, android.graphics.ColorSpace.Rgb.TransferParameters arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->match([FLandroid/graphics/ColorSpace$Rgb$TransferParameters;)Landroid/graphics/ColorSpace;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->toString()Ljava/lang/String;");
    }

    public float[] toXyz(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace;->toXyz(FFF)[F");
    }

    public abstract float[] toXyz(float[] arg0);
        public static enum Adaptation {
            BRADFORD, CIECAT02, VON_KRIES;
        }
        public static class Connector {

        public android.graphics.ColorSpace getDestination() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Connector;->getDestination()Landroid/graphics/ColorSpace;");
        }

        public android.graphics.ColorSpace.RenderIntent getRenderIntent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Connector;->getRenderIntent()Landroid/graphics/ColorSpace$RenderIntent;");
        }

        public android.graphics.ColorSpace getSource() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Connector;->getSource()Landroid/graphics/ColorSpace;");
        }

        public float[] transform(float arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Connector;->transform(FFF)[F");
        }

        public float[] transform(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Connector;->transform([F)[F");
        }
        }
        public static enum Model {
            CMYK, LAB, RGB, XYZ;

        public int getComponentCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Model;->getComponentCount()I");
        }
        }
        public static enum Named {
            ACES, ACESCG, ADOBE_RGB, BT2020, BT2020_HLG, BT2020_PQ, BT709, CIE_LAB, CIE_XYZ, DCI_P3, DISPLAY_P3, EXTENDED_SRGB, LINEAR_EXTENDED_SRGB, LINEAR_SRGB, NTSC_1953, OK_LAB, PRO_PHOTO_RGB, SMPTE_C, SRGB;
        }
        public static enum RenderIntent {
            ABSOLUTE, PERCEPTUAL, RELATIVE, SATURATION;
        }
        public static class Rgb extends android.graphics.ColorSpace {

        public Rgb(java.lang.String arg0, float[] arg1, android.graphics.ColorSpace.Rgb.TransferParameters arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;-><init>(Ljava/lang/String;[FLandroid/graphics/ColorSpace$Rgb$TransferParameters;)V");
        }

        public Rgb(java.lang.String arg0, float[] arg1, double arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;-><init>(Ljava/lang/String;[FD)V");
        }

        public Rgb(java.lang.String arg0, float[] arg1, float[] arg2, android.graphics.ColorSpace.Rgb.TransferParameters arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;-><init>(Ljava/lang/String;[F[FLandroid/graphics/ColorSpace$Rgb$TransferParameters;)V");
        }

        public Rgb(java.lang.String arg0, float[] arg1, float[] arg2, double arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;-><init>(Ljava/lang/String;[F[FD)V");
        }

        public Rgb(java.lang.String arg0, float[] arg1, float[] arg2, java.util.function.DoubleUnaryOperator arg3, java.util.function.DoubleUnaryOperator arg4, float arg5, float arg6) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;-><init>(Ljava/lang/String;[F[FLjava/util/function/DoubleUnaryOperator;Ljava/util/function/DoubleUnaryOperator;FF)V");
        }

        public Rgb(java.lang.String arg0, float[] arg1, java.util.function.DoubleUnaryOperator arg2, java.util.function.DoubleUnaryOperator arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;-><init>(Ljava/lang/String;[FLjava/util/function/DoubleUnaryOperator;Ljava/util/function/DoubleUnaryOperator;)V");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->equals(Ljava/lang/Object;)Z");
        }

        public float[] fromLinear(float arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->fromLinear(FFF)[F");
        }

        public float[] fromLinear(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->fromLinear([F)[F");
        }

        public float[] fromXyz(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->fromXyz([F)[F");
        }

        public java.util.function.DoubleUnaryOperator getEotf() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getEotf()Ljava/util/function/DoubleUnaryOperator;");
        }

        public float[] getInverseTransform() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getInverseTransform()[F");
        }

        public float[] getInverseTransform(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getInverseTransform([F)[F");
        }

        public float getMaxValue(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getMaxValue(I)F");
        }

        public float getMinValue(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getMinValue(I)F");
        }

        public java.util.function.DoubleUnaryOperator getOetf() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getOetf()Ljava/util/function/DoubleUnaryOperator;");
        }

        public float[] getPrimaries() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getPrimaries()[F");
        }

        public float[] getPrimaries(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getPrimaries([F)[F");
        }

        public android.graphics.ColorSpace.Rgb.TransferParameters getTransferParameters() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getTransferParameters()Landroid/graphics/ColorSpace$Rgb$TransferParameters;");
        }

        public float[] getTransform() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getTransform()[F");
        }

        public float[] getTransform(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getTransform([F)[F");
        }

        public float[] getWhitePoint() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getWhitePoint()[F");
        }

        public float[] getWhitePoint(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->getWhitePoint([F)[F");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->hashCode()I");
        }

        public boolean isSrgb() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->isSrgb()Z");
        }

        public boolean isWideGamut() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->isWideGamut()Z");
        }

        public float[] toLinear(float arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->toLinear(FFF)[F");
        }

        public float[] toLinear(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->toLinear([F)[F");
        }

        public float[] toXyz(float[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb;->toXyz([F)[F");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Rgb() {
            }
            public static class TransferParameters {

            public double a;
            public double b;
            public double c;
            public double d;
            public double e;
            public double f;
            public double g;

            public TransferParameters(double arg0, double arg1, double arg2, double arg3, double arg4) {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb$TransferParameters;-><init>(DDDDD)V");
            }

            public TransferParameters(double arg0, double arg1, double arg2, double arg3, double arg4, double arg5, double arg6) {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb$TransferParameters;-><init>(DDDDDDD)V");
            }

            public boolean equals(java.lang.Object arg0) {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb$TransferParameters;->equals(Ljava/lang/Object;)Z");
            }

            public int hashCode() {
                throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/ColorSpace$Rgb$TransferParameters;->hashCode()I");
            }

                /** Scratch mirror constructor (SkeletonGen). */
                public TransferParameters() {
                }
            }
        }
    }
