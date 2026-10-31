package tartine.guest;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * Guest-facing {@link File} whose path is a guest path, never a host path.
 *
 * <p>The translator rewrites {@code new java.io.File(...)} to this class and
 * every instance call on {@code java.io.File} to the matching
 * {@link GuestFiles} bridge. The bridge maps the guest path through
 * {@link tartine.runtime.Vfs}; this class only carries the guest spelling and
 * overrides the inherited path/filesystem surface so a missed call cannot
 * expose the host working directory or an unmapped host location.
 *
 * <p>Relative guest paths are denied by the VFS (there is no guest cwd), but
 * the path getters still answer a deterministic guest spelling instead of
 * consulting the host.
 */
@SuppressWarnings("deprecation") // File.toURL is overridden to deny it
public final class GuestFile extends File {
    private static final long serialVersionUID = 1L;

    private final String guestPath;

    public GuestFile(String pathname) {
        super(pathname);
        this.guestPath = pathname;
    }

    public GuestFile(String parent, String child) {
        this(join(parent, child));
    }

    public GuestFile(File parent, String child) {
        this(join(GuestFiles.guestPath(parent), child));
    }

    /** Guest spelling carried by this file (safe to show to the guest). */
    public String guestPath() {
        return guestPath;
    }

    @Override
    public String getPath() {
        return guestPath;
    }

    @Override
    public String getName() {
        return GuestFiles.getName(this);
    }

    @Override
    public String getParent() {
        return GuestFiles.getParent(this);
    }

    @Override
    public File getParentFile() {
        return GuestFiles.getParentFile(this);
    }

    @Override
    public boolean isAbsolute() {
        return guestPath.startsWith("/");
    }

    @Override
    public String getAbsolutePath() {
        return GuestFiles.getAbsolutePath(this);
    }

    @Override
    public File getAbsoluteFile() {
        return GuestFiles.getAbsoluteFile(this);
    }

    @Override
    public String getCanonicalPath() {
        return GuestFiles.getCanonicalPath(this);
    }

    @Override
    public File getCanonicalFile() {
        return GuestFiles.getCanonicalFile(this);
    }

    @Override
    public boolean canRead() {
        return GuestFiles.canRead(this);
    }

    @Override
    public boolean canWrite() {
        return GuestFiles.canWrite(this);
    }

    @Override
    public boolean canExecute() {
        return GuestFiles.canExecute(this);
    }

    @Override
    public boolean exists() {
        return GuestFiles.exists(this);
    }

    @Override
    public boolean isDirectory() {
        return GuestFiles.isDirectory(this);
    }

    @Override
    public boolean isFile() {
        return GuestFiles.isFile(this);
    }

    @Override
    public boolean isHidden() {
        return GuestFiles.isHidden(this);
    }

    @Override
    public long lastModified() {
        return GuestFiles.lastModified(this);
    }

    @Override
    public long length() {
        return GuestFiles.length(this);
    }

    @Override
    public boolean createNewFile() throws IOException {
        return GuestFiles.createNewFile(this);
    }

    @Override
    public boolean delete() {
        return GuestFiles.delete(this);
    }

    @Override
    public void deleteOnExit() {
        GuestFiles.deleteOnExit(this);
    }

    @Override
    public String[] list() {
        return GuestFiles.list(this);
    }

    @Override
    public String[] list(FilenameFilter filter) {
        return GuestFiles.list(this, filter);
    }

    @Override
    public File[] listFiles() {
        return GuestFiles.listFiles(this);
    }

    @Override
    public File[] listFiles(FilenameFilter filter) {
        return GuestFiles.listFiles(this, filter);
    }

    @Override
    public File[] listFiles(FileFilter filter) {
        return GuestFiles.listFiles(this, filter);
    }

    @Override
    public boolean mkdir() {
        return GuestFiles.mkdir(this);
    }

    @Override
    public boolean mkdirs() {
        return GuestFiles.mkdirs(this);
    }

    @Override
    public boolean renameTo(File dest) {
        return GuestFiles.renameTo(this, dest);
    }

    @Override
    public boolean setLastModified(long time) {
        return GuestFiles.setLastModified(this, time);
    }

    @Override
    public boolean setReadOnly() {
        return GuestFiles.setReadOnly(this);
    }

    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        return GuestFiles.setWritable(this, writable, ownerOnly);
    }

    @Override
    public boolean setWritable(boolean writable) {
        return GuestFiles.setWritable(this, writable);
    }

    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        return GuestFiles.setReadable(this, readable, ownerOnly);
    }

    @Override
    public boolean setReadable(boolean readable) {
        return GuestFiles.setReadable(this, readable);
    }

    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        return GuestFiles.setExecutable(this, executable, ownerOnly);
    }

    @Override
    public boolean setExecutable(boolean executable) {
        return GuestFiles.setExecutable(this, executable);
    }

    @Override
    public long getTotalSpace() {
        return GuestFiles.getTotalSpace(this);
    }

    @Override
    public long getFreeSpace() {
        return GuestFiles.getFreeSpace(this);
    }

    @Override
    public long getUsableSpace() {
        return GuestFiles.getUsableSpace(this);
    }

    @Override
    public Path toPath() {
        return GuestFiles.toPath(this);
    }

    @Override
    public URI toURI() {
        return GuestFiles.toURI(this);
    }

    @Override
    public URL toURL() {
        return GuestFiles.toURL(this);
    }

    @Override
    public int compareTo(File other) {
        return GuestFiles.compareTo(this, other);
    }

    @Override
    public boolean equals(Object other) {
        return GuestFiles.equals(this, other);
    }

    @Override
    public int hashCode() {
        return GuestFiles.hashCode(this);
    }

    @Override
    public String toString() {
        return guestPath;
    }

    private static String join(String parent, String child) {
        if (parent == null) {
            throw new NullPointerException("parent");
        }
        if (child == null) {
            throw new NullPointerException("child");
        }
        if (parent.isEmpty()) {
            return child;
        }
        if (parent.equals("/")) {
            return "/" + child;
        }
        String base = parent.endsWith("/")
                ? parent.substring(0, parent.length() - 1) : parent;
        String name = child.startsWith("/") ? child.substring(1) : child;
        return name.isEmpty() ? base : base + "/" + name;
    }
}
