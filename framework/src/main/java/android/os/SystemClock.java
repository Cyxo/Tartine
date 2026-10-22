package android.os;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Desktop mapping (Tartine). Ported from
 * aosp-frameworks-base/core/java/android/os/SystemClock.java:
 * <ul>
 *   <li>{@code uptimeNanos()} — the AOSP ravenwood host mapping
 *       {@code uptimeNanos$ravenwood()} is {@code System.nanoTime() - anchor},
 *       anchored at process start; mirrored here verbatim.</li>
 *   <li>{@code uptimeMillis()} — AOSP {@code uptimeMillis$ravenwood()}:
 *       {@code uptimeNanos() / 1_000_000}.</li>
 *   <li>{@code elapsedRealtimeNanos()} — AOSP counts deep sleep too
 *       (CLOCK_BOOTTIME); host JVMs expose no boottime clock, so the
 *       monotonic {@code uptimeNanos} value is returned and time in host
 *       power suspend is counted. Documented gap, not a silent lie.</li>
 *   <li>{@code currentThreadTimeMicro()} — AOSP binds procfs; the portable
 *       equivalent is {@link ThreadMXBean#getCurrentThreadCpuTime()}</li>
 *   <li>{@code sleep(long)} — AOSP {@code sleep} never throws; loop
 *       {@link Thread#sleep} to the deadline.</li>
 * </ul>
 */
public class SystemClock {

    private static final long ANCHOR_NANO_TIME = System.nanoTime();

    private static final long WALL_ANCHOR_NANO_TIME = System.nanoTime();
    private static final long WALL_ANCHOR_MICROS = System.currentTimeMillis() * 1000L;

    private SystemClock() {
    }

    /** AOSP javadoc: "milliseconds of non-sleep uptime since boot". */
    public static long uptimeMillis() {
        return uptimeNanos() / 1_000_000L;
    }

    /** AOSP javadoc: "nanoseconds of non-sleep uptime since boot". */
    public static long uptimeNanos() {
        return System.nanoTime() - ANCHOR_NANO_TIME;
    }

    /** AOSP javadoc: "elapsed milliseconds since boot" (includes deep sleep). */
    public static long elapsedRealtime() {
        // Host gap: no CLOCK_BOOTTIME on the JVM; uptime == realtime here.
        return uptimeMillis();
    }

    /** AOSP javadoc: "elapsed nanoseconds since boot" (includes deep sleep). */
    public static long elapsedRealtimeNanos() {
        return uptimeNanos();
    }

    /** AOSP javadoc: "elapsed milliseconds in the thread". */
    public static long currentThreadTimeMillis() {
        return threadCpuNanos() / 1_000_000L;
    }

    /** AOSP javadoc: "elapsed microseconds in the thread". */
    public static long currentThreadTimeMicro() {
        return threadCpuNanos() / 1_000L;
    }

    /** AOSP javadoc: "elapsed microseconds in wall time". */
    public static long currentTimeMicro() {
        return WALL_ANCHOR_MICROS + (System.nanoTime() - WALL_ANCHOR_NANO_TIME) / 1_000L;
    }

    /**
     * AOSP javadoc: "Waits a given number of milliseconds (of uptimeMillis)
     * before returning... similar to Thread.sleep... does not throw
     * InterruptedException". Ported as a deadline loop that preserves the
     * interrupted flag instead of unwinding.
     */
    public static void sleep(long ms) {
        if (ms <= 0) {
            return;
        }
        long endNanos = System.nanoTime() + ms * 1_000_000L;
        long remaining = ms;
        while (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            remaining = (endNanos - System.nanoTime()) / 1_000_000L;
        }
    }

    private static long threadCpuNanos() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }
}
