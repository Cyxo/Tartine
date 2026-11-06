package tartine.guest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.GuestLinks;
import tartine.runtime.Vfs;

/**
 * Behavioral pins for the stream facade: file-backed stream, reader, writer,
 * {@code PrintStream}, and {@code PrintWriter} constructors open the
 * Vfs-mapped host path, honor append, and refuse read-only or escaped paths
 * before the host stream opens.
 */
class GuestFileStreamsTest {
    @TempDir
    Path root;

    private String home;

    @BeforeEach
    void installHome() {
        home = System.getProperty("tartine.home");
        System.setProperty("tartine.home", root.toString());
        Vfs.resetCard();
    }

    @AfterEach
    void restoreHome() {
        if (home == null) {
            System.clearProperty("tartine.home");
        } else {
            System.setProperty("tartine.home", home);
        }
        Vfs.resetCard();
    }

    @Test
    void streamsReadAndWriteThroughTheVfs() throws Exception {
        GuestFile dir = new GuestFile("/data/data/com.example/files");
        assertTrue(dir.mkdirs());
        GuestFile file = new GuestFile(dir, "s.bin");
        try (FileOutputStream out = new GuestFileOutputStream(file)) {
            out.write(7);
        }
        Path host = root.resolve("data").resolve("com.example")
                .resolve("files").resolve("s.bin");
        assertArrayEquals(new byte[] {7}, Files.readAllBytes(host));

        try (FileInputStream in = new GuestFileInputStream(file)) {
            assertEquals(7, in.read());
            assertEquals(-1, in.read());
        }
    }

    @Test
    void appendFlagIsHonored() throws Exception {
        GuestFile file = new GuestFile("/sdcard/Documents/append.txt");
        assertTrue(file.getParentFile().mkdirs());
        try (FileWriter writer = new GuestFileWriter(file)) {
            writer.write("a");
        }
        try (FileWriter writer = new GuestFileWriter(file, true)) {
            writer.write("b");
        }
        assertEquals("ab", Files.readString(
                root.resolve("sdcard").resolve("Documents")
                        .resolve("append.txt")));
    }

    @Test
    void readersAndPrintWritersMapTheirFiles() throws Exception {
        GuestFile file = new GuestFile("/sdcard/Documents/note.txt");
        assertTrue(file.getParentFile().mkdirs());
        try (FileWriter writer = new GuestFileWriter(file)) {
            writer.write("hello");
        }
        try (FileReader reader = new GuestFileReader(file)) {
            char[] chars = new char[5];
            assertEquals(5, reader.read(chars));
            assertEquals("hello", new String(chars));
        }

        Path host = root.resolve("sdcard").resolve("Documents")
                .resolve("note.txt");
        try (PrintWriter writer = new GuestPrintWriter(file)) {
            writer.print("hi");
        }
        assertEquals("hi", Files.readString(host));

        try (PrintStream stream = new GuestPrintStream(file)) {
            stream.print("yo");
        }
        assertEquals("yo", Files.readString(host));
    }

    @Test
    void stringConstructorsMapGuestPaths() throws Exception {
        GuestFile dir = new GuestFile("/sdcard/Documents");
        assertTrue(dir.mkdirs());

        try (FileWriter writer = new GuestFileWriter(
                "/sdcard/Documents/s.txt")) {
            writer.write("x");
        }
        try (FileReader reader = new GuestFileReader(
                "/sdcard/Documents/s.txt")) {
            assertEquals('x', reader.read());
        }
        try (PrintWriter writer = new GuestPrintWriter(
                "/sdcard/Documents/p.txt")) {
            writer.print("p");
        }
        try (PrintStream stream = new GuestPrintStream(
                new GuestFile("/sdcard/Documents/q.txt"))) {
            stream.print("q");
        }
        try (PrintStream stream = new GuestPrintStream(
                "/sdcard/Documents/r.txt")) {
            stream.print("r");
        }
        Path documents = root.resolve("sdcard").resolve("Documents");
        assertEquals("x", Files.readString(documents.resolve("s.txt")));
        assertEquals("p", Files.readString(documents.resolve("p.txt")));
        assertEquals("q", Files.readString(documents.resolve("q.txt")));
        assertEquals("r", Files.readString(documents.resolve("r.txt")));
    }

    @Test
    void wrappingConstructorsStillWork() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream stream = new GuestPrintStream(bytes, true)) {
            stream.print("x");
        }
        assertEquals("x", bytes.toString(StandardCharsets.UTF_8));

        ByteArrayOutputStream more = new ByteArrayOutputStream();
        try (PrintWriter writer = new GuestPrintWriter(more, true,
                StandardCharsets.UTF_8)) {
            writer.print("y");
        }
        assertEquals("y", more.toString(StandardCharsets.UTF_8));
    }

    @Test
    void frameworkReturnedHostFilesReverseMap() throws Exception {
        Path host = root.resolve("data").resolve("pkg").resolve("f.bin");
        Files.createDirectories(Objects.requireNonNull(host.getParent()));
        Files.write(host, new byte[] {1, 2, 3});

        try (FileInputStream in = new GuestFileInputStream(host.toFile())) {
            assertArrayEquals(new byte[] {1, 2, 3}, in.readAllBytes());
        }
        try (FileOutputStream out = new GuestFileOutputStream(
                host.toFile())) {
            out.write(9);
        }
        assertArrayEquals(new byte[] {9}, Files.readAllBytes(host));
    }

    @Test
    void unmappedReadsAnswerFileNotFound() {
        assertThrows(FileNotFoundException.class,
                () -> new GuestFileInputStream("/proc/self/maps"));
        assertThrows(FileNotFoundException.class,
                () -> new GuestFileInputStream(
                        new GuestFile("/proc/self/maps")));
        assertThrows(SandboxDenied.class,
                () -> new GuestFileOutputStream("/proc/self/maps"));
    }

    @Test
    void readOnlyMountRefusesWritesBeforeOpening() throws Exception {
        Path card = root.resolve("card");
        Files.createDirectories(card);
        Files.writeString(card.resolve("keep.txt"), "keep");
        Vfs.configure(new Vfs.Sdcard(card, true));

        try (FileInputStream in = new GuestFileInputStream(
                new GuestFile("/sdcard/keep.txt"))) {
            assertEquals('k', in.read());
        }
        assertThrows(SandboxDenied.class, () -> new GuestFileOutputStream(
                new GuestFile("/sdcard/new.txt")));
        assertThrows(SandboxDenied.class,
                () -> new GuestFileWriter("/sdcard/new.txt"));
        assertFalse(Files.exists(card.resolve("new.txt")));
    }

    @Test
    void symlinkEscapesAreRefused() throws Exception {
        Path card = root.resolve("sdcard");
        Files.createDirectories(card);
        Path outside = root.resolve("outside");
        Files.createDirectories(outside);
        Files.writeString(outside.resolve("secret.txt"), "secret");
        assumeTrue(GuestLinks.create(card.resolve("escape"), outside),
                "symlink/junction creation unavailable");

        GuestFile secret = new GuestFile("/sdcard/escape/secret.txt");
        assertThrows(FileNotFoundException.class,
                () -> new GuestFileInputStream(secret));
        assertThrows(SandboxDenied.class,
                () -> new GuestFileOutputStream(secret));
        assertThrows(SandboxDenied.class, () -> new GuestFileOutputStream(
                new GuestFile("/sdcard/escape/new.txt")));
        assertFalse(Files.exists(outside.resolve("new.txt")));
    }
}
