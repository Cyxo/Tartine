package android.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Desktop logcat mapping: priorities become JUL levels, the configured level
 * filters lower priorities, and the logger name is the class that called
 * {@code Log} (not the supplied tag), giving logcat's class prefix.
 */
class LogTest {
    private final Logger root = Logger.getLogger("");
    private Level previousLevel;
    private List<LogRecord> records;
    private Handler capture;

    @BeforeEach
    void captureRoot() {
        previousLevel = root.getLevel();
        records = new ArrayList<>();
        capture = new Handler() {
            @Override
            public void publish(LogRecord record) {
                records.add(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        capture.setLevel(Level.ALL);
        root.addHandler(capture);
    }

    @AfterEach
    void restoreRoot() {
        root.removeHandler(capture);
        root.setLevel(previousLevel);
    }

    @Test
    void prioritiesMapOntoJulLevels() {
        root.setLevel(Level.ALL);
        Log.v("tag", "verbose");
        Log.d("tag", "debug");
        Log.i("tag", "info");
        Log.w("tag", "warn");
        Log.e("tag", "error");
        Log.wtf("tag", "assert");

        assertEquals(List.of(Level.FINEST, Level.FINE, Level.INFO,
                Level.WARNING, Level.SEVERE, Level.SEVERE),
                records.stream().map(LogRecord::getLevel).toList());
    }

    @Test
    void configuredLevelDropsLowerPriorities() {
        root.setLevel(Level.INFO);
        Log.v("tag", "verbose");
        Log.d("tag", "debug");
        Log.i("tag", "info");
        Log.w("tag", "warn");

        assertEquals(List.of(Level.INFO, Level.WARNING),
                records.stream().map(LogRecord::getLevel).toList());
    }

    @Test
    void callerClassIsTheLoggerNameNotTheTag() {
        root.setLevel(Level.INFO);
        AppCode.info("from the app");

        assertEquals(AppCode.class.getName(),
                records.get(0).getLoggerName());
    }

    @Test
    void isLoggableFollowsTheConfiguredLevel() {
        root.setLevel(Level.INFO);
        assertTrue(Log.isLoggable("tag", Log.INFO));
        assertTrue(Log.isLoggable("tag", Log.WARN));
        assertFalse(Log.isLoggable("tag", Log.DEBUG));
        assertFalse(Log.isLoggable("tag", Log.VERBOSE));

        root.setLevel(Level.FINE);
        assertTrue(Log.isLoggable("tag", Log.DEBUG));
        assertFalse(Log.isLoggable("tag", Log.VERBOSE));
    }

    @Test
    void printlnReturnsTheMessageLength() {
        root.setLevel(Level.FINE);
        assertEquals(5, Log.println(Log.INFO, "tag", "hello"));
        assertEquals(0, Log.println(Log.INFO, "tag", null));
    }

    @Test
    void throwableOverloadsAppendTheStackTraceToTheMessage() {
        root.setLevel(Level.SEVERE);
        IllegalStateException boom = new IllegalStateException("kaput");
        Log.e("tag", "failed", boom);

        LogRecord record = records.get(0);
        String message = String.valueOf(record.getMessage());
        assertTrue(message.contains("failed"), message);
        assertTrue(message.contains("IllegalStateException: kaput"), message);
        assertTrue(message.contains(LogTest.class.getName()), message);
    }

    @Test
    void getStackTraceStringHandlesNullAndThrowables() {
        assertEquals("", Log.getStackTraceString(null));
        String trace = Log.getStackTraceString(
                new IllegalStateException("boom"));
        assertTrue(trace.contains("IllegalStateException: boom"), trace);
        assertTrue(trace.contains(LogTest.class.getName()), trace);
    }

    @Test
    void nullTagDoesNotBreakCallerResolution() {
        root.setLevel(Level.INFO);
        assertEquals(2, Log.i(null, "ok"));
        assertEquals(LogTest.class.getName(),
                records.get(0).getLoggerName());
    }

    /** Non-test frame so the caller lookup has an app class to find. */
    private static final class AppCode {
        static void info(String message) {
            Log.i("app-tag", message);
        }
    }
}
