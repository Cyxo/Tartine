package tartine.guest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Guest-facing {@link PrintStream}: the translator rewrites
 * {@code NEW java.io.PrintStream} and its constructor call here. The
 * file-backed constructors open the {@link tartine.runtime.Vfs}-mapped host
 * path (read-only mounts are refused); the stream-backed constructors pass
 * through unchanged so wrapping {@code System.out}-style streams still
 * works.
 */
public final class GuestPrintStream extends PrintStream {
    public GuestPrintStream(OutputStream out) {
        super(out);
    }

    public GuestPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public GuestPrintStream(OutputStream out, boolean autoFlush, String encoding)
            throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public GuestPrintStream(OutputStream out, boolean autoFlush,
            Charset charset) {
        super(out, autoFlush, charset);
    }

    public GuestPrintStream(File file) throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(file));
    }

    public GuestPrintStream(File file, String csn)
            throws FileNotFoundException, UnsupportedEncodingException {
        super(GuestFileStreams.hostForWrite(file), csn);
    }

    public GuestPrintStream(File file, Charset charset) throws IOException {
        super(GuestFileStreams.hostForWrite(file), charset);
    }

    public GuestPrintStream(String fileName) throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(fileName));
    }

    public GuestPrintStream(String fileName, String csn)
            throws FileNotFoundException, UnsupportedEncodingException {
        super(GuestFileStreams.hostForWrite(fileName), csn);
    }

    public GuestPrintStream(String fileName, Charset charset)
            throws IOException {
        super(GuestFileStreams.hostForWrite(fileName), charset);
    }
}
