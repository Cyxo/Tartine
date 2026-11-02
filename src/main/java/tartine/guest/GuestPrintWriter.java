package tartine.guest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Guest-facing {@link PrintWriter}: the translator rewrites
 * {@code NEW java.io.PrintWriter} and its constructor call here. The
 * file-backed constructors open the {@link tartine.runtime.Vfs}-mapped host
 * path (read-only mounts are refused); the writer/stream-backed
 * constructors pass through unchanged.
 */
public final class GuestPrintWriter extends PrintWriter {
    public GuestPrintWriter(Writer out) {
        super(out);
    }

    public GuestPrintWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public GuestPrintWriter(OutputStream out) {
        super(out);
    }

    public GuestPrintWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public GuestPrintWriter(OutputStream out, boolean autoFlush,
            Charset charset) {
        super(out, autoFlush, charset);
    }

    public GuestPrintWriter(File file) throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(file));
    }

    public GuestPrintWriter(File file, String csn)
            throws FileNotFoundException, UnsupportedEncodingException {
        super(GuestFileStreams.hostForWrite(file), csn);
    }

    public GuestPrintWriter(File file, Charset charset) throws IOException {
        super(GuestFileStreams.hostForWrite(file), charset);
    }

    public GuestPrintWriter(String fileName) throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(fileName));
    }

    public GuestPrintWriter(String fileName, String csn)
            throws FileNotFoundException, UnsupportedEncodingException {
        super(GuestFileStreams.hostForWrite(fileName), csn);
    }

    public GuestPrintWriter(String fileName, Charset charset)
            throws IOException {
        super(GuestFileStreams.hostForWrite(fileName), charset);
    }
}
