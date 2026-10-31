package tartine.guest;

/**
 * Raised when guest code asks for something the sandbox does not expose.
 *
 * <p>Translation turns a method-level denial into the usual named
 * {@code UnsupportedOperationException} gap; class-level denials become a
 * named {@code ClassNotFoundException} in {@code TranslatedLoader}. The
 * exception itself is a runtime failure (never a silent {@code false}) so a
 * missing bridge cannot be mistaken for an ordinary filesystem answer.
 */
public final class SandboxDenied extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SandboxDenied(String reason) {
        super(reason);
    }
}
