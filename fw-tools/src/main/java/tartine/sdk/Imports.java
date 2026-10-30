package tartine.sdk;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Import cleanup: rewrites fully-qualified type names to simple names plus
 * imports, keeping the fully-qualified form only where a simple name would be
 * ambiguous.
 *
 * <p>Two callers share this transform: {@link SkeletonGen} post-processes
 * generated files so regenerated skeletons follow the convention, and the
 * {@code fwShorten} Gradle task runs the same pass over the committed
 * sources.
 *
 * <p>The transform is text-preserving outside the rewritten type names and
 * the import block. Comments, string/char literals, and text blocks are never
 * touched, and {@code package}/{@code import} lines are never scanned.
 *
 * <p>Rules (AGENTS.md / docs/architecture.md): a type in the current package
 * or {@code java.lang} needs no import; every other type gets a single-type
 * import unless its simple name is already taken by the file's own type, a
 * nested type declared in the file, another import, a same-package type, or a
 * different type in the same file. Chains keep the nested suffix
 * ({@code android.graphics.Paint.Style} becomes {@code Paint.Style}).
 */
public final class Imports {
    private Imports() {
    }

    /**
     * The known top-level types, keyed by fully-qualified source name. Nested
     * types are folded into their top-level owner: the resolver only needs to
     * know where a chain's type prefix ends.
     */
    public static final class Universe {
        private final Set<String> topLevel = new HashSet<>();
        private final Map<String, Set<String>> byPackage = new HashMap<>();

        /** Adds one top-level type; nested binary names fold to their owner. */
        public Universe addTopLevel(String fqn) {
            if (fqn == null || fqn.isEmpty()) {
                return this;
            }
            String name = fqn;
            int dollar = name.indexOf('$');
            if (dollar >= 0) {
                name = name.substring(0, dollar);
            }
            int dot = name.lastIndexOf('.');
            String pkg = dot < 0 ? "" : name.substring(0, dot);
            String simple = dot < 0 ? name : name.substring(dot + 1);
            if (simple.isEmpty() || !Character.isJavaIdentifierStart(simple.charAt(0))) {
                return this;
            }
            if (topLevel.add(name)) {
                byPackage.computeIfAbsent(pkg, k -> new HashSet<>()).add(simple);
            }
            return this;
        }

        /** Adds every class in a parsed {@code android.jar} index. */
        public Universe addIndex(FwIndex index) {
            for (FwIndex.FwClass cls : index.classes()) {
                String descriptor = cls.descriptor;
                if (descriptor.startsWith("L") && descriptor.endsWith(";")) {
                    descriptor = descriptor.substring(1, descriptor.length() - 1);
                }
                addTopLevel(descriptor.replace('/', '.'));
            }
            return this;
        }

        /** Adds the top-level classes in a jar (fast: entry names only). */
        public Universe addJar(Path jar) throws IOException {
            try (ZipFile zip = new ZipFile(jar.toFile())) {
                Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (!name.endsWith(".class") || name.startsWith("META-INF/versions/")
                            || name.equals("module-info.class")
                            || name.equals("package-info.class")
                            || name.endsWith("/package-info.class")) {
                        continue;
                    }
                    String binary = name.substring(0, name.length() - ".class".length());
                    int dollar = binary.indexOf('$');
                    if (dollar >= 0) {
                        binary = binary.substring(0, dollar);
                    }
                    addTopLevel(binary.replace('/', '.'));
                }
            }
            return this;
        }

        /** Adds the top-level types declared under one source root. */
        public Universe addSourceRoot(Path root) throws IOException {
            if (!Files.isDirectory(root)) {
                return this;
            }
            try (Stream<Path> walk = Files.walk(root)) {
                walk.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                    String fileName = path.getFileName().toString();
                    if (fileName.equals("module-info.java") || fileName.equals("package-info.java")) {
                        return;
                    }
                    try {
                        String source = Files.readString(path, StandardCharsets.UTF_8);
                        String pkg = header(source).pkg();
                        String simple = fileName.substring(0, fileName.length() - ".java".length());
                        addTopLevel(pkg.isEmpty() ? simple : pkg + "." + simple);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            } catch (UncheckedIOException e) {
                throw e.getCause();
            }
            return this;
        }

        /** Adds every top-level type of the running JDK ({@code jrt:}). */
        public Universe addJdk() {
            try {
                FileSystem jrt = FileSystems.getFileSystem(URI.create("jrt:/"));
                Path modules = jrt.getPath("/modules");
                try (DirectoryStream<Path> dirs = Files.newDirectoryStream(modules)) {
                    for (Path module : dirs) {
                        try (Stream<Path> walk = Files.walk(module)) {
                            walk.filter(path -> path.toString().endsWith(".class"))
                                    .forEach(path -> addJdkClass(module, path));
                        }
                    }
                }
            } catch (IOException | RuntimeException e) {
                // Not a JDK image (or a restricted runtime): java.* shortening
                // degrades to fully-qualified, which is still correct.
            }
            return this;
        }

        private void addJdkClass(Path module, Path path) {
            Path relative = module.relativize(path);
            String binary = relative.toString().replace(File.separatorChar, '.')
                    .replace('/', '.');
            binary = binary.substring(0, binary.length() - ".class".length());
            int dollar = binary.indexOf('$');
            if (dollar >= 0) {
                return; // nested: the top-level owner is scanned separately
            }
            if (binary.equals("module-info") || binary.equals("package-info")
                    || binary.endsWith(".package-info")) {
                return;
            }
            addTopLevel(binary);
        }

        /** Adds the top-level classes on the current JVM classpath. */
        public Universe addClasspath() {
            String classpath = System.getProperty("java.class.path", "");
            for (String entry : classpath.split(Pattern.quote(File.pathSeparator))) {
                if (entry.isEmpty()) {
                    continue;
                }
                Path path = Paths.get(entry);
                try {
                    if (Files.isDirectory(path)) {
                        addClassDirectory(path);
                    } else if (entry.endsWith(".jar") && Files.isRegularFile(path)) {
                        addJar(path);
                    }
                } catch (IOException e) {
                    // A missing/opaque classpath entry only loses shortening.
                }
            }
            return this;
        }

        private void addClassDirectory(Path root) throws IOException {
            try (Stream<Path> walk = Files.walk(root)) {
                walk.filter(path -> path.toString().endsWith(".class")).forEach(path -> {
                    Path relative = root.relativize(path);
                    String binary = relative.toString().replace(File.separatorChar, '.');
                    binary = binary.substring(0, binary.length() - ".class".length());
                    int dollar = binary.indexOf('$');
                    if (dollar >= 0) {
                        binary = binary.substring(0, dollar);
                    }
                    if (!binary.equals("module-info") && !binary.equals("package-info")
                            && !binary.endsWith(".package-info")) {
                        addTopLevel(binary);
                    }
                });
            }
        }

        /** Longest top-level type prefix of {@code chain}, or null. */
        public String longestTopLevel(String chain) {
            String candidate = chain;
            while (candidate.indexOf('.') >= 0) {
                if (topLevel.contains(candidate)) {
                    return candidate;
                }
                candidate = candidate.substring(0, candidate.lastIndexOf('.'));
            }
            return null;
        }

        /** True when {@code simple} is a top-level type in {@code pkg}. */
        public boolean hasType(String pkg, String simple) {
            Set<String> names = byPackage.get(pkg);
            return names != null && names.contains(simple);
        }
    }

    /** Rewrites one compilation unit; {@code fileNameSimple} is its type name. */
    public static String shorten(String source, String fileNameSimple, Universe universe) {
        Header header = header(source);
        String body = source.substring(header.bodyStart());
        Scan scan = scan(body);

        Map<String, String> importedTypes = new LinkedHashMap<>();
        Set<String> occupied = new HashSet<>();
        Set<String> wildcardPackages = new HashSet<>();
        for (ImportDecl imp : header.imports()) {
            if (imp.isStatic()) {
                String last = lastSegment(imp.name());
                if (!last.equals("*")) {
                    occupied.add(last);
                }
            } else if (imp.name().endsWith(".*")) {
                wildcardPackages.add(imp.name().substring(0, imp.name().length() - 2));
            } else {
                String simple = lastSegment(imp.name());
                importedTypes.putIfAbsent(simple, imp.name());
                occupied.add(simple);
            }
        }

        String selfType = header.pkg().isEmpty()
                ? fileNameSimple : header.pkg() + "." + fileNameSimple;
        List<Resolved> resolved = new ArrayList<>();
        Map<String, Set<String>> wanted = new HashMap<>();
        for (Chain chain : scan.chains()) {
            String top = universe.longestTopLevel(chain.text());
            if (top == null) {
                continue;
            }
            String simple = lastSegment(top);
            String suffix = chain.text().substring(top.length());
            boolean samePackage = packageOf(top).equals(header.pkg());
            boolean javaLang = packageOf(top).equals("java.lang");
            String existing = importedTypes.get(simple);
            boolean blocked = false;
            if (existing != null && !existing.equals(top)) {
                blocked = true;
            } else if (scan.declared().contains(simple) && !top.equals(selfType)) {
                blocked = true;
            } else if (samePackage || javaLang) {
                // No import: the name must not be shadowed by a same-package
                // type (java.lang only) or by an on-demand import.
                if (javaLang && universe.hasType(header.pkg(), simple)) {
                    blocked = true;
                }
                for (String wildcard : wildcardPackages) {
                    if (!wildcard.equals(packageOf(top)) && universe.hasType(wildcard, simple)) {
                        blocked = true;
                        break;
                    }
                }
            } else if (existing == null) {
                if (universe.hasType(header.pkg(), simple) || occupied.contains(simple)) {
                    blocked = true;
                }
            }
            if (blocked) {
                continue;
            }
            boolean needsImport = !samePackage && !javaLang && existing == null;
            resolved.add(new Resolved(chain, top, simple, suffix, needsImport));
            if (needsImport) {
                wanted.computeIfAbsent(simple, k -> new HashSet<>()).add(top);
            }
        }

        // Two different types with the same simple name in one file: keep both
        // fully qualified; only unambiguous imports are added.
        Set<String> conflicting = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : wanted.entrySet()) {
            if (entry.getValue().size() > 1) {
                conflicting.add(entry.getKey());
            }
        }

        List<Resolved> kept = new ArrayList<>();
        for (Resolved r : resolved) {
            if (!(r.needsImport() && conflicting.contains(r.simple()))) {
                kept.add(r);
            }
        }

        StringBuilder rewritten = new StringBuilder(body);
        for (int i = kept.size() - 1; i >= 0; i--) {
            Resolved r = kept.get(i);
            rewritten.replace(r.chain().start(), r.chain().end(), r.simple() + r.suffix());
        }

        Set<String> plain = new TreeSet<>();
        Set<String> statics = new TreeSet<>();
        for (ImportDecl imp : header.imports()) {
            if (imp.isStatic()) {
                statics.add(imp.name());
            } else {
                plain.add(imp.name());
            }
        }
        for (Resolved r : kept) {
            if (r.needsImport()) {
                plain.add(r.top());
            }
        }

        String sep = source.contains("\r\n") ? "\r\n" : "\n";
        StringBuilder importsOut = new StringBuilder();
        for (String name : plain) {
            importsOut.append("import ").append(name).append(';').append(sep);
        }
        if (!plain.isEmpty() && !statics.isEmpty()) {
            importsOut.append(sep);
        }
        for (String name : statics) {
            importsOut.append("import static ").append(name).append(';').append(sep);
        }

        String tail = stripLeadingBlankLines(rewritten.toString());
        String prefix = source.substring(0, header.prefixEnd());
        StringBuilder out = new StringBuilder();
        out.append(prefix);
        if (!prefix.isEmpty()) {
            if (!endsWithNewline(prefix)) {
                out.append(sep);
            }
            if (importsOut.length() > 0 || !tail.isEmpty()) {
                out.append(sep);
            }
        }
        if (importsOut.length() > 0) {
            out.append(importsOut);
            if (!tail.isEmpty()) {
                out.append(sep);
            }
        }
        out.append(tail);
        return out.toString();
    }

    /**
     * One-time (and repeatable) pass over committed sources. Universe sources:
     * the local {@code android.jar}, the JDK, the JVM classpath, and the given
     * roots. CLI: {@code Imports <sourceRoot>... [--check]}.
     */
    public static void main(String[] args) throws IOException {
        List<Path> roots = new ArrayList<>();
        boolean check = false;
        for (String arg : args) {
            if (arg.equals("--check")) {
                check = true;
            } else {
                roots.add(Paths.get(arg));
            }
        }
        if (roots.isEmpty()) {
            throw new IllegalArgumentException("usage: Imports <sourceRoot>... [--check]");
        }
        Universe universe = new Universe().addJdk().addClasspath();
        Path jar = Sdk.findJar(Sdk.TARGET_API);
        if (jar != null) {
            universe.addJar(jar);
        }
        for (Path root : roots) {
            universe.addSourceRoot(root);
        }

        int files = 0;
        int changed = 0;
        for (Path root : roots) {
            if (!Files.isDirectory(root)) {
                continue;
            }
            List<Path> javaFiles;
            try (Stream<Path> walk = Files.walk(root)) {
                javaFiles = walk.filter(path -> path.toString().endsWith(".java"))
                        .filter(path -> !path.getFileName().toString().equals("module-info.java"))
                        .sorted()
                        .toList();
            }
            for (Path file : javaFiles) {
                files++;
                String source = Files.readString(file, StandardCharsets.UTF_8);
                String fileName = file.getFileName().toString();
                String simple = fileName.substring(0, fileName.length() - ".java".length());
                String shortened = shorten(source, simple, universe);
                if (!shortened.equals(source)) {
                    changed++;
                    if (check) {
                        System.out.println("would rewrite: " + file);
                    } else {
                        Files.writeString(file, shortened, StandardCharsets.UTF_8);
                        System.out.println("rewrote: " + file);
                    }
                }
            }
        }
        System.out.println((check ? "would rewrite " : "rewrote ") + changed
                + " of " + files + " files");
        if (check && changed > 0) {
            System.exit(1);
        }
    }

    private record Chain(int start, int end, String text) {
    }

    private record Resolved(Chain chain, String top, String simple, String suffix,
            boolean needsImport) {
    }

    private record ImportDecl(boolean isStatic, String name) {
    }

    private record Header(String pkg, int prefixEnd, int bodyStart, List<ImportDecl> imports) {
    }

    private record Scan(List<Chain> chains, Set<String> declared) {
    }

    /** Splits off the package/import header (comments preserved before it). */
    private static Header header(String source) {
        int length = source.length();
        int pos = 0;
        String pkg = "";
        int packageLineEnd = -1;
        List<ImportDecl> imports = new ArrayList<>();
        int firstImportLineStart = -1;
        int lastImportLineEnd = -1;
        while (true) {
            pos = skipTrivia(source, pos);
            if (pos >= length) {
                break;
            }
            if (startsWithWord(source, pos, "package")) {
                int cursor = skipTrivia(source, pos + "package".length());
                int start = cursor;
                while (cursor < length && (Character.isJavaIdentifierPart(source.charAt(cursor))
                        || source.charAt(cursor) == '.')) {
                    cursor++;
                }
                pkg = source.substring(start, cursor);
                cursor = skipTrivia(source, cursor);
                if (cursor < length && source.charAt(cursor) == ';') {
                    cursor++;
                }
                int end = endOfLine(source, cursor);
                if (packageLineEnd < 0) {
                    packageLineEnd = end;
                }
                pos = end;
                continue;
            }
            if (startsWithWord(source, pos, "import")) {
                int lineStart = lineStart(source, pos);
                int cursor = skipTrivia(source, pos + "import".length());
                boolean isStatic = false;
                if (startsWithWord(source, cursor, "static")) {
                    isStatic = true;
                    cursor = skipTrivia(source, cursor + "static".length());
                }
                int start = cursor;
                while (cursor < length && (Character.isJavaIdentifierPart(source.charAt(cursor))
                        || source.charAt(cursor) == '.' || source.charAt(cursor) == '*')) {
                    cursor++;
                }
                String name = source.substring(start, cursor);
                cursor = skipTrivia(source, cursor);
                if (cursor < length && source.charAt(cursor) == ';') {
                    cursor++;
                }
                int end = endOfLine(source, cursor);
                imports.add(new ImportDecl(isStatic, name));
                if (firstImportLineStart < 0) {
                    firstImportLineStart = lineStart;
                }
                lastImportLineEnd = end;
                pos = end;
                continue;
            }
            break;
        }
        int prefixEnd;
        int bodyStart;
        if (packageLineEnd >= 0) {
            prefixEnd = packageLineEnd;
            bodyStart = lastImportLineEnd >= 0 ? lastImportLineEnd : packageLineEnd;
        } else if (lastImportLineEnd >= 0) {
            prefixEnd = firstImportLineStart;
            bodyStart = lastImportLineEnd;
        } else {
            prefixEnd = 0;
            bodyStart = 0;
        }
        return new Header(pkg, prefixEnd, bodyStart, imports);
    }

    /** Lexer-aware chain and declaration scan (comments/literals skipped). */
    private static Scan scan(String source) {
        List<Chain> chains = new ArrayList<>();
        Set<String> declared = new HashSet<>();
        int length = source.length();
        int pos = 0;
        while (pos < length) {
            char c = source.charAt(pos);
            if (c == '/' && pos + 1 < length) {
                if (source.charAt(pos + 1) == '/') {
                    pos = skipLineComment(source, pos);
                    continue;
                }
                if (source.charAt(pos + 1) == '*') {
                    pos = skipBlockComment(source, pos);
                    continue;
                }
            }
            if (c == '"') {
                if (pos + 2 < length && source.charAt(pos + 1) == '"'
                        && source.charAt(pos + 2) == '"') {
                    pos = skipTextBlock(source, pos);
                } else {
                    pos = skipString(source, pos);
                }
                continue;
            }
            if (c == '\'') {
                pos = skipChar(source, pos);
                continue;
            }
            if (Character.isJavaIdentifierStart(c)) {
                int start = pos;
                pos = skipIdentifier(source, pos);
                int end = pos;
                int cursor = pos;
                while (cursor < length && source.charAt(cursor) == '.' && cursor + 1 < length
                        && Character.isJavaIdentifierStart(source.charAt(cursor + 1))) {
                    cursor = skipIdentifier(source, cursor + 1);
                    end = cursor;
                }
                if (end > pos) {
                    chains.add(new Chain(start, end, source.substring(start, end)));
                } else {
                    String word = source.substring(start, pos);
                    if (word.equals("class") || word.equals("interface")
                            || word.equals("enum") || word.equals("record")) {
                        int nameStart = skipTrivia(source, pos);
                        if (nameStart < length
                                && Character.isJavaIdentifierStart(source.charAt(nameStart))) {
                            int nameEnd = skipIdentifier(source, nameStart);
                            declared.add(source.substring(nameStart, nameEnd));
                        }
                    }
                }
                pos = end;
                continue;
            }
            pos++;
        }
        return new Scan(chains, declared);
    }

    private static int skipIdentifier(String source, int pos) {
        while (pos < source.length() && Character.isJavaIdentifierPart(source.charAt(pos))) {
            pos++;
        }
        return pos;
    }

    private static int skipTrivia(String source, int pos) {
        int length = source.length();
        while (pos < length) {
            char c = source.charAt(pos);
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f'
                    || c == '\uFEFF') {
                pos++;
                continue;
            }
            if (c == '/' && pos + 1 < length && source.charAt(pos + 1) == '/') {
                pos = skipLineComment(source, pos);
                continue;
            }
            if (c == '/' && pos + 1 < length && source.charAt(pos + 1) == '*') {
                pos = skipBlockComment(source, pos);
                continue;
            }
            break;
        }
        return pos;
    }

    private static int skipLineComment(String source, int pos) {
        int length = source.length();
        pos += 2;
        while (pos < length && source.charAt(pos) != '\n' && source.charAt(pos) != '\r') {
            pos++;
        }
        return pos;
    }

    private static int skipBlockComment(String source, int pos) {
        int end = source.indexOf("*/", pos + 2);
        return end < 0 ? source.length() : end + 2;
    }

    private static int skipString(String source, int pos) {
        int length = source.length();
        pos++;
        while (pos < length) {
            char c = source.charAt(pos++);
            if (c == '\\' && pos < length) {
                pos++;
            } else if (c == '"') {
                return pos;
            }
        }
        return pos;
    }

    private static int skipChar(String source, int pos) {
        int length = source.length();
        pos++;
        while (pos < length) {
            char c = source.charAt(pos++);
            if (c == '\\' && pos < length) {
                pos++;
            } else if (c == '\'') {
                return pos;
            }
        }
        return pos;
    }

    private static int skipTextBlock(String source, int pos) {
        int length = source.length();
        pos += 3;
        while (pos < length) {
            int end = source.indexOf("\"\"\"", pos);
            if (end < 0) {
                return length;
            }
            int backslashes = 0;
            for (int i = end - 1; i >= 0 && source.charAt(i) == '\\'; i--) {
                backslashes++;
            }
            if (backslashes % 2 == 0) {
                return end + 3;
            }
            pos = end + 3;
        }
        return length;
    }

    private static boolean startsWithWord(String source, int pos, String word) {
        if (!source.startsWith(word, pos)) {
            return false;
        }
        int end = pos + word.length();
        return end >= source.length() || !Character.isJavaIdentifierPart(source.charAt(end));
    }

    private static int lineStart(String source, int pos) {
        while (pos > 0 && source.charAt(pos - 1) != '\n' && source.charAt(pos - 1) != '\r') {
            pos--;
        }
        return pos;
    }

    private static int endOfLine(String source, int pos) {
        int length = source.length();
        while (pos < length && source.charAt(pos) != '\n' && source.charAt(pos) != '\r') {
            pos++;
        }
        if (pos < length) {
            if (source.charAt(pos) == '\r' && pos + 1 < length && source.charAt(pos + 1) == '\n') {
                pos += 2;
            } else {
                pos++;
            }
        }
        return pos;
    }

    private static String stripLeadingBlankLines(String text) {
        int pos = 0;
        while (pos < text.length()) {
            char c = text.charAt(pos);
            if (c == ' ' || c == '\t') {
                int end = pos;
                while (end < text.length() && (text.charAt(end) == ' ' || text.charAt(end) == '\t')) {
                    end++;
                }
                if (end < text.length() && (text.charAt(end) == '\n' || text.charAt(end) == '\r')) {
                    pos = end;
                    continue;
                }
                break;
            }
            if (c == '\r' || c == '\n') {
                pos++;
                continue;
            }
            break;
        }
        return text.substring(pos);
    }

    private static boolean endsWithNewline(String text) {
        return text.endsWith("\n") || text.endsWith("\r");
    }

    private static String lastSegment(String name) {
        int dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(dot + 1);
    }

    private static String packageOf(String fqn) {
        int dot = fqn.lastIndexOf('.');
        return dot < 0 ? "" : fqn.substring(0, dot);
    }
}
