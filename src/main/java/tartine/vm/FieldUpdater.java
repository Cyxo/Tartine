package tartine.vm;

/**
 * Host side of {@code AtomicReferenceFieldUpdater}: names a VmObject field.
 * Operations are plain map reads/writes (single thread, no volatility).
 * Type checks are lenient; mismatches surface at use sites.
 */
public final class FieldUpdater {
    public final String definingClass;
    public final String field;

    public FieldUpdater(String definingClass, String field) {
        this.definingClass = definingClass;
        this.field = field;
    }

    public String key() {
        return definingClass + "." + field;
    }

    public static VmObject checkedTarget(Object target) {
        if (!(target instanceof VmObject)) {
            throw new ClassCastException();
        }
        return (VmObject) target;
    }
}
