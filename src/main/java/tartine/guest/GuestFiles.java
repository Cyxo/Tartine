package tartine.guest;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;
import tartine.fw.PermissionManager;
import tartine.runtime.Vfs;

/**
 * Static bridge behind the translated {@code java.io.File} surface.
 *
 * <p>Every instance call on {@code java.io.File} is rewritten by the
 * translator to {@code INVOKESTATIC GuestFiles.<name>(File, ...)}. Guest
 * paths map through {@link Vfs}; a path that does not map is nonexistent for
 * reads and a {@link SandboxDenied} for writes, never a host fallback. A
 * read-only mount refuses every write-shaped operation with the same named
 * denial.
 */
public final class GuestFiles {
    private GuestFiles() {
    }

    /** Guest path carried by {@code file}, or {@code null} when unmapped. */
    static String guestPathOrNull(File file, Vfs.Policy policy) {
        if (file instanceof GuestFile guest) {
            return guest.guestPath();
        }
        if (file == null) {
            return null;
        }
        return policy.guestPath(file.toPath());
    }

    /** Guest path for {@code file}; unmapped host files are denied. */
    static String guestPath(File file) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        String guest = guestPathOrNull(file, Vfs.policy());
        if (guest == null) {
            throw new SandboxDenied("file is outside the guest filesystem");
        }
        return guest;
    }

    private static Path target(File file, Vfs.Policy policy) {
        String guest = guestPathOrNull(file, policy);
        if (guest == null || !PermissionManager.storageAllowed(guest, false)) {
            return null;
        }
        return policy.secureTarget(guest, false);
    }

    /** Like {@link #target} but gated on the shared-storage write grant. */
    private static Path writeTarget(File file, Vfs.Policy policy) {
        String guest = guestPathOrNull(file, policy);
        if (guest == null || !PermissionManager.storageAllowed(guest, true)) {
            return null;
        }
        return policy.secureTarget(guest, false);
    }

    private static Path requireTarget(File file, boolean write) {
        Vfs.Policy policy = Vfs.policy();
        String guest = guestPathOrNull(file, policy);
        if (guest == null) {
            throw new SandboxDenied("path is outside the guest filesystem");
        }
        if (!PermissionManager.storageAllowed(guest, write)) {
            throw new SecurityException("Permission Denial: "
                    + (write ? "writing " : "reading ") + guest);
        }
        // secureTarget rechecks the real path, so a symlink/junction inside
        // a mount cannot point the guest outside it.
        Path host = policy.secureTarget(guest, write);
        if (host == null) {
            throw new SandboxDenied("path is outside the guest filesystem");
        }
        if (write && policy.readOnlyHost(host)) {
            throw new SandboxDenied("path is on a read-only mount");
        }
        return host;
    }

    public static String getPath(File file) {
        return guestPath(file);
    }

    /** Last path segment, ignoring trailing separators. */
    public static String getName(File file) {
        String guest = guestPath(file);
        int end = guest.length();
        while (end > 1 && guest.charAt(end - 1) == '/') {
            end--;
        }
        int slash = guest.lastIndexOf('/', end - 1);
        return guest.substring(slash + 1, end);
    }

    /** Parent guest path, or null at the root. */
    public static String getParent(File file) {
        String guest = guestPath(file);
        if (guest.equals("/")) {
            return null;
        }
        int end = guest.length();
        while (end > 1 && guest.charAt(end - 1) == '/') {
            end--;
        }
        int slash = guest.lastIndexOf('/', end - 1);
        if (slash < 0) {
            return null;
        }
        return slash == 0 ? "/" : guest.substring(0, slash);
    }

    public static File getParentFile(File file) {
        String parent = getParent(file);
        return parent == null ? null : new GuestFile(parent);
    }

    public static boolean isAbsolute(File file) {
        return guestPath(file).startsWith("/");
    }

    public static String getAbsolutePath(File file) {
        String guest = guestPath(file);
        return guest.startsWith("/") ? guest : "/" + guest;
    }

    public static File getAbsoluteFile(File file) {
        return new GuestFile(getAbsolutePath(file));
    }

    public static String getCanonicalPath(File file) {
        return canonical(getAbsolutePath(file));
    }

    public static File getCanonicalFile(File file) {
        return new GuestFile(getCanonicalPath(file));
    }

    public static boolean canRead(File file) {
        Path host = target(file, Vfs.policy());
        return host != null && Files.isReadable(host);
    }

    public static boolean canWrite(File file) {
        Path host = writeTarget(file, Vfs.policy());
        return host != null && Files.isWritable(host);
    }

    public static boolean canExecute(File file) {
        Path host = target(file, Vfs.policy());
        return host != null && Files.isExecutable(host);
    }

    public static boolean exists(File file) {
        Path host = target(file, Vfs.policy());
        return host != null && Files.exists(host);
    }

    public static boolean isDirectory(File file) {
        Path host = target(file, Vfs.policy());
        return host != null && Files.isDirectory(host);
    }

    public static boolean isFile(File file) {
        Path host = target(file, Vfs.policy());
        return host != null && Files.isRegularFile(host);
    }

    /** Host hidden-attribute check; unmapped paths read as visible. */
    public static boolean isHidden(File file) {
        Path host = target(file, Vfs.policy());
        if (host == null) {
            return false;
        }
        try {
            return Files.isHidden(host);
        } catch (IOException e) {
            return false;
        }
    }

    /** Last-modified time in millis, or 0 when unknown. */
    public static long lastModified(File file) {
        Path host = target(file, Vfs.policy());
        if (host == null) {
            return 0L;
        }
        try {
            return Files.getLastModifiedTime(host).toMillis();
        } catch (IOException e) {
            return 0L;
        }
    }

    /** File size in bytes, or 0 when unknown. */
    public static long length(File file) {
        Path host = target(file, Vfs.policy());
        if (host == null) {
            return 0L;
        }
        try {
            return Files.size(host);
        } catch (IOException e) {
            return 0L;
        }
    }

    /** Creates an empty file; false when it already exists. */
    public static boolean createNewFile(File file) throws IOException {
        Path host = requireTarget(file, true);
        try {
            Files.createFile(host);
            return true;
        } catch (FileAlreadyExistsException e) {
            return false;
        }
    }

    /** Deletes the file or empty directory; false on failure. */
    public static boolean delete(File file) {
        Path host = requireTarget(file, true);
        try {
            return Files.deleteIfExists(host);
        } catch (IOException e) {
            return false;
        }
    }

    public static void deleteOnExit(File file) {
        throw new SandboxDenied("File.deleteOnExit is not available in the guest sandbox");
    }

    public static String[] list(File file) {
        return list(file, (FilenameFilter) null);
    }

    /** Entry names accepted by the filter, or null when not a directory. */
    public static String[] list(File file, FilenameFilter filter) {
        Vfs.Policy policy = Vfs.policy();
        Path host = target(file, policy);
        String guest = guestPathOrNull(file, policy);
        if (host == null || guest == null || !Files.isDirectory(host)) {
            return null;
        }
        File dir = new GuestFile(guest);
        try (Stream<Path> entries = Files.list(host)) {
            return entries.map(entry -> entry.toFile().getName())
                    .filter(name -> filter == null || filter.accept(dir, name))
                    .toArray(String[]::new);
        } catch (IOException e) {
            return null;
        }
    }

    public static File[] listFiles(File file) {
        return listFiles(file, (FilenameFilter) null);
    }

    /** Guest files for the names accepted by the filter. */
    public static File[] listFiles(File file, FilenameFilter filter) {
        String[] names = list(file, filter);
        if (names == null) {
            return null;
        }
        String guest = guestPath(file);
        File[] out = new File[names.length];
        for (int i = 0; i < names.length; i++) {
            out[i] = new GuestFile(join(guest, names[i]));
        }
        return out;
    }

    /** Guest files accepted by the filter, or null when not a directory. */
    public static File[] listFiles(File file, FileFilter filter) {
        Vfs.Policy policy = Vfs.policy();
        Path host = target(file, policy);
        String guest = guestPathOrNull(file, policy);
        if (host == null || guest == null || !Files.isDirectory(host)) {
            return null;
        }
        try (Stream<Path> entries = Files.list(host)) {
            return entries.map(entry -> new GuestFile(
                            join(guest, entry.toFile().getName())))
                    .filter(child -> filter == null || filter.accept(child))
                    .toArray(File[]::new);
        } catch (IOException e) {
            return null;
        }
    }

    /** Creates one directory; false when it cannot be created. */
    public static boolean mkdir(File file) {
        Path host = requireTarget(file, true);
        try {
            Files.createDirectory(host);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Creates missing parents; false when the path already exists or fails. */
    public static boolean mkdirs(File file) {
        Path host = requireTarget(file, true);
        if (Files.exists(host)) {
            return false;
        }
        try {
            Files.createDirectories(host);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Moves the file within the guest mounts; false on failure. */
    public static boolean renameTo(File file, File dest) {
        Path source = requireTarget(file, true);
        Path target = requireTarget(dest, true);
        try {
            Files.move(source, target);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Sets the modification time; false on failure. */
    public static boolean setLastModified(File file, long time) {
        Path host = requireTarget(file, true);
        try {
            Files.setLastModifiedTime(host, FileTime.fromMillis(time));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Clears every write bit; false on failure. */
    public static boolean setReadOnly(File file) {
        Path host = requireTarget(file, true);
        try {
            return host.toFile().setReadOnly();
        } catch (RuntimeException e) {
            return false;
        }
    }

    /** Sets or clears the write bit; false on failure. */
    public static boolean setWritable(File file, boolean writable, boolean ownerOnly) {
        Path host = requireTarget(file, true);
        try {
            return host.toFile().setWritable(writable, ownerOnly);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean setWritable(File file, boolean writable) {
        return setWritable(file, writable, true);
    }

    /** Sets or clears the read bit; false on failure. */
    public static boolean setReadable(File file, boolean readable, boolean ownerOnly) {
        Path host = requireTarget(file, true);
        try {
            return host.toFile().setReadable(readable, ownerOnly);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean setReadable(File file, boolean readable) {
        return setReadable(file, readable, true);
    }

    /** Sets or clears the execute bit; false on failure. */
    public static boolean setExecutable(File file, boolean executable, boolean ownerOnly) {
        Path host = requireTarget(file, true);
        try {
            return host.toFile().setExecutable(executable, ownerOnly);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean setExecutable(File file, boolean executable) {
        return setExecutable(file, executable, true);
    }

    public static long getTotalSpace(File file) {
        return space(file, 0);
    }

    public static long getFreeSpace(File file) {
        return space(file, 1);
    }

    public static long getUsableSpace(File file) {
        return space(file, 2);
    }

    private static long space(File file, int kind) {
        Path host = target(file, Vfs.policy());
        if (host == null) {
            return 0L;
        }
        try {
            return switch (kind) {
                case 0 -> Files.getFileStore(host).getTotalSpace();
                case 1 -> Files.getFileStore(host).getUnallocatedSpace();
                default -> Files.getFileStore(host).getUsableSpace();
            };
        } catch (IOException e) {
            return 0L;
        }
    }

    public static int compareTo(File file, File other) {
        return guestPath(file).compareTo(guestPath(other));
    }

    /** Guest-path equality between two {@code File} values. */
    // Static bridge for File.equals(Object): file is the receiver.
    @SuppressWarnings("PMD.SuspiciousEqualsMethodName")
    public static boolean equals(File file, Object other) {
        if (!(other instanceof File that)) {
            return false;
        }
        Vfs.Policy policy = Vfs.policy();
        String left = guestPathOrNull(file, policy);
        String right = guestPathOrNull(that, policy);
        return left != null && left.equals(right);
    }

    public static int hashCode(File file) {
        String guest = guestPathOrNull(file, Vfs.policy());
        return guest == null ? 0 : guest.hashCode();
    }

    public static String toString(File file) {
        String guest = guestPathOrNull(file, Vfs.policy());
        return guest == null ? "<outside guest filesystem>" : guest;
    }

    public static Path toPath(File file) {
        throw new SandboxDenied("File.toPath is not available in the guest sandbox");
    }

    /** Mirrors {@code java.io.File}; the acronym is the Java API name. */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public static URI toURI(File file) {
        throw new SandboxDenied("File.toURI is not available in the guest sandbox");
    }

    /** Mirrors {@code java.io.File}; the acronym is the Java API name. */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public static URL toURL(File file) {
        throw new SandboxDenied("File.toURL is not available in the guest sandbox");
    }

    private static String join(String parent, String child) {
        if (parent.isEmpty()) {
            return child;
        }
        if (parent.equals("/")) {
            return "/" + child;
        }
        return parent.endsWith("/") ? parent + child : parent + "/" + child;
    }

    /** Lexical guest canonicalization; never consults the host working dir. */
    private static String canonical(String path) {
        String guest = path.replace('\\', '/');
        boolean absolute = guest.startsWith("/");
        Deque<String> parts = new ArrayDeque<>();
        for (String segment : guest.split("/")) {
            if (segment.isEmpty() || segment.equals(".")) {
                continue;
            }
            if (segment.equals("..")) {
                if (!parts.isEmpty()) {
                    parts.removeLast();
                } else if (!absolute) {
                    parts.addLast("..");
                }
            } else {
                parts.addLast(segment);
            }
        }
        String body = String.join("/", parts);
        return absolute ? "/" + body : body;
    }
}
