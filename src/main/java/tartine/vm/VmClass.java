package tartine.vm;

/**
 * A DEX {@code const-class} value: the class object for a descriptor.
 * Behavior ({@code getName}, {@code isInstance}, ...) lives in host
 * builtins; the interpreter never runs {@code Class} DEX bodies.
 */
public final class VmClass {
    /** Type descriptor, e.g. {@code Ltest/Counter;} or {@code [I}. */
    public final String descriptor;

    public VmClass(String descriptor) {
        this.descriptor = descriptor;
    }
}
