package tartine.config;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pins the one-line logcat-style console format: priority letter, logger name
 * (the caller class for {@code android.util.Log}), message, and an appended
 * throwable stack trace.
 */
class LogFormatTest {

    @Test
    void formatsOneLogcatLine() {
        LogRecord record = new LogRecord(Level.INFO, "hello");
        record.setLoggerName("com.example.MainActivity");
        assertEquals("I/com.example.MainActivity: hello"
                + System.lineSeparator(),
                new LogFormat().format(record));
    }

    @Test
    void priorityLettersFollowTheJulLevel() {
        assertEquals('V', letter(Level.FINEST));
        assertEquals('V', letter(Level.FINER));
        assertEquals('D', letter(Level.FINE));
        assertEquals('D', letter(Level.CONFIG));
        assertEquals('I', letter(Level.INFO));
        assertEquals('W', letter(Level.WARNING));
        assertEquals('E', letter(Level.SEVERE));
    }

    @Test
    void throwableIsAppendedAsAStackTrace() {
        IllegalStateException boom = new IllegalStateException("kaput");
        LogRecord record = new LogRecord(Level.SEVERE, "failed");
        record.setLoggerName("com.example.MainActivity");
        record.setThrown(boom);
        String formatted = new LogFormat().format(record);
        assertTrue(formatted.startsWith(
                "E/com.example.MainActivity: failed"),
                formatted);
        assertTrue(formatted.contains("IllegalStateException: kaput"),
                formatted);
        assertTrue(formatted.contains(LogFormatTest.class.getName()),
                formatted);
    }

    private static char letter(Level level) {
        LogRecord record = new LogRecord(level, "");
        record.setLoggerName("test");
        return LogFormat.priority(record);
    }
}
