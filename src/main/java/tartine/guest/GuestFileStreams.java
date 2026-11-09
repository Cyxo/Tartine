package tartine.guest;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import tartine.fw.PermissionManager;
import tartine.runtime.Vfs;

/**
 * Mapping seam behind the guest {@code File*Stream}/{@code File*}Reader/
 * {@code File*}Writer/{@code PrintStream}/{@code PrintWriter} subclasses.
 *
 * <p>The translator rewrites {@code NEW java.io.FileInputStream} (and the
 * other file-backed stream/writer types) to the matching
 * {@code tartine.guest.GuestFile*} subclass and the constructor call to that
 * subclass, so the stack shape and the host stream type survive. Each
 * constructor resolves its guest path through {@link Vfs} here: reads on an
 * unmapped or symlink-escaped path answer {@link FileNotFoundException}
 * (the Java answer for a nonexistent file), writes fail closed with a named
 * {@link SandboxDenied}, and a write on a read-only mount is refused before
 * the host stream opens.
 */
public final class GuestFileStreams {
    private GuestFileStreams() {
    }

    /** Host file for a guest read; missing/unmapped paths fail closed. */
    public static File hostForRead(File file) throws FileNotFoundException {
        return read(file, null).toFile();
    }

    /** Host file for a guest read named by a guest path string. */
    public static File hostForRead(String name) throws FileNotFoundException {
        return read(null, name).toFile();
    }

    /** Host file for a guest write; read-only mounts are refused. */
    public static File hostForWrite(File file) throws FileNotFoundException {
        return write(file, null).toFile();
    }

    /** Host file for a guest write named by a guest path string. */
    public static File hostForWrite(String name) throws FileNotFoundException {
        return write(null, name).toFile();
    }

    private static Path read(File file, String name)
            throws FileNotFoundException {
        Vfs.Policy policy = Vfs.policy();
        String guest = file != null
                ? GuestFiles.guestPathOrNull(file, policy)
                : name;
        Path host = guest == null ? null : policy.secureTarget(guest, false);
        if (host == null) {
            throw new FileNotFoundException(
                    "no such file (guest sandbox): "
                    + (guest == null ? "outside the guest filesystem"
                            : guest));
        }
        if (!PermissionManager.storageAllowed(guest, false)) {
            throw new FileNotFoundException(
                    "permission denied (guest sandbox): " + guest);
        }
        return host;
    }

    private static Path write(File file, String name)
            throws FileNotFoundException {
        Vfs.Policy policy = Vfs.policy();
        String guest = file != null
                ? GuestFiles.guestPathOrNull(file, policy)
                : name;
        if (guest == null) {
            throw new SandboxDenied("path is outside the guest filesystem");
        }
        if (!PermissionManager.storageAllowed(guest, true)) {
            throw new SecurityException(
                    "Permission Denial: writing " + guest);
        }
        Path host = policy.secureTarget(guest, true);
        if (host == null) {
            throw new SandboxDenied("path is outside the guest filesystem");
        }
        if (policy.readOnlyHost(host)) {
            throw new SandboxDenied("path is on a read-only mount");
        }
        return host;
    }
}
