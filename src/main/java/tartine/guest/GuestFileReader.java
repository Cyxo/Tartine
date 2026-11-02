package tartine.guest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Guest-facing {@link FileReader}: the translator rewrites
 * {@code NEW java.io.FileReader} and its constructor call here, and the
 * constructor opens the {@link tartine.runtime.Vfs}-mapped host path.
 */
public final class GuestFileReader extends FileReader {
    public GuestFileReader(File file) throws FileNotFoundException {
        super(GuestFileStreams.hostForRead(file));
    }

    public GuestFileReader(File file, Charset charset) throws IOException {
        super(GuestFileStreams.hostForRead(file), charset);
    }

    public GuestFileReader(String fileName) throws FileNotFoundException {
        super(GuestFileStreams.hostForRead(fileName));
    }

    public GuestFileReader(String fileName, Charset charset)
            throws IOException {
        super(GuestFileStreams.hostForRead(fileName), charset);
    }
}
