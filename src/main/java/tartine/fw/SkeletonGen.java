package tartine.fw;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dev-time generator for the {@code framework} module (AGENTS.md "Framework
 * in Java"): reads the LOCAL SDK {@code android.jar}, computes the closure
 * of seed classes (superclasses, implemented interfaces, android types in
 * signatures), and writes skeleton sources with the exact android.jar
 * signatures. Every non-abstract body throws
 * {@code UnsupportedOperationException("Tartine: not implemented:
 * <vmSignature>")} so the gap-driven loop survives.
 *
 * Emission rules:
 * <ul>
 *   <li>Abstract members stay abstract on interfaces and enums; classes
 *       compile concrete (adapter surface: framework-dispatch mirrors
 *       construct them) with gap-naming bodies for abstract members.
 *       Native/synchronized modifiers are dropped.</li>
 *   <li>Nested classes (binary {@code $} names) become {@code static}
 *       nested types inside their top-level source file (documented limit:
 *       {@code $} purely as nesting delimiter; two-plus depth flattens one
 *       level down at the same depth — rare in the published API).</li>
 *   <li>Android enums are emitted as real Java enums: constants are the
 *       ACC_ENUM fields; the implicit {@code (String,int)} ctor is not
 *       re-emitted.</li>
 *   <li>Static finals with ConstantValue keep their values (long/float/
 *       double/string literals; NaN/Infinity use the host class refs).</li>
 *   <li>Private members are skipped (not part of the published API).</li>
 * </ul>
 *
 * Regeneration is additive by default: an existing source file is skipped
 * unless {@code --force} is passed (which overwrites and would discard
 * hand-ported bodies — use deliberately). CLI:
 * {@code SkeletonGen <outDir> [androidJarPath] [--force]}.
 */
public final class SkeletonGen {
    /** Seed descriptors for the closure (grow as classes port). */
    private static final List<String> DEFAULT_SEEDS = List.of(
            "android/graphics/Paint", "android/view/View");

    /** ACC bits (JVMS 4.1/4.5/4.6). */
    private static final int ACC_PUBLIC = 0x0001;
    private static final int ACC_PRIVATE = 0x0002;
    private static final int ACC_PROTECTED = 0x0004;
    private static final int ACC_STATIC = 0x0008;
    private static final int ACC_FINAL = 0x0010;
    private static final int ACC_BRIDGE = 0x0040;
    private static final int ACC_INTERFACE = 0x0200;
    private static final int ACC_ABSTRACT = 0x0400;
    private static final int ACC_NATIVE = 0x0100;
    private static final int ACC_SYNTHETIC = 0x1000;
    private static final int ACC_ENUM = 0x4000;

    public static void main(String[] args) throws IOException {
        Path outDir = null;
        Path jar = null;
        boolean force = false;
        for (String arg : args) {
            if (arg.equals("--force")) {
                force = true;
            } else if (outDir == null) {
                outDir = Paths.get(arg);
            } else if (jar == null) {
                jar = Paths.get(arg);
            }
        }
        if (outDir == null) {
            throw new IllegalArgumentException(
                    "usage: SkeletonGen <outDir> [androidJarPath] [--force]");
        }
        FwIndex index;
        if (jar != null) {
            index = FwIndex.loadJar(jar);
        } else {
            Path found = Sdk.findJar(-1);
            if (found == null) {
                throw new IllegalArgumentException(
                        "no local android.jar (set ANDROID_HOME/ANDROID_SDK_ROOT or pass the path)");
            }
            index = FwIndex.loadJar(found);
        }
        Map<String, FwIndex.FwClass> closure = closure(index, DEFAULT_SEEDS);
        Map<String, List<FwIndex.FwClass>> files = group(closure);
        for (Map.Entry<String, List<FwIndex.FwClass>> file : files.entrySet()) {
            Path src = sourcePath(outDir, file.getKey());
            if (!force && Files.exists(src)) {
                System.out.println("skip (exists): " + file.getKey());
                continue;
            }
            Files.createDirectories(src.getParent());
            Files.writeString(src, emitFile(file.getKey(), file.getValue(), closure),
                    StandardCharsets.UTF_8);
            System.out.println("wrote: " + src);
        }
        System.out.println("classes in closure: " + closure.size()
                + "; files touched: " + files.size());
    }

    /** Seed descriptors -> reachable android.jar classes (super/iface/sig refs). */
    static Map<String, FwIndex.FwClass> closure(FwIndex index, List<String> seeds) {
        Map<String, FwIndex.FwClass> out = new HashMap<>();
        Deque<String> work = new ArrayDeque<>();
        for (String seed : seeds) {
            FwIndex.FwClass found = index.find("L" + seed + ";");
            if (found != null && out.putIfAbsent(seed, found) == null) {
                work.add(seed);
            }
        }
        while (!work.isEmpty()) {
            for (String ref : referenced(out.get(work.poll()))) {
                if (out.containsKey(ref)) {
                    continue;
                }
                // java.* / javax.* stay host classes: the guest's java.*
                // passthrough reflects the local JDK, so generating skeleton
                // shadows would fight it (AGENTS.md "host builtins carry
                // value semantics"). org.xmlpull.v1 IS declared in
                // android.jar and provided nowhere else — follow it.
                if (ref.startsWith("java/") || ref.startsWith("javax/")) {
                    continue;
                }
                FwIndex.FwClass found = index.find("L" + ref + ";");
                if (found != null && out.putIfAbsent(ref, found) == null) {
                    work.add(ref);
                }
                // A nested class needs its full enclosing chain present:
                // one source file per top-level binary name and nested
                // types declared inside their parents, so an orphan
                // reference (Button$Options without Button) would emit an
                // empty file and fail compilation.
                int cut = ref.indexOf('$');
                while (cut >= 0) {
                    String ancestor = ref.substring(0, cut);
                    if (!out.containsKey(ancestor)) {
                        FwIndex.FwClass parent = index.find("L" + ancestor + ";");
                        if (parent == null) {
                            break; // holder absent from the jar; nested can't parent
                        }
                        out.put(ancestor, parent);
                        work.add(ancestor);
                    }
                    cut = ref.indexOf('$', cut + 1);
                }
            }
        }
        return out;
    }

    /** Super/iface + android types referenced from signatures (minus self). */
    private static List<String> referenced(FwIndex.FwClass cls) {
        List<String> refs = new ArrayList<>();
        if (cls.superclass != null && !cls.superclass.equals("Ljava/lang/Object;")) {
            refs.add(strip(cls.superclass));
        }
        for (String iface : cls.interfaces) {
            refs.add(strip(iface));
        }
        for (FwIndex.FwMethod m : cls.methods) {
            for (String p : m.params) {
                collectRefs(p, refs);
            }
            collectRefs(m.ret, refs);
            // Erasure drops generic arguments inside <>; the Signature
            // attribute carries them and only the Signature reveals them
            // (Consumer<ViewTranslationRequest> params).
            if (m.signature != null) {
                collectRefs(m.signature, refs);
            }
        }
        for (FwIndex.FwField f : cls.fields) {
            collectRefs(f.type, refs);
        }
        if (cls.signature != null) {
            collectRefs(cls.signature, refs);
        }
        return refs;
    }

    /**
     * Class references inside a descriptor or generic Signature string:
     * every {@code L} starts one (a name cannot contain {@code L}); a
     * {@code <} body is scanned linearly by the same loop, so nested
     * generic arguments and throws clauses surface without recursion.
     */
    private static void collectRefs(String descriptor, List<String> refs) {
        int i = 0;
        int length = descriptor.length();
        while (i < length) {
            if (descriptor.charAt(i) != 'L') {
                i++;
                continue;
            }
            int nameStart = i + 1;
            int j = nameStart;
            while (j < length && descriptor.charAt(j) != ';'
                    && descriptor.charAt(j) != '<') {
                j++;
            }
            String name = descriptor.substring(nameStart, j);
            if (!name.isEmpty() && !name.contains("<") && name.indexOf('/') >= 0
                    && !refs.contains(name)) {
                refs.add(name);
            }
            i = j <= nameStart ? i + 1 : j;
        }
    }

    private static String strip(String descriptor) {
        return descriptor.substring(1, descriptor.length() - 1);
    }

    /**
     * One source file per top-level binary name (everything before the first
     * {@code $}), classes ordered outer first and shallower nesting before
     * deeper (declaration order preserved inside an ordering group).
     */
    static Map<String, List<FwIndex.FwClass>> group(Map<String, FwIndex.FwClass> closure) {
        Map<String, Map<String, FwIndex.FwClass>> grouped = new LinkedHashMap<>();
        for (FwIndex.FwClass cls : closure.values()) {
            String binary = strip(cls.descriptor);
            String top = binary.indexOf('$') < 0 ? binary : binary.substring(0, binary.indexOf('$'));
            grouped.computeIfAbsent(top, k -> new LinkedHashMap<>()).put(binary, cls);
        }
        Map<String, List<FwIndex.FwClass>> out = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, FwIndex.FwClass>> e : grouped.entrySet()) {
            List<FwIndex.FwClass> members = new ArrayList<>(e.getValue().values());
            members.sort((a, b) -> {
                int d = depth(a) - depth(b);
                if (d != 0) {
                    return d;
                }
                return a.descriptor.compareTo(b.descriptor);
            });
            out.put(e.getKey(), members);
        }
        return out;
    }

    private static int depth(FwIndex.FwClass cls) {
        return strip(cls.descriptor).split("\\$").length;
    }

    private static Path sourcePath(Path outDir, String binaryName) {
        int lastSlash = binaryName.lastIndexOf('/');
        String pkg = lastSlash < 0 ? "" : binaryName.substring(0, lastSlash);
        Path dir = pkg.isEmpty() ? outDir : outDir.resolve(pkg);
        return dir.resolve(binaryName.substring(lastSlash + 1) + ".java");
    }

    /** Emits one source file (package clause + outer class + static nesteds). */
    static String emitFile(String topBinary, List<FwIndex.FwClass> members,
            Map<String, FwIndex.FwClass> closure) {
        int lastSlash = topBinary.lastIndexOf('/');
        String pkg = lastSlash < 0 ? "" : topBinary.substring(0, lastSlash);
        StringBuilder out = new StringBuilder();
        if (!pkg.isEmpty()) {
            out.append("package ").append(pkg.replace('/', '.')).append(";\n\n");
        }
        for (FwIndex.FwClass cls : members) {
            if (depth(cls) == 1) {
                emitClass(out, cls, members, 1, closure);
            }
        }
        return out.toString();
    }

    /** Emits one class (possibly nested) from the closure onto {@code out}. */
    private static void emitClass(StringBuilder out, FwIndex.FwClass cls,
            List<FwIndex.FwClass> members, int level,
            Map<String, FwIndex.FwClass> closure) {
        String indent = indent(level);
        out.append(declHeader(cls, indent)).append(" {\n");
        String parentBinary = strip(cls.descriptor);
        if ((cls.access & ACC_ENUM) != 0) {
            emitEnumBody(out, cls, indent);
        } else {
            emitMembers(out, cls, indent, closure);
            emitAdapterCtor(out, cls, indent);
        }
        for (FwIndex.FwClass member : members) {
            String binary = strip(member.descriptor);
            String parent = binary.lastIndexOf('$') < 0
                    ? null
                    : binary.substring(0, binary.lastIndexOf('$'));
            if (parent != null && parent.equals(parentBinary) && depth(member) == depth(cls) + 1) {
                emitClass(out, member, members, level + 1, closure);
            }
        }
        out.append(indent).append("}\n");
    }

    private static String indent(int level) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < level; i++) {
            b.append("    ");
        }
        return b.toString();
    }

    private static String declHeader(FwIndex.FwClass cls, String indent) {
        StringBuilder b = new StringBuilder(indent);
        if (strip(cls.descriptor).indexOf('$') >= 0) {
            b.append("public static ");
        } else if ((cls.access & ACC_PUBLIC) != 0) {
            b.append("public ");
        }
        if ((cls.access & ACC_INTERFACE) != 0) {
            b.append("interface");
        } else if (isEnum(cls)) {
            b.append("enum");
        } else {
            // Adapter surface: abstract classes compile concrete so the
            // interpreter's framework-dispatch mirrors can construct them.
            // The abstract marker survives in FwIndex for API probing;
            // bodies still name their gap ("not implemented").
            b.append("class");
        }
        // Signature-aware header when the classfile carries a class-level
        // Signature; erasure fallback otherwise.
        String typeParams = null;
        String superSource = null;
        List<String> ifaceSources;
        if (cls.signature != null) {
            ClassSig sig = classSignature(cls.signature);
            typeParams = sig.typeParams;
            superSource = sig.superClass;
            ifaceSources = sig.interfaces;
        } else {
            if ((cls.access & ACC_INTERFACE) == 0 && cls.superclass != null
                    && !cls.superclass.equals("Ljava/lang/Object;")
                    && !cls.superclass.equals("Ljava/lang/Enum;")) {
                superSource = sourceType(cls.superclass);
            }
            ifaceSources = new ArrayList<>();
            for (String iface : cls.interfaces) {
                ifaceSources.add(sourceType(iface));
            }
        }
        b.append(' ').append(simpleName(strip(cls.descriptor)));
        if (typeParams != null) {
            b.append(typeParams);
        }
        if (superSource != null && !superSource.equals("java.lang.Object")
                && !(isEnum(cls) && superSource.startsWith("java.lang.Enum"))) {
            b.append(" extends ").append(superSource);
        }
        if (!ifaceSources.isEmpty()) {
            b.append((cls.access & ACC_INTERFACE) != 0 ? " extends " : " implements ");
            for (int i = 0; i < ifaceSources.size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(ifaceSources.get(i));
            }
        }
        return b.toString();
    }

    /** Parsed class-level generic signature (JVMS 4.7.9.1). */
    private record ClassSig(String typeParams, String superClass, List<String> interfaces) {
    }

    private static ClassSig classSignature(String sig) {
        int[] pos = {0};
        String typeParams = null;
        if (sig.charAt(pos[0]) == '<') {
            typeParams = "<" + typeParamNames(sig, pos) + ">";
        }
        String superClass = sourceTypeSig(sig, pos);
        List<String> interfaces = new ArrayList<>();
        while (pos[0] < sig.length()) {
            interfaces.add(sourceTypeSig(sig, pos));
        }
        return new ClassSig(typeParams, superClass, interfaces);
    }

    /**
     * Collects bare parameter names for "<T:Ljava/lang/Object;>": bounds are
     * consumed for cursor progress but not rendered (skeletons declare bare
     * type parameters; erasure-checked code never reads the bounds).
     */
    private static String typeParamNames(String sig, int[] pos) {
        StringBuilder names = new StringBuilder();
        pos[0]++; // consume '<'
        while (sig.charAt(pos[0]) != '>') {
            int start = pos[0];
            while (sig.charAt(pos[0]) != ':') {
                pos[0]++;
            }
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(sig, start, pos[0]);
            // Class bound (possibly empty, `S::Iface`) then interface bounds.
            // Grammar topology: after every ':' comes either another ':' (an
            // empty class bound) or a full ReferenceTypeSignature — never a
            // bare parameter name.
            while (sig.charAt(pos[0]) == ':') {
                pos[0]++;
                char c = sig.charAt(pos[0]);
                if (c == ':') {
                    continue;
                }
                if (c == 'L' || c == '[' || c == 'T') {
                    sourceTypeSig(sig, pos);
                } else {
                    throw new IllegalArgumentException("bad type parameter: "
                            + sig);
                }
            }
        }
        pos[0]++; // consume '>'
        return names.toString();
    }

    /** Reads one type signature at pos[0], returning source text. */
    private static String sourceTypeSig(String sig, int[] pos) {
        char c = sig.charAt(pos[0]);
        if (c == '[') {
            pos[0]++;
            return sourceTypeSig(sig, pos) + "[]";
        }
        if (c == 'T') {
            pos[0]++;
            int end = sig.indexOf(';', pos[0]);
            String name = sig.substring(pos[0], end);
            pos[0] = end + 1;
            return name;
        }
        if (c == 'L') {
            pos[0]++;
            StringBuilder name = new StringBuilder();
            while (sig.charAt(pos[0]) != '<' && sig.charAt(pos[0]) != ';') {
                name.append(sig.charAt(pos[0]));
                pos[0]++;
            }
            if (sig.charAt(pos[0]) == '<') {
                pos[0]++;
                List<String> args = new ArrayList<>();
                while (sig.charAt(pos[0]) != '>') {
                    args.add(sourceTypeArg(sig, pos));
                }
                pos[0] += 2; // consume '>' and the terminating ';'
                return classNameSource(name) + "<" + String.join(", ", args) + ">";
            }
            pos[0]++;
            return classNameSource(name);
        }
        pos[0]++;
        return switch (c) {
            case 'B' -> "byte";
            case 'C' -> "char";
            case 'D' -> "double";
            case 'F' -> "float";
            case 'I' -> "int";
            case 'J' -> "long";
            case 'S' -> "short";
            case 'Z' -> "boolean";
            case 'V' -> "void";
            default -> throw new IllegalArgumentException("bad signature: " + sig);
        };
    }

    /** TypeArgument: wildcard-prefixed TypeSignature. */
    private static String sourceTypeArg(String sig, int[] pos) {
        // Peek, not consume: the default case hands the SAME position to
        // sourceTypeSig, which must see the 'L'/'['/'T' opening itself.
        return switch (sig.charAt(pos[0])) {
            case '*' -> { pos[0]++; yield "?"; }
            case '+' -> { pos[0]++; yield "? extends " + sourceTypeSig(sig, pos); }
            case '-' -> { pos[0]++; yield "? super " + sourceTypeSig(sig, pos); }
            default -> sourceTypeSig(sig, pos);
        };
    }

    private static String classNameSource(StringBuilder slashed) {
        return slashed.toString().replace('/', '.').replace('$', '.');
    }

    private static boolean isEnum(FwIndex.FwClass cls) {
        return (cls.access & ACC_ENUM) != 0;
    }

    private static void emitMembers(StringBuilder out, FwIndex.FwClass cls,
            String indent, Map<String, FwIndex.FwClass> closure) {
        boolean first = true;
        for (FwIndex.FwMethod f : fillerMethods(cls, closure)) {
            if (skipMember(f.access)) {
                continue;
            }
            if (first) {
                out.append('\n');
                first = false;
            } else {
                out.append('\n');
            }
            emitMethod(out, cls, f, indent);
        }
        for (FwIndex.FwField f : cls.fields) {
            if (skipMember(f.access)) {
                continue;
            }
            if (first) {
                out.append('\n');
                first = false;
            }
            emitField(out, cls, f, indent);
        }
        for (FwIndex.FwMethod m : cls.methods) {
            if (m.name.equals("<clinit>") || skipMember(m.access)) {
                continue;
            }
            if (first) {
                out.append('\n');
                first = false;
            } else {
                out.append('\n');
            }
            if (m.name.equals("<init>")) {
                emitCtor(out, cls, m, indent);
            } else {
                emitMethod(out, cls, m, indent);
            }
        }
    }

    /**
     * Scratch no-arg constructor backing interpreter framework-dispatch
     * mirrors. Only for classes the platform jar does not itself declare
     * {@code ()V} on: guests cannot legally reference such a signature
     * (their compile SDK has none), and reusing a real UOE skeleton
     * constructor would defeat mirror construction. Adapter surface, not
     * SDK surface.
     */
    private static void emitAdapterCtor(StringBuilder out, FwIndex.FwClass cls,
            String indent) {
        // Abstract markers vanish in emitted classes (adapter surface), so
        // only interfaces/enums stay exempt from the mirror constructor.
        if ((cls.access & (ACC_INTERFACE | ACC_ENUM)) != 0) {
            return;
        }
        for (FwIndex.FwMethod m : cls.methods) {
            if (m.name.equals("<init>") && m.params.isEmpty() && m.ret.equals("V")) {
                return; // real ()V exists; the mirror uses it once hosted
            }
        }
        out.append('\n').append(indent)
                .append("    /** Scratch mirror constructor (SkeletonGen). */\n")
                .append(indent).append("    public ")
                .append(simpleName(strip(cls.descriptor))).append("() {\n")
                .append(indent).append("    }\n");
    }

    /**
     * Abstract members a generated concrete class must redeclare. The SDK
     * stub classfiles carry only own-declared members, but the adapter
     * surface compiles concrete: inherited interface and abstract-super
     * members that the published class relied on leaving unimplemented
     * (staying abstract) now need gap-naming bodies in this class. Only
     * members of the android module closure fill; {@code java.*} ancestry
     * stays on the host JDK and its members never shadow here.
     */
    private static List<FwIndex.FwMethod> fillerMethods(FwIndex.FwClass cls,
            Map<String, FwIndex.FwClass> closure) {
        if ((cls.access & (ACC_INTERFACE | ACC_ENUM)) != 0) {
            return Collections.emptyList();
        }
        // Required-abstract accumulator: concrete declarations on the walk
        // satisfy (and cancel) pending requirements; own declarations
        // cancel too (they already re-emit).
        Map<String, FwIndex.FwMethod> required = new LinkedHashMap<>();
        Set<String> satisfied = new LinkedHashSet<>();
        Set<String> visited = new LinkedHashSet<>();
        Deque<String> work = new ArrayDeque<>();
        if (cls.superclass != null && !"Ljava/lang/Object;".equals(cls.superclass)) {
            work.add(cls.superclass);
        }
        for (String iface : cls.interfaces) {
            work.add(iface);
        }
        while (!work.isEmpty()) {
            String desc = work.poll();
            if (!visited.add(desc)) {
                continue;
            }
            // Closure keys are bare binary names ("android/os/Parcelable");
            // supers/interfaces carry full descriptors ("Landroid/os/...;").
            String binary = desc.startsWith("L") && desc.endsWith(";")
                    ? desc.substring(1, desc.length() - 1)
                    : desc;
            FwIndex.FwClass type = closure.get(binary);
            if (type == null) {
                continue; // java.* subtree or outside the closure: host owned
            }
            for (FwIndex.FwMethod m : type.methods) {
                String key = m.name + "(" + String.join("", m.params)
                        + ")" + m.ret;
                if ((m.access & ACC_ABSTRACT) == 0) {
                    satisfied.add(key);
                    required.remove(key);
                } else if (!satisfied.contains(key)) {
                    required.putIfAbsent(key, m);
                }
            }
            if (type.superclass != null
                    && !"Ljava/lang/Object;".equals(type.superclass)) {
                work.add(type.superclass);
            }
            for (String iface : type.interfaces) {
                work.add(iface);
            }
        }
        List<FwIndex.FwMethod> filler = new ArrayList<>();
        for (FwIndex.FwMethod m : required.values()) {
            if (cls.hasMethod(m.name, m.params, m.ret)) {
                continue;
            }
            filler.add(m);
        }
        return filler;
    }

    /** Enum constants first (must be first in source), then other members. */
    private static void emitEnumBody(StringBuilder out, FwIndex.FwClass cls, String indent) {
        StringBuilder constants = new StringBuilder();
        for (FwIndex.FwField f : cls.fields) {
            if ((f.access & ACC_ENUM) != 0) {
                if (constants.length() > 0) {
                    constants.append(", ");
                }
                constants.append(f.name);
            }
        }
        out.append(indent).append("    ").append(constants).append(";\n");
        for (FwIndex.FwMethod m : cls.methods) {
            // Every enum ctor carries the synthetic (String,int) prefix, and
            // the constants clause calls exactly that ctor during <clinit>:
            // re-emitting one with a UOE body would break enum-class init,
            // and extra-param ctors cannot be re-declared without the prefix
            // machinery we do not parse. Guests never <init>-construct enums
            // (constants come from sget), so all enum ctors stay implicit.
            if (m.name.equals("<init>") || m.name.equals("<clinit>") || skipMember(m.access)) {
                continue;
            }
            // values()/valueOf(java.lang.String) are compiler-generated on
            // every enum; redeclaring them is a syntax error.
            if (isEnum(cls) && m.name.equals("values")
                    && m.params.isEmpty() && m.ret.equals("[" + cls.descriptor)) {
                continue;
            }
            if (isEnum(cls) && m.name.equals("valueOf")
                    && m.params.size() == 1
                    && m.params.get(0).equals("Ljava/lang/String;")
                    && m.ret.equals(cls.descriptor)) {
                continue;
            }
            out.append('\n');
            if (m.name.equals("<init>")) {
                emitCtor(out, cls, m, indent);
            } else {
                emitMethod(out, cls, m, indent);
            }
        }
    }

    private static boolean skipMember(int access) {
        if ((access & ACC_SYNTHETIC) != 0 || (access & ACC_BRIDGE) != 0) {
            return true;
        }
        return (access & ACC_PUBLIC) == 0 && (access & ACC_PROTECTED) == 0;
    }

    private static void emitField(StringBuilder out, FwIndex.FwClass cls,
            FwIndex.FwField f, String indent) {
        // Interface fields are implicitly public static final: without a
        // ConstantValue no initializer can be written (values live in the
        // real <clinit>, which skeletons drop), so skip them there.
        Object constant = cls.constants.get(f.name);
        if ((cls.access & ACC_INTERFACE) != 0 && constant == null) {
            return;
        }
        StringBuilder mods = new StringBuilder();
        if ((f.access & ACC_PROTECTED) != 0) {
            mods.append("protected ");
        } else {
            mods.append("public ");
        }
        if ((f.access & ACC_STATIC) != 0) {
            mods.append("static ");
        }
        String type = sourceType(f.type);
        // Keep `final` only for static finals with a ConstantValue: blank
        // finals are illegal in interfaces and unassigned instance finals
        // fail javac against our UOE-only ctor bodies.
        if ((f.access & ACC_STATIC) != 0 && (f.access & ACC_FINAL) != 0
                && constant != null) {
            mods.append("final ");
        }
        if ((f.access & ACC_STATIC) != 0 && (f.access & ACC_FINAL) != 0
                && constant != null) {
            out.append(indent + mods + type + " " + f.name + " = "
                    + constantLiteral(constant) + ";\n");
        } else {
            out.append(indent + mods + type + " " + f.name + ";\n");
        }
    }

    private static void emitCtor(StringBuilder out, FwIndex.FwClass cls,
            FwIndex.FwMethod m, String indent) {
        StringBuilder b = new StringBuilder(indent);
        if ((m.access & ACC_PROTECTED) != 0) {
            b.append("protected ");
        } else {
            b.append("public ");
        }
        b.append(simpleName(strip(cls.descriptor))).append('(')
                .append(paramList(m)).append(')');
        b.append(bodyBlock(sig(cls, m), indent));
        out.append(b);
    }

    private static void emitMethod(StringBuilder out, FwIndex.FwClass cls,
            FwIndex.FwMethod m, String indent) {
        StringBuilder b = new StringBuilder(indent);
        if ((m.access & ACC_PROTECTED) != 0) {
            b.append("protected ");
        } else {
            b.append("public ");
        }
        if ((m.access & ACC_STATIC) != 0) {
            b.append("static ");
        }
        // Enums keep abstract members (constant-specific bodies we cannot
        // emit); for classes the adapter surface rule applies: every member
        // compiles concrete with a gap-naming body, so abstract skeleton
        // classes (ViewGroup...) construct as mirrors.
        boolean abstractMethod = (m.access & ACC_ABSTRACT) != 0
                && (cls.access & (ACC_INTERFACE | ACC_ENUM)) != 0;
        if (abstractMethod) {
            b.append("abstract ");
        } else if ((cls.access & ACC_INTERFACE) != 0 && (m.access & ACC_STATIC) == 0) {
            // Interface instance methods with bodies are default methods.
            b.append("default ");
        } else if ((m.access & ACC_FINAL) != 0) {
            b.append("final ");
        }
        // Method-level generic Signature when present: the descriptor is the
        // erasure and cannot override a generic interface member (ArrayMap's
        // get under Map<K, V>).
        List<String> params = m.params;
        String ret = sourceType(m.ret);
        String typeParams = null;
        if (m.signature != null) {
            MethodSig ms = methodSignature(m.signature);
            typeParams = ms.typeParams;
            params = ms.params;
            ret = ms.ret;
        }
        if (typeParams != null) {
            b.append(typeParams).append(' ');
        }
        b.append(ret).append(' ').append(m.name).append('(');
        if (m.signature != null) {
            b.append(argList(params));
        } else {
            b.append(paramList(m));
        }
        b.append(')');
        if (abstractMethod) {
            b.append(";\n");
        } else {
            b.append(bodyBlock(sig(cls, m), indent));
        }
        out.append(b);
    }

    /** Parsed method-level generic signature (JVMS 4.7.9.1). */
    private record MethodSig(String typeParams, List<String> params, String ret) {
    }

    /** Format: [TypeParameters] ( TypeSignature* ) ReturnSignature ThrowsSignature*. */
    private static MethodSig methodSignature(String sig) {
        int[] pos = {0};
        String typeParams = null;
        if (sig.charAt(pos[0]) == '<') {
            typeParams = "<" + typeParamNames(sig, pos) + ">";
        }
        if (sig.charAt(pos[0]) != '(') {
            throw new IllegalArgumentException("bad method signature: " + sig);
        }
        pos[0]++;
        List<String> params = new ArrayList<>();
        while (sig.charAt(pos[0]) != ')') {
            params.add(sourceTypeSig(sig, pos));
        }
        pos[0]++; // consume ')'
        String ret = sourceTypeSig(sig, pos);
        // Trailing ThrowsSignatures ('^'-prefixed) are intentionally ignored:
        // skeleton sources omit throws clauses.
        return new MethodSig(typeParams, params, ret);
    }

    private static String bodyBlock(String gapSignature, String indent) {
        return " {\n" + indent + "    throw new UnsupportedOperationException("
                + quote("Tartine: not implemented: " + gapSignature)
                + ");\n" + indent + "}\n";
    }

    private static String paramList(FwIndex.FwMethod m) {
        List<String> rendered = new ArrayList<>();
        for (String p : m.params) {
            rendered.add(sourceType(p));
        }
        return argList(rendered);
    }

    /** Composes already-rendered parameter types into the argument list. */
    private static String argList(List<String> renderedParams) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < renderedParams.size(); i++) {
            if (i > 0) {
                b.append(", ");
            }
            b.append(renderedParams.get(i)).append(" arg").append(i);
        }
        return b.toString();
    }

    private static String sig(FwIndex.FwClass cls, FwIndex.FwMethod m) {
        StringBuilder b = new StringBuilder();
        b.append(cls.descriptor).append("->").append(m.name).append('(');
        for (String p : m.params) {
            b.append(p);
        }
        b.append(')').append(m.ret);
        return b.toString();
    }

    private static String simpleName(String binary) {
        int slash = binary.lastIndexOf('/');
        int dollar = binary.lastIndexOf('$');
        String pkgLess = slash < 0 ? binary : binary.substring(slash + 1);
        return dollar < 0 ? pkgLess : pkgLess.substring(pkgLess.lastIndexOf('$') + 1);
    }

    private static String constantLiteral(Object constant) {
        if (constant instanceof Long) {
            return constant + "L";
        }
        if (constant instanceof Float) {
            return floatValue((Float) constant);
        }
        if (constant instanceof Double) {
            return doubleValue((Double) constant);
        }
        if (constant instanceof String) {
            return quote((String) constant);
        }
        if (constant instanceof Integer) {
            return String.valueOf(constant);
        }
        throw new IllegalStateException("bad constant: " + constant.getClass());
    }

    private static String floatValue(float v) {
        if (Float.isNaN(v)) {
            return "Float.NaN";
        }
        if (v == Float.POSITIVE_INFINITY) {
            return "Float.POSITIVE_INFINITY";
        }
        if (v == Float.NEGATIVE_INFINITY) {
            return "Float.NEGATIVE_INFINITY";
        }
        return v + "f";
    }

    private static String doubleValue(double v) {
        if (Double.isNaN(v)) {
            return "Double.NaN";
        }
        if (v == Double.POSITIVE_INFINITY) {
            return "Double.POSITIVE_INFINITY";
        }
        if (v == Double.NEGATIVE_INFINITY) {
            return "Double.NEGATIVE_INFINITY";
        }
        return v + "d";
    }

    private static String quote(String text) {
        StringBuilder out = new StringBuilder("\"");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\\': out.append("\\\\"); break;
                case '"': out.append("\\\""); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (Character.isISOControl(c)) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }
        return out.append('"').toString();
    }

    /** Descriptor -> Java source type ({@code /}->., {@code $}->.). */
    static String sourceType(String descriptor) {
        int arrays = 0;
        int pos = 0;
        while (pos < descriptor.length() && descriptor.charAt(pos) == '[') {
            arrays++;
            pos++;
        }
        String inner = descriptor.substring(pos);
        String base;
        switch (inner) {
            case "I": base = "int"; break;
            case "B": base = "byte"; break;
            case "C": base = "char"; break;
            case "D": base = "double"; break;
            case "F": base = "float"; break;
            case "J": base = "long"; break;
            case "S": base = "short"; break;
            case "Z": base = "boolean"; break;
            case "V": base = "void"; break;
            default:
                if (!inner.startsWith("L")) {
                    throw new IllegalStateException("not a ref: " + descriptor);
                }
                base = strip(inner).replace('/', '.').replace('$', '.');
        }
        StringBuilder out = new StringBuilder(base);
        for (int i = 0; i < arrays; i++) {
            out.append("[]");
        }
        return out.toString();
    }
}
