package tartine.vm;

/**
 * A DEX-level {@code throw}: carries the thrown value across interpreter
 * frames until a DEX catch handler claims it. Host failures (e.g.
 * {@link ArithmeticException} from division) are NOT wrapped and therefore
 * not catchable by DEX handlers yet.
 */
public final class VmThrow extends RuntimeException {
    public final Object value;

    public VmThrow(Object value) {
        super("dex throw");
        this.value = value;
    }
}
