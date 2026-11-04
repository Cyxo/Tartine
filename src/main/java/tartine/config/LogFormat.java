package tartine.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * One-line logcat-style formatter: {@code P/logger: message} where {@code P}
 * is the priority letter (V/D/I/W/E) and the logger name is the class that
 * logged (for {@code android.util.Log}, the calling app class). Throwables are
 * appended as stack traces.
 */
final class LogFormat extends Formatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder text = new StringBuilder();
        text.append(priority(record)).append('/')
                .append(record.getLoggerName()).append(": ")
                .append(formatMessage(record))
                .append(System.lineSeparator());
        if (record.getThrown() != null) {
            StringWriter trace = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(trace));
            text.append(trace);
        }
        return text.toString();
    }

    /** Logcat priority letter for a JUL level. */
    static char priority(LogRecord record) {
        int value = record.getLevel().intValue();
        if (value >= java.util.logging.Level.SEVERE.intValue()) {
            return 'E';
        }
        if (value >= java.util.logging.Level.WARNING.intValue()) {
            return 'W';
        }
        if (value >= java.util.logging.Level.INFO.intValue()) {
            return 'I';
        }
        if (value >= java.util.logging.Level.FINE.intValue()) {
            return 'D';
        }
        return 'V';
    }
}