package tartine.guest;

import java.util.logging.Logger;

/**
 * Logging seam reachable from translated guest bytecode. {@code tartine.guest}
 * is allow-listed by the sandbox so guest apps (and their injected call sites)
 * can call it without exposing host internals.
 */
public final class TxTrace {
    private static final Logger log =
            Logger.getLogger(TxTrace.class.getName());

    private TxTrace() {
    }

    /** Very verbose call-site trace used while porting the framework. */
    public static void site(String owner, String name) {
        log.finest("tx: site " + owner + "->" + name);
    }
}