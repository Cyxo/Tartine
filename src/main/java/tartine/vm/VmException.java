package tartine.vm;

/**
 * Failure contract: unknown opcodes and unimplemented (framework) methods
 * fail loudly with the offending opcode or signature. Each failure names
 * the next unit of work; see the VM decision in AGENTS.md.
 */
public class VmException extends RuntimeException {
    public VmException(String message) {
        super(message);
    }

    public VmException(String message, Throwable cause) {
        super(message, cause);
    }
}
