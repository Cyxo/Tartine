package tartine.vm;

/**
 * Host side of a Kotlin {@code Lazy} delegate: the initializer plus the
 * cached value. Single-threaded; the thread-safety mode is ignored.
 */
public final class LazyHolder {
    /** The {@code Function0} initializer (a VmObject from app dex). */
    public final VmObject function;
    public Object value;
    public boolean initialized;

    public LazyHolder(VmObject function) {
        this.function = function;
    }
}
