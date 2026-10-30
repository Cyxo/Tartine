package android.graphics;

    public class MeshSpecification {

    public static final int ALPHA_TYPE_OPAQUE = 1;
    public static final int ALPHA_TYPE_PREMULTIPLIED = 2;
    public static final int ALPHA_TYPE_UNKNOWN = 0;
    public static final int ALPHA_TYPE_UNPREMULTIPLIED = 3;
    public static final int TYPE_FLOAT = 0;
    public static final int TYPE_FLOAT2 = 1;
    public static final int TYPE_FLOAT3 = 2;
    public static final int TYPE_FLOAT4 = 3;
    public static final int TYPE_UBYTE4 = 4;

    public static MeshSpecification make(MeshSpecification.Attribute[] arg0, int arg1, MeshSpecification.Varying[] arg2, String arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification;->make([Landroid/graphics/MeshSpecification$Attribute;I[Landroid/graphics/MeshSpecification$Varying;Ljava/lang/String;Ljava/lang/String;)Landroid/graphics/MeshSpecification;");
    }

    public static MeshSpecification make(MeshSpecification.Attribute[] arg0, int arg1, MeshSpecification.Varying[] arg2, String arg3, String arg4, ColorSpace arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification;->make([Landroid/graphics/MeshSpecification$Attribute;I[Landroid/graphics/MeshSpecification$Varying;Ljava/lang/String;Ljava/lang/String;Landroid/graphics/ColorSpace;)Landroid/graphics/MeshSpecification;");
    }

    public static MeshSpecification make(MeshSpecification.Attribute[] arg0, int arg1, MeshSpecification.Varying[] arg2, String arg3, String arg4, ColorSpace arg5, int arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification;->make([Landroid/graphics/MeshSpecification$Attribute;I[Landroid/graphics/MeshSpecification$Varying;Ljava/lang/String;Ljava/lang/String;Landroid/graphics/ColorSpace;I)Landroid/graphics/MeshSpecification;");
    }
        public static class Attribute {

        public Attribute(int arg0, int arg1, String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Attribute;-><init>(IILjava/lang/String;)V");
        }

        public String getName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Attribute;->getName()Ljava/lang/String;");
        }

        public int getOffset() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Attribute;->getOffset()I");
        }

        public int getType() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Attribute;->getType()I");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Attribute;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Attribute() {
            }
        }
        public static class Varying {

        public Varying(int arg0, String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Varying;-><init>(ILjava/lang/String;)V");
        }

        public String getName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Varying;->getName()Ljava/lang/String;");
        }

        public int getType() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Varying;->getType()I");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/MeshSpecification$Varying;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Varying() {
            }
        }
    }
