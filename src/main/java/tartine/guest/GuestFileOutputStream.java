package tartine.guest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Guest-facing {@link FileOutputStream}: the translator rewrites
 * {@code NEW java.io.FileOutputStream} and its constructor call here, and
 * the constructor opens the {@link tartine.runtime.Vfs}-mapped host path.
 * A read-only mount refuses to open before the host file is touched.
 */
public final class GuestFileOutputStream extends FileOutputStream {
    public GuestFileOutputStream(File file) throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(file));
    }

    public GuestFileOutputStream(File file, boolean append)
            throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(file), append);
    }

    public GuestFileOutputStream(String name) throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(name));
    }

    public GuestFileOutputStream(String name, boolean append)
            throws FileNotFoundException {
        super(GuestFileStreams.hostForWrite(name), append);
    }
}
