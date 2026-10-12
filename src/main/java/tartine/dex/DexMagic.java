package tartine.dex;

/**
 * DEX file magic handling.
 *
 * <p>A {@code classes.dex} file starts with 8 magic bytes: {@code "dex\n"}
 * followed by a 3-digit version ({@code 035}, {@code 037}, {@code 038},
 * {@code 039}) and a zero byte. See {@code source.android.com/docs/core/runtime/dex-format}.
 */
public final class DexMagic {
    private DexMagic() {
    }

    /** First four bytes of every DEX file: {@code 64 65 78 0A} ("dex\n"). */
    public static final byte[] PREFIX = new byte[]{0x64, 0x65, 0x78, 0x0A};

    /** Known DEX format versions, oldest first. */
    public static final String[] VERSIONS = new String[]{"035", "037", "038", "039"};

    /**
     * Returns true if {@code data} starts with a valid 8-byte DEX magic.
     */
    public static boolean hasDexMagic(byte[] data) {
        if (data == null || data.length < 8) {
            return false;
        }
        for (int i = 0; i < PREFIX.length; i++) {
            if (data[i] != PREFIX[i]) {
                return false;
            }
        }
        for (int i = 0; i < 3; i++) {
            byte b = data[4 + i];
            if (b < '0' || b > '9') {
                return false;
            }
        }
        return data[7] == 0;
    }
}
