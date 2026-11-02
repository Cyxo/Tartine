package tartine.guest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Guest-facing {@link FileWriter}: the translator rewrites
 * {@code NEW java.io.FileWriter} and its constructor call here, and the
 * constructor opens the {@link tartine.runtime.Vfs}-mapped host path.
 * A read-only mount refuses to open before the host file is touched.
 */
public final class GuestFileWriter extends FileWriter {
    public GuestFileWriter(File file) throws IOException {
        super(GuestFileStreams.hostForWrite(file));
    }

    public GuestFileWriter(File file, boolean append) throws IOException {
        super(GuestFileStreams.hostForWrite(file), append);
    }

    public GuestFileWriter(File file, Charset charset) throws IOException {
        super(GuestFileStreams.hostForWrite(file), charset);
    }

    public GuestFileWriter(File file, Charset charset, boolean append)
            throws IOException {
        super(GuestFileStreams.hostForWrite(file), charset, append);
    }

    public GuestFileWriter(String fileName) throws IOException {
        super(GuestFileStreams.hostForWrite(fileName));
    }

    public GuestFileWriter(String fileName, boolean append)
            throws IOException {
        super(GuestFileStreams.hostForWrite(fileName), append);
    }

    public GuestFileWriter(String fileName, Charset charset)
            throws IOException {
        super(GuestFileStreams.hostForWrite(fileName), charset);
    }

    public GuestFileWriter(String fileName, Charset charset, boolean append)
            throws IOException {
        super(GuestFileStreams.hostForWrite(fileName), charset, append);
    }
}
