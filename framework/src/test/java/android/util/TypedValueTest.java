package android.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** AOSP complex-value conversion, including every fixed-point radix. */
class TypedValueTest {

    @Test
    void complexConvertersHonorEveryRadixMultiplier() {
        int[] radices = {
            TypedValue.COMPLEX_RADIX_23p0,
            TypedValue.COMPLEX_RADIX_16p7,
            TypedValue.COMPLEX_RADIX_8p15,
            TypedValue.COMPLEX_RADIX_0p23
        };
        // The raw mantissa is 65,536 in each value. These expectations pin
        // the radix multiplier and the mantissa's eight-bit shift together.
        float[] expected = {65536.0f, 512.0f, 2.0f, 1.0f / 128.0f};
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.density = 2.0f;

        for (int i = 0; i < radices.length; i++) {
            int complex = 0x01000000
                    | (radices[i] << TypedValue.COMPLEX_RADIX_SHIFT)
                    | TypedValue.COMPLEX_UNIT_DIP;

            assertEquals(expected[i], TypedValue.complexToFloat(complex), 0.0f,
                    "complexToFloat radix " + radices[i]);
            assertEquals(expected[i] * metrics.density,
                    TypedValue.complexToDimension(complex, metrics), 0.0f,
                    "complexToDimension radix " + radices[i]);
        }
    }
}
