package android.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Desktop mapping (Tartine). Ported from
 * aosp-frameworks-base/core/java/android/util/Log.java (AOSP): the same
 * VERBOSE..ASSERT priorities, tag-based API and {@code println} funnel.
 *
 * <p>The device writes to liblog (logcat); the desktop routes every call to
 * {@link java.util.logging} instead, so logcat calls respect the host
 * {@code log.level} and land in the same console output as the runtime's own
 * logs. The logger name is the class that called {@code Log} (found through a
 * {@link StackWalker}), not {@code android.util.Log}, which gives logcat's
 * usual class prefix. A call is dropped when its mapped JUL level is below the
 * configured root level, exactly like a tag with a lower logcat priority.
 */
public final class Log {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    /** Caller lookup: skip our own frames, keep the app class. */
    private static final StackWalker CALLERS = StackWalker.getInstance();

    /** AOSP has a package-private constructor; never instantiated. */
    Log() {
    }

    public static int v(String tag, String msg) {
        return println(VERBOSE, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return println(VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int d(String tag, String msg) {
        return println(DEBUG, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return println(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int i(String tag, String msg) {
        return println(INFO, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return println(INFO, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(String tag, String msg) {
        return println(WARN, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return println(WARN, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(String tag, Throwable tr) {
        return println(WARN, tag, getStackTraceString(tr));
    }

    public static int e(String tag, String msg) {
        return println(ERROR, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return println(ERROR, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int wtf(String tag, String msg) {
        return println(ASSERT, tag, msg);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return println(ASSERT, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int wtf(String tag, Throwable tr) {
        return println(ASSERT, tag, getStackTraceString(tr));
    }

    /** Whether a tag at {@code priority} passes the configured log level. */
    public static boolean isLoggable(String tag, int level) {
        return logger(tag).isLoggable(julLevel(level));
    }

    /**
     * The single funnel every priority method goes through, mirroring
     * AOSP's {@code println_native}: map the priority, resolve the calling
     * class, log when the level is enabled.
     */
    public static int println(int priority, String tag, String msg) {
        Logger logger = logger(tag);
        Level level = julLevel(priority);
        if (logger.isLoggable(level)) {
            logger.log(level, msg);
        }
        return msg == null ? 0 : msg.length();
    }

    /** AOSP: the throwable's stack trace, or an empty string for null. */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter trace = new StringWriter();
        tr.printStackTrace(new PrintWriter(trace));
        return trace.toString();
    }

    /** Caller-class logger (logcat's class prefix), tag as a fallback. */
    private static Logger logger(String tag) {
        String caller = CALLERS.walk(frames -> frames
                .filter(frame -> !isLogFrame(frame.getClassName()))
                .map(StackWalker.StackFrame::getClassName)
                .findFirst()
                .orElse(null));
        if (caller != null) {
            return Logger.getLogger(caller);
        }
        return Logger.getLogger(tag != null ? tag : "android.util.Log");
    }

    /** True for {@code android.util.Log} and its nested classes only. */
    private static boolean isLogFrame(String className) {
        return className.equals("android.util.Log")
                || className.startsWith("android.util.Log$");
    }

    /** Logcat priority to JUL level; unknown priorities are INFO. */
    private static Level julLevel(int priority) {
        return switch (priority) {
            case VERBOSE -> Level.FINEST;
            case DEBUG -> Level.FINE;
            case WARN -> Level.WARNING;
            case ERROR, ASSERT -> Level.SEVERE;
            default -> Level.INFO;
        };
    }
}