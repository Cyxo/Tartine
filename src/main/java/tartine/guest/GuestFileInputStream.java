package tartine.guest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Guest-facing {@link FileInputStream}: the translator rewrites
 * {@code NEW java.io.FileInputStream} and its constructor call here, and the
 * constructor opens the {@link tartine.runtime.Vfs}-mapped host path.
 *
 * <p>Final by design: guest subclasses of {@code FileInputStream} are
 * denied by class metadata, so the rewrite never initializes a subclass
 * instance through this constructor.
 */
public final class GuestFileInputStream extends FileInputStream {
    public GuestFileInputStream(File file) throws FileNotFoundException {
        super(GuestFileStreams.hostForRead(file));
    }

    public GuestFileInputStream(String name) throws FileNotFoundException {
        super(GuestFileStreams.hostForRead(name));
    }
}
