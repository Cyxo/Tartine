package tartine.fw;

/**
 * Host seam value carrier: one resolved resource value.
 *
 * <p>Mirrors the {@code Res_value} / {@code android.util.TypedValue} shape —
 * a data-type tag and a data word — plus the resolved string text when the
 * value is a string (the data word then holds the host string-pool index,
 * which the guest never sees). {@code sourceId} is the resource id the value
 * was read from, so {@code TypedValue.resourceId} and
 * {@code TypedArray.getResourceId} can report it.
 *
 * <p>{@code type}/{@code data} use the {@code android.util.TypedValue}
 * constants directly, so the framework maps this record onto a
 * {@code TypedValue} without translation.
 */
public final class ResValue {

    /** {@code TypedValue} data-type tag ({@code TYPE_*}). */
    public final int type;
    /** Raw {@code Res_value} data word. */
    public final int data;
    /** Resolved text for {@code TYPE_STRING} (else {@code null}). */
    public final String string;
    /** Resource id the value came from, or 0. */
    public final int sourceId;

    public ResValue(int type, int data, String string, int sourceId) {
        this.type = type;
        this.data = data;
        this.string = string;
        this.sourceId = sourceId;
    }
}
