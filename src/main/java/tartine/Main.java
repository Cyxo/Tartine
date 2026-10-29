package tartine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import tartine.apk.Abi;
import tartine.apk.ApkReader;
import tartine.apk.AppIcon;
import tartine.apk.BinaryXml;
import tartine.dex.Dex;
import tartine.dex.DexClass;
import tartine.dex.DexDump;
import tartine.dex.DexMethod;
import tartine.dex.DexSet;
import tartine.fw.FwIndex;
import tartine.fw.Sdk;
import tartine.gfx.GfxSurface;
import tartine.graphics.DesktopWindow;
import tartine.graphics.RasterPresenter;
import tartine.graphics.WindowIcon;
import tartine.vm.Interpreter;
import tartine.vm.VmException;
import tartine.vm.VmObject;
import tartine.vm.VmThrow;

/**
 * Entry point.
 *
 * <ul>
 * <li>{@code apk <file.apk>} — headless APK summary: manifest, code, libraries.
 * <li>{@code run <file.apk>} — headless probe through the translation loader
 * ({@code tartine.tx}: real JVM bytecode, framework module classes, host JDK),
 * executing the launcher activity's {@code onCreate} until the first gap.
 * <li>{@code gui <file.apk> [timeoutSecs]} — the same translated launch, then
 * shows the activity window on desktop. {@code txrun}/{@code txgui} are kept
 * as aliases.
 * <li>{@code irun <file.apk>} / {@code igui <file.apk> [timeoutSecs]} — the
 * interpreter path, kept for comparisons while the translation path grows.
 * <li>{@code dump <file.dex>} — prints our instruction dump of a dex file.
 * </ul>
 */
public final class Main {
    private Main() {
    }

    /**
     * Frame drawn by the first-draw pass, handed to the desktop window in
     * gui mode ({@code runOnCreate -> ... -> driveDraw} writes it). Staging
     * until Choreographer-driven redraws replace the one-shot handoff.
     */
    private static GfxSurface firstDrawFrame;

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        tartine.config.HostConfig.load();
        // Renderer backend: installed once at boot, before any framework
        // graphics call (text measurement included). The framework resolves
        // it through GfxBackend.get() and never imports Skija.
        tartine.gfx.GfxBackend.install(
                new tartine.gfx.skija.SkijaBackend());
        // Seed the default virtual sdcard (no-op when TARTINE_SDCARD or
        // sdcard.mount already configured an explicit mount).
        tartine.runtime.Vfs.ensureDefaultCard();
        try {
            switch (args[0]) {
                case "apk":
                    require(args, 2);
                    printApkSummary(new File(args[1]));
                    break;
                case "run":
                    require(args, 2);
                    txRun(new File(args[1]));
                    break;
                case "gui":
                    require(args, 2);
                    txGuiRun(new File(args[1]),
                            args.length > 2 ? Long.parseLong(args[2]) : 0);
                    break;
                // Backward-compatible aliases for the old translation task names.
                case "txrun":
                    require(args, 2);
                    txRun(new File(args[1]));
                    break;
                case "txgui":
                    require(args, 2);
                    txGuiRun(new File(args[1]),
                            args.length > 2 ? Long.parseLong(args[2]) : 0);
                    break;
                // Interpreter path, kept for comparisons with the translation.
                case "irun":
                    require(args, 2);
                    probeRun(new File(args[1]));
                    break;
                case "igui":
                    require(args, 2);
                    interpreterGuiRun(new File(args[1]),
                            args.length > 2 ? Long.parseLong(args[2]) : 0);
                    break;
                case "txdump":
                    require(args, 3);
                    txDump(new File(args[1]), args[2],
                            args.length > 3 ? args[3] : null);
                    break;
                case "dump":
                    dumpDex(new File(args[1]),
                            args.length > 2 ? args[2] : null);
                    break;
                default:
                    if (args[0].endsWith(".apk")) {
                        printApkSummary(new File(args[0]));
                    } else {
                        dumpDex(new File(args[0]),
                                args.length > 1 ? args[1] : null);
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("tartine: " + e);
            System.exit(1);
        }
    }

    private static void usage() {
        System.out.println("Usage: tartine.Main (apk|run|gui|irun|igui|dump) <file>");
    }

    private static void require(String[] args, int count) {
        if (args.length < count) {
            throw new IllegalArgumentException("missing file argument");
        }
    }

    private static void dumpDex(File file, String fqnPrefix) throws Exception {
        ApkReader reader = ApkReader.open(file.toPath());
        DexSet dexes = reader.dexSet();
        for (Dex dex : dexes.dexes()) {
            for (DexClass cls : dex.classes()) {
                if (fqnPrefix == null || cls.type.contains(fqnPrefix)) {
                    System.out.print(DexDump.dump(cls));
                }
            }
        }
    }

    private static void printApkSummary(File apk) throws Exception {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            BinaryXml.Element manifest = reader.manifest();
            System.out.println("package: " + manifest.attr("package"));
            System.out.println("version: " + manifest.attr("versionCode")
                    + " / " + manifest.attr("versionName"));
            List<String> activities = new ArrayList<>();
            int permissions = 0;
            for (BinaryXml.Element child : manifest.children) {
                if (child.name.equals("uses-permission")) {
                    permissions++;
                } else if (child.name.equals("application")) {
                    for (BinaryXml.Element component : child.children) {
                        if (component.name.equals("activity")
                                || component.name.equals("activity-alias")) {
                            activities.add(component.attr("name"));
                        }
                    }
                }
            }
            System.out.println("activities (" + activities.size() + "): " + activities);
            System.out.println("permissions: " + permissions);
            DexSet dexes = reader.dexSet();
            System.out.println("dex files: " + reader.dexEntries().size()
                    + ", classes: " + dexes.classCount()
                    + ", methods: " + dexes.methodCount());
            Set<String> abis = new TreeSet<>(reader.nativeAbis());
            System.out.println("native abis: " + (abis.isEmpty() ? "none" : abis)
                    + " (host picks " + Abi.pickBest(abis) + ")");
            System.out.println("resources: " + reader.hasResources());
        }
    }

    /**
     * Translates one dex class (temp probe mode) and prints the result
     * through ASM's TraceClassVisitor, naming methods to filter on.
     */
    private static void txDump(File apk, String dottedOrSlash,
            String methodFilter) throws Exception {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            DexSet dexes = reader.dexSet();
            tartine.tx.TranslatedLoader loader = new tartine.tx.TranslatedLoader(
                    Main.class.getClassLoader(), dexes);
            String desc = dottedOrSlash.contains("/")
                    ? dottedOrSlash
                    : dottedOrSlash.replace('.', '/');
            byte[] bytes;
            if (methodFilter != null) {
                tartine.dex.DexClass cls =
                        loader.dexLookup().apply("L" + desc + ";");
                bytes = tartine.tx.TxTranslator.emitProbeSingle(cls,
                        loader.fieldSynths(), loader.guestInterfaces(),
                        loader.dexLookup(), loader.getParent(), methodFilter);
            } else {
                bytes = loader.translateClass(desc);
            }
            org.objectweb.asm.util.TraceClassVisitor printer =
                    new org.objectweb.asm.util.TraceClassVisitor(new PrintWriter(
                            System.err));
            new org.objectweb.asm.ClassReader(bytes).accept(printer, 0);
        }
    }

    /** Interpreter headless probe ({@code irun}), kept as a reference while
     *  the translation path grows. */
    private static void probeRun(File apk) throws Exception {
        runOnCreate(apk);
    }

    /** Launcher activity loaded through the translated loader, plus the
     *  still-open APK reader the loader's lazy per-class translation needs. */
    private record TxApp(ApkReader reader, tartine.tx.TranslatedLoader loader,
            Object activity) {
    }

    /**
     * Translated-run probe ({@code run}, the {@code tartine.tx} counterpart
     * of {@link #runOnCreate}): launcher {@code <init>} then {@code onCreate}
     * as real JVM bytecode. The framework module supplies android.* skeletons
     * (UOE bodies = the gap census); the dex classes come from
     * {@link TranslatedLoader}.
     */
    private static void txRun(File apk) throws Exception {
        txLaunch(apk).reader().close();
    }

    /**
     * Translated launch: loader bring-up, main Looper, launcher {@code
     * <init>} then {@code onCreate} as real JVM bytecode. Keeps the APK
     * reader open (the loader translates classes lazily); callers close
     * {@code TxApp.reader()} when done. Failures print and come back with a
     * null activity.
     */
    private static TxApp txLaunch(File apk) throws Exception {
        ApkReader reader = ApkReader.open(apk.toPath());
        BinaryXml.Element manifest = reader.manifest();
        String packageName = manifest.attr("package");
        String activity = findLauncher(manifest);
        if (activity == null) {
            System.out.println("no launcher activity in manifest");
            return new TxApp(reader, null, null);
        }
        activity = qualify(packageName, activity);
        System.out.println("launcher: " + activity);
        DexSet dexes = reader.dexSet();
        System.out.println("dex classes: " + dexes.classCount());
        tartine.tx.TranslatedLoader loader = new tartine.tx.TranslatedLoader(
                Main.class.getClassLoader(), dexes);
        Thread.currentThread().setContextClassLoader(loader);
        Class<?> task;
        try {
            task = loader.loadClass(activity);
        } catch (ClassNotFoundException | LinkageError | RuntimeException e) {
            System.out.println("load " + activity + ": " + sanitize(e));
            printRootFrames(e);
            return new TxApp(reader, loader, null);
        }
        System.out.println("loaded: " + task.getName());
        // Device parity with ActivityThread.main(): the guest's main
        // thread carries the main Looper before any Activity code runs.
        android.os.Looper.prepareMainLooper();
        Object receiver;
        try {
            receiver = task.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | LinkageError e) {
            System.out.println("<init>: " + sanitize(e));
            printRootFrames(e);
            return new TxApp(reader, loader, null);
        }
        java.lang.reflect.Method onCreate;
        try {
            // Android Activity.onCreate(Bundle) is protected — getMethod
            // (public only) misses it; walk the chain with accessibility.
            onCreate = null;
            for (Class<?> c = task; c != null && onCreate == null;
                    c = c.getSuperclass()) {
                try {
                    onCreate = c.getDeclaredMethod("onCreate",
                            android.os.Bundle.class);
                } catch (NoSuchMethodException ignored) {
                    // continue up the chain
                }
            }
            if (onCreate == null) {
                throw new NoSuchMethodException("onCreate(android.os.Bundle)");
            }
            onCreate.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            System.out.println("no onCreate(Bundle): " + sanitize(e));
            return new TxApp(reader, loader, null);
        }
        try {
            onCreate.invoke(receiver, new Object[] { null });
            System.out.println("onCreate returned");
            return new TxApp(reader, loader, receiver);
        } catch (java.lang.reflect.InvocationTargetException x) {
            System.out.println("onCreate threw: " + sanitize(x));
            printRootFrames(x);
            return new TxApp(reader, loader, null);
        }
    }

    /** Root-cause stack frames for a launch failure (debug only). */
    private static void printRootFrames(Throwable source) {
        Throwable root = source;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        int frameDepth = 16;
        for (StackTraceElement frame : root.getStackTrace()) {
            System.out.println("    at " + frame);
            if (--frameDepth <= 0) {
                break;
            }
        }
    }

    /**
     * Translated GUI probe ({@code gui}): run the launcher through {@code
     * TranslatedLoader}, then drive the compose pipeline the way the
     * interpreter GUI does (createComposition, setContent, attach owners,
     * lifecycle to RESUMED), but with plain reflection on the translated
     * classes, and show the desktop window. Gaps print; a blocked draw
     * still shows the partially composed surface like a first frame.
     */
    private static void txGuiRun(File apk, long timeoutSecs) throws Exception {
        firstDrawFrame = null;
        TxApp app = txLaunch(apk);
        try {
            if (app.activity() == null) {
                System.out.println("gui: launch failed; no window");
                return;
            }
            Object activity = app.activity();
            // Activity.setContentView delegates to the window (framework
            // Activity.java lines 3906-3911); the flattened decor keeps the
            // view as its child (PhoneWindow.setContentView flow per the
            // framework Window shell).
            Object window = txField(activity, "mWindow");
            Object decor = txField(window, "mDecor");
            Object view = txInvoke(decor, "getChildAt", 0);
            System.out.println("gui content view: " + describeTx(view));
            Object deferred = txField(view,
                    "shouldCreateCompositionOnAttachedToWindow");
            System.out.println("gui defer-composition flag: " + deferred);
            boolean defer = Boolean.TRUE.equals(deferred)
                    || Integer.valueOf(1).equals(deferred);
            if (!defer) {
                System.out.println(
                        "gui: no deferred composition; nothing to drive");
                return;
            }
            System.out.println("gui parentContext: "
                    + describeTx(txField(view, "parentContext")));
            System.out.println("gui attached: "
                    + txInvoke(view, "isAttachedToWindow"));
            // Attach parity with the interpreter driver: joinWindow marks the
            // content view attached at setContentView time; the host shell's
            // ViewRootImpl-style attach chain has not landed, so the driver
            // stands in and sets the AttachInfo ref the same way.
            txSetField(view, "mAttachInfo", new Object());
            System.out.println("gui attached after mark: "
                    + txInvoke(view, "isAttachedToWindow"));
            txInvoke(view, "createComposition");
            System.out.println("gui: createComposition returned");
            Object lambda = txInvoke(txField(view, "content"), "getValue");
            System.out.println("gui content lambda: " + describeTx(lambda));
            Object composition = txField(view, "composition");
            txInvoke(composition, "setContent", lambda);
            System.out.println("gui: setContent returned");
            Object owner = txInvoke(composition, "getOwner");
            // Attach parity: the activity is the view tree's lifecycle and
            // saved-state owner (what ComponentActivity does to the decor
            // on device), then the owner's onAttachedToWindow fires the
            // stored composition callback.
            txInvokeStatic(app.loader(),
                    "androidx.lifecycle.ViewTreeLifecycleOwner", "set",
                    view, activity);
            txInvokeStatic(app.loader(),
                    "androidx.savedstate.ViewTreeSavedStateRegistryOwner",
                    "set", view, activity);
            txInvoke(owner, "onAttachedToWindow");
            System.out.println("gui: onAttachedToWindow returned");
            Object lifecycle = txInvoke(activity, "getLifecycle");
            Class<?> events = app.loader()
                    .loadClass("androidx.lifecycle.Lifecycle$Event");
            for (String event : new String[] {"ON_CREATE", "ON_START",
                    "ON_RESUME"}) {
                txInvoke(lifecycle, "handleLifecycleEvent",
                        events.getDeclaredField(event).get(null));
            }
            System.out.println("gui: lifecycle resumed");
            // Content again post-attach: the observer forwards the stored
            // content to the inner composition (interpreter driveAttach
            // parity).
            txInvoke(composition, "setContent", lambda);
            System.out.println("gui: post-attach setContent returned");
            // Frame pump parity with driveFrame: fire the posted
            // Choreographer callbacks (the driver's VSync), repeating while
            // callbacks re-post (bounded like the interpreter's 10 rounds).
            for (int round = 0; round < 10; round++) {
                int fired = android.view.Choreographer.pumpFrameCallbacks(
                        System.nanoTime());
                System.out.println("gui: frame callbacks round " + round
                        + ": " + fired);
                if (fired == 0) {
                    break;
                }
            }
            txMeasureLayoutDraw(owner);
            System.out.println("gui: window ready (frame: "
                    + (firstDrawFrame != null ? "present" : "absent") + ")");
            try {
                DesktopWindow.show(activity.getClass().getName(), timeoutSecs,
                        firstDrawFrame, loadWindowIcons(apk));
            } finally {
                closeFirstDrawFrame();
            }
            System.out.println("gui: window closed");
        } finally {
            app.reader().close();
        }
    }

    /**
     * Measure, layout, then render the composed root into a headless
     * surface (the first-traversal twin of the device's VSync pass; the
     * interpreter driveMeasureLayout/driveDraw parity). Painted counts are
     * the first-draw evidence.
     */
    private static void txMeasureLayoutDraw(Object owner) {
        try {
            txInvoke(owner, "onMeasure", 480 | (1 << 30), 960 | (1 << 30));
            System.out.println("gui: onMeasure returned");
            txInvoke(owner, "onLayout", false, 0, 0, 480, 960);
            System.out.println("gui: onLayout returned");
            GfxSurface surface = RasterPresenter.createSurface(480, 960);
            android.graphics.Canvas hostCanvas =
                    RasterPresenter.createSurfaceCanvas(surface);
            // Frame handed to the desktop window; a blocked draw still
            // shows the partially composed surface, like the first
            // on-device frame.
            firstDrawFrame = surface;
            txInvoke(owner, "dispatchDraw", hostCanvas);
            System.out.println("gui: dispatchDraw returned");
            int[] pixels = surface.readPixels();
            long painted = 0;
            for (int pixel : pixels) {
                if ((pixel >>> 24) != 0) {
                    painted++;
                }
            }
            System.out.println("gui: painted pixels " + painted + "/"
                    + pixels.length);
        } catch (RuntimeException e) {
            System.out.println("gui: measure/draw blocked at: "
                    + sanitize(e));
        }
    }

    /** Reads a private field walking the receiver's runtime class chain. */
    private static Object txField(Object owner, String name) {
        try {
            for (Class<?> c = owner.getClass(); c != null;
                    c = c.getSuperclass()) {
                try {
                    java.lang.reflect.Field f = c.getDeclaredField(name);
                    f.setAccessible(true);
                    return f.get(owner);
                } catch (NoSuchFieldException ignored) {
                    // continue up the chain
                }
            }
            throw new NoSuchFieldException(name);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("tx field " + name + ": " + e,
                    e);
        }
    }

    /** Writes a private field walking the receiver's runtime class chain. */
    private static void txSetField(Object owner, String name, Object value) {
        try {
            for (Class<?> c = owner.getClass(); c != null;
                    c = c.getSuperclass()) {
                try {
                    java.lang.reflect.Field f = c.getDeclaredField(name);
                    f.setAccessible(true);
                    f.set(owner, value);
                    return;
                } catch (NoSuchFieldException ignored) {
                    // continue up the chain
                }
            }
            throw new NoSuchFieldException(name);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("tx field " + name + ": " + e,
                    e);
        }
    }

    /** Reflective invoke walking the receiver's runtime class chain. */
    private static Object txInvoke(Object owner, String name, Object... args) {
        try {
            for (Class<?> c = owner.getClass(); c != null;
                    c = c.getSuperclass()) {
                for (java.lang.reflect.Method m : c.getDeclaredMethods()) {
                    if (!m.getName().equals(name)
                            || m.getParameterCount() != args.length) {
                        continue;
                    }
                    m.setAccessible(true);
                    return m.invoke(owner, args);
                }
            }
            throw new NoSuchMethodException(name);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            Throwable cause = e;
            if (e instanceof java.lang.reflect.InvocationTargetException x) {
                cause = x.getCause();
                if (cause != null) {
                    cause.printStackTrace(); // TEMP probe diagnostic
                }
            }
            throw new IllegalStateException("tx invoke " + name + ": "
                    + sanitize(cause), cause);
        }
    }

    /** Reflective invoke of a static translated method by binary name. */
    private static Object txInvokeStatic(tartine.tx.TranslatedLoader loader,
            String className, String name, Object... args) {
        try {
            Class<?> cls = loader.loadClass(className);
            for (java.lang.reflect.Method m : cls.getDeclaredMethods()) {
                boolean stat = java.lang.reflect.Modifier.isStatic(
                        m.getModifiers());
                if (m.getName().equals(name)
                        && m.getParameterCount() == args.length && stat) {
                    m.setAccessible(true);
                    return m.invoke(null, args);
                }
            }
            throw new NoSuchMethodException(className + "." + name);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            Throwable cause = e;
            if (e instanceof java.lang.reflect.InvocationTargetException x) {
                cause = x.getCause();
            }
            throw new IllegalStateException("tx static " + className + "."
                    + name + ": " + sanitize(cause), cause);
        }
    }

    /** Safe one-line description: literals verbatim, unknown guest
     *  references as class names only (never their toString). */
    private static String describeTx(Object value) {
        if (value == null || value instanceof Integer
                || value instanceof Boolean || value instanceof String) {
            return String.valueOf(value);
        }
        return "<ref " + value.getClass().getName() + ">";
    }

    /**
     * Translates a reflective/launch failure to one printable line: the
     * exception chain with host frames trimmed away.
     */
    private static String sanitize(Throwable error) {
        StringBuilder text = new StringBuilder();
        for (Throwable t = error; t != null && text.length() < 2000;
                t = t.getCause() instanceof Throwable ? t.getCause() : null) {
            if (text.length() > 0) {
                text.append(" <- ");
            }
            text.append(t.getClass().getSimpleName());
            String message = t.getMessage();
            if (message != null) {
                text.append(" (").append(message.replace("\n", " ")).append(')');
            }
            if (t.getCause() == t) {
                break;
            }
        }
        return text.toString();
    }

    private static void interpreterGuiRun(File apk, long timeoutSecs) throws Exception {
        firstDrawFrame = null;
        String activity = runOnCreate(apk);
        if (activity == null) {
            System.out.println("igui: onCreate did not return; no window");
            return;
        }
        System.out.println("igui: window ready (frame: "
                + (firstDrawFrame != null ? "present" : "absent") + ")");
        try {
            DesktopWindow.show(activity, timeoutSecs, firstDrawFrame,
                    loadWindowIcons(apk));
        } finally {
            closeFirstDrawFrame();
        }
        System.out.println("window closed");
    }

    /** Releases the first frame once the desktop window has consumed it. */
    private static void closeFirstDrawFrame() {
        if (firstDrawFrame != null) {
            firstDrawFrame.close();
            firstDrawFrame = null;
        }
    }

    /**
     * Resolves the app's launcher icon for the desktop window (a second
     * read of the APK; resolution never throws — an empty list keeps the
     * platform default icon).
     */
    private static List<WindowIcon> loadWindowIcons(File apkFile) {
        try (ApkReader reader = ApkReader.open(apkFile.toPath())) {
            return AppIcon.load(reader, System.out::println);
        } catch (IOException error) {
            System.out.println("icon: cannot reopen apk ("
                    + error.getMessage() + ")");
            return List.of();
        }
    }

    /**
     * Headless probe shared by {@code irun} and {@code igui}: executes the
     * launcher activity's {@code onCreate} until the first gap. Returns the
     * activity name when onCreate returned, else null.
     */
    private static String runOnCreate(File apk) throws Exception {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            BinaryXml.Element manifest = reader.manifest();
            String packageName = manifest.attr("package");
            String activity = findLauncher(manifest);
            if (activity == null) {
                System.out.println("no launcher activity in manifest");
                return null;
            }
            activity = qualify(packageName, activity);
            System.out.println("launcher: " + activity);
            DexSet dexes = reader.dexSet();
            DexClass cls = dexes.findClass("L" + activity.replace('.', '/') + ";");
            if (cls == null) {
                System.out.println("class not in dex: " + activity);
                return null;
            }
            DexMethod onCreate = null;
            for (DexMethod method : cls.methods) {
                if (method.name.equals("onCreate") && method.parameterTypes.size() == 1) {
                    onCreate = method;
                    break;
                }
            }
            if (onCreate == null) {
                System.out.println("no onCreate in " + activity);
                return null;
            }
            // Probe: run the real constructor chain (in-dex bodies execute,
            // framework <init> bodies are no-ops), then onCreate.
            Interpreter interpreter = new Interpreter(dexes);
            FwIndex framework = loadFramework(manifest, interpreter);
            if (framework != null) {
                interpreter.setFramework(framework);
            }
            DexMethod init = null;
            for (DexMethod method : cls.methods) {
                if (method.name.equals("<init>") && method.parameterTypes.isEmpty()) {
                    init = method;
                    break;
                }
            }
            if (init == null) {
                System.out.println("no default constructor in " + activity);
                return null;
            }
            VmObject receiver = new VmObject(cls);
            // Diagnostic escape hatch for spin hangs (default 100M).
            long maxSteps = 100000000L;
            try {
                maxSteps = Long.parseLong(
                        System.getenv().getOrDefault("TARTINE_MAX_STEPS", "100000000"));
            } catch (NumberFormatException e) {
                // keep default
            }
            interpreter.setMaxSteps(maxSteps);
            try {
                interpreter.invoke(cls, init, receiver);
                Object result = interpreter.invoke(cls, onCreate, receiver,
                        new Object[]{null});
                System.out.println("onCreate returned after " + interpreter.steps()
                        + " steps: " + result);
                assertContent(dexes, interpreter, receiver);
                return activity;
            } catch (VmException blocked) {
                System.out.println("blocked after " + interpreter.steps() + " steps at: "
                        + blocked.getMessage());
                printTrace(interpreter);
                return null;
            } catch (RuntimeException bug) {
                System.out.println("failed after " + interpreter.steps() + " steps with "
                        + bug);
                if (bug instanceof VmThrow && ((VmThrow) bug).value instanceof VmObject) {
                    System.out.println("thrown: "
                            + ((VmObject) ((VmThrow) bug).value).type.type);
                }
                printTrace(interpreter);
                bug.printStackTrace(System.err);
                return null;
            }
        }
    }

    private static void printTrace(Interpreter interpreter) {
        for (String line : interpreter.lastTrace()) {
            System.out.println("  at " + line);
        }
    }

    /**
     * Proves {@code setContent} took effect: the activity retained the
     * ComposeView, the deferred-composition flag is set, the content state
     * exists, and (best effort) the stored lambda is the app's composable.
     */
    private static void assertContent(DexSet dexes, Interpreter interpreter,
            VmObject activity) {
        Object contentView = activity.getField("Landroid/app/Activity;.mContentView");
        if (!(contentView instanceof VmObject)) {
            System.out.println("content view: MISSING (nothing retained)");
            return;
        }
        VmObject view = (VmObject) contentView;
        System.out.println("content view: " + view.type.type);
        Object deferred = view.getField("Landroidx/compose/ui/platform/ComposeView;"
                + ".shouldCreateCompositionOnAttachedToWindow");
        System.out.println("defer-composition flag: " + deferred
                + (Integer.valueOf(1).equals(deferred) ? " (ok)" : " (UNEXPECTED)"));
        Object state = view.getField(
                "Landroidx/compose/ui/platform/ComposeView;.content");
        if (!(state instanceof VmObject)) {
            System.out.println("content state: MISSING");
            return;
        }
        VmObject stateObject = (VmObject) state;
        System.out.println("content state: " + stateObject.type.type);
        try {
            Object lambda = readContentLambda(dexes, interpreter, view);
            System.out.println("content lambda: " + (lambda instanceof VmObject
                    ? ((VmObject) lambda).type.type : lambda));
        } catch (RuntimeException e) {
            System.out.println("content lambda: unreadable (" + e.getMessage() + ")");
        }
        // setContentView joined the view to a window; on device
        // onAttachedToWindow would now start composition. Drive it.
        driveComposition(dexes, interpreter, view, activity);
    }

    /**
     * Reads the stored composable out of the ComposeView's content state,
     * or null when it is missing or unreadable.
     */
    private static Object readContentLambda(DexSet dexes, Interpreter interpreter,
            VmObject view) {
        Object state = view.getField(
                "Landroidx/compose/ui/platform/ComposeView;.content");
        if (!(state instanceof VmObject)) {
            return null;
        }
        VmObject stateObject = (VmObject) state;
        DexMethod getValue = null;
        DexClass home = stateObject.type;
        while (home != null && getValue == null) {
            for (DexMethod candidate : home.methods) {
                if (candidate.name.equals("getValue")
                        && candidate.parameterTypes.isEmpty()) {
                    getValue = candidate;
                    break;
                }
            }
            home = home.superclass == null ? null
                    : dexes.findClass(home.superclass);
        }
        if (getValue == null) {
            return null;
        }
        try {
            return interpreter.invoke(stateObject.type, getValue, stateObject);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Starts composition on a deferred ComposeView (the driver's equivalent
     * of onAttachedToWindow) and reports the result. Gaps inside composition
     * are the next worklist, not probe failures.
     */
    private static void driveComposition(DexSet dexes, Interpreter interpreter,
            VmObject view, VmObject activity) {
        Object deferred = view.getField("Landroidx/compose/ui/platform/ComposeView;"
                + ".shouldCreateCompositionOnAttachedToWindow");
        if (!Integer.valueOf(1).equals(deferred)) {
            return;
        }
        DexMethod create = null;
        DexClass home = view.type;
        while (home != null && create == null) {
            for (DexMethod candidate : home.methods) {
                if (candidate.name.equals("createComposition")
                        && candidate.parameterTypes.isEmpty()
                        && (candidate.accessFlags & 0x0008) == 0) {
                    create = candidate;
                    break;
                }
            }
            if (create == null) {
                home = home.superclass == null ? null : dexes.findClass(home.superclass);
            }
        }
        if (create == null) {
            System.out.println("composition: no createComposition to drive");
            return;
        }
        try {
            interpreter.invoke(view.type, create, view);
            System.out.println("createComposition returned after " + interpreter.steps()
                    + " steps");
        } catch (RuntimeException e) {
            System.out.println("composition blocked at: " + e.getMessage());
            if (e instanceof VmThrow) {
                System.out.println("thrown: " + describe(((VmThrow) e).value));
            }
            printTrace(interpreter);
        }
        System.out.println("composition: " + describe(view.getField(
                "Landroidx/compose/ui/platform/AbstractComposeView;.composition")));
        driveContent(dexes, interpreter, view);
        driveAttach(dexes, interpreter, view, activity);
    }

    /**
     * Runs the stored composable on the created composition (the driver's
     * equivalent of the frame commit). Gaps inside are the next worklist.
     */
    private static void driveContent(DexSet dexes, Interpreter interpreter,
            VmObject view) {
        Object composition = view.getField(
                "Landroidx/compose/ui/platform/AbstractComposeView;.composition");
        if (!(composition instanceof VmObject)) {
            System.out.println("content: no composition to drive");
            return;
        }
        VmObject compositionObject = (VmObject) composition;
        Object lambda = readContentLambda(dexes, interpreter, view);
        if (!(lambda instanceof VmObject)) {
            System.out.println("content: no composable lambda to run");
            return;
        }
        DexMethod setContent = null;
        DexClass home = compositionObject.type;
        while (home != null && setContent == null) {
            for (DexMethod candidate : home.methods) {
                if (candidate.name.equals("setContent")
                        && candidate.parameterTypes.size() == 1
                        && (candidate.accessFlags & 0x0008) == 0) {
                    setContent = candidate;
                    break;
                }
            }
            if (setContent == null) {
                home = home.superclass == null ? null : dexes.findClass(home.superclass);
            }
        }
        if (setContent == null) {
            System.out.println("content: no setContent to drive");
            return;
        }
        try {
            interpreter.invoke(compositionObject.type, setContent, compositionObject,
                    new Object[]{lambda});
            System.out.println("setContent returned after " + interpreter.steps()
                    + " steps");
        } catch (RuntimeException e) {
            System.out.println("content blocked at: " + e.getMessage());
            if (e instanceof VmThrow) {
                System.out.println("thrown: " + describe(((VmThrow) e).value));
            }
            printTrace(interpreter);
            e.printStackTrace(System.err);
        }
    }

    private static String describe(Object value) {
        return value instanceof VmObject
                ? ((VmObject) value).type.type : String.valueOf(value);
    }

    /**
     * Models attach completion: tags the content view with the activity as
     * its lifecycle/saved-state owners (what ComponentActivity does to the
     * decor on device), then runs the AndroidComposeView's
     * onAttachedToWindow, which fires the stored composition callback.
     */
    private static void driveAttach(DexSet dexes, Interpreter interpreter,
            VmObject view, VmObject activity) {
        Object composition = view.getField(
                "Landroidx/compose/ui/platform/AbstractComposeView;.composition");
        if (!(composition instanceof VmObject)) {
            System.out.println("attach: no composition to attach");
            return;
        }
        VmObject compositionObject = (VmObject) composition;
        try {
            invokeStatic(dexes, interpreter,
                    "Landroidx/lifecycle/ViewTreeLifecycleOwner;", "set",
                    view, activity);
            invokeStatic(dexes, interpreter,
                    "Landroidx/savedstate/ViewTreeSavedStateRegistryOwner;", "set",
                    view, activity);
            Object owner = invokeVirtual(dexes, interpreter, compositionObject,
                    "getOwner", 0);
            if (!(owner instanceof VmObject)) {
                System.out.println("attach: no owner to attach");
                return;
            }
            VmObject ownerObject = (VmObject) owner;
            invokeVirtual(dexes, interpreter, ownerObject, "onAttachedToWindow", 0);
            ownerObject.putField("Landroid/view/View;.mAttached", 1);
            System.out.println("onAttachedToWindow returned after " + interpreter.steps()
                    + " steps");
            driveLifecycle(dexes, interpreter, activity);
            driveContent(dexes, interpreter, view);
            Object original = invokeVirtual(dexes, interpreter, compositionObject,
                    "getOriginal", 0);
            Object invalidations = original instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) original,
                            "getHasInvalidations", 0)
                    : null;
            System.out.println("original invalidations: " + invalidations);
            Object root = invokeVirtual(dexes, interpreter, ownerObject, "getRoot", 0);
            Object children = root instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) root,
                            "getChildren$ui_release", 0)
                    : null;
            Object childCount = children instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) children, "size", 0)
                    : null;
            System.out.println("root children: " + childCount);
            driveFrame(dexes, interpreter, ownerObject);
        } catch (RuntimeException e) {
            System.out.println("attach blocked at: " + e.getMessage());
            if (e instanceof VmThrow) {
                System.out.println("thrown: " + describe(((VmThrow) e).value));
            }
            printTrace(interpreter);
            e.printStackTrace(System.err);
        }
    }

    /** Invokes a static dex method found by class, name, and arity. */
    private static Object invokeStatic(DexSet dexes, Interpreter interpreter,
            String classDesc, String name, Object... args) {
        DexClass cls = dexes.findClass(classDesc);
        if (cls == null) {
            throw new VmException("no class: " + classDesc);
        }
        for (DexMethod candidate : cls.methods) {
            if (candidate.name.equals(name)
                    && candidate.parameterTypes.size() == args.length
                    && (candidate.accessFlags & 0x0008) != 0) {
                return interpreter.invoke(cls, candidate, null, args);
            }
        }
        throw new VmException("no static: " + classDesc + "." + name);
    }

    /** Invokes an instance dex method found by walking the receiver's hierarchy. */
    private static Object invokeVirtual(DexSet dexes, Interpreter interpreter,
            VmObject receiver, String name, int arity, Object... args) {
        DexClass home = receiver.type;
        while (home != null) {
            for (DexMethod candidate : home.methods) {
                if (candidate.name.equals(name)
                        && candidate.parameterTypes.size() == arity
                        && (candidate.accessFlags & 0x0008) == 0) {
                    return interpreter.invoke(receiver.type, candidate, receiver, args);
                }
            }
            home = home.superclass == null ? null : dexes.findClass(home.superclass);
        }
        throw new VmException("no method: " + receiver.type.type + "." + name);
    }

    /**
     * Brings the activity lifecycle to RESUMED through the registry (the
     * driver's equivalent of the device's activity bring-up). Dispatching
     * ON_CREATE notifies the composition's observer, which forwards the
     * stored content to the inner composition.
     */
    private static void driveLifecycle(DexSet dexes, Interpreter interpreter,
            VmObject activity) {
        Object lifecycle = invokeVirtual(dexes, interpreter, activity, "getLifecycle", 0);
        if (!(lifecycle instanceof VmObject)) {
            System.out.println("lifecycle: none to drive");
            return;
        }
        VmObject lifecycleObject = (VmObject) lifecycle;
        String[] events = {"ON_CREATE", "ON_START", "ON_RESUME"};
        for (String event : events) {
            Object constant = interpreter.staticField(
                    "Landroidx/lifecycle/Lifecycle$Event;", event,
                    "Landroidx/lifecycle/Lifecycle$Event;");
            invokeVirtual(dexes, interpreter, lifecycleObject, "handleLifecycleEvent", 1,
                    constant);
        }
        System.out.println("lifecycle resumed after " + interpreter.steps() + " steps");
    }

    /**
     * Fires recorded frame callbacks with the current nanos (the driver's
     * VSync), repeating while callbacks re-post, up to a bound. Then
     * reports whether the root gained children.
     */
    private static void driveFrame(DexSet dexes, Interpreter interpreter,
            VmObject owner) {
        for (int round = 0; round < 10; round++) {
            java.util.List<Object> callbacks = interpreter.frameCallbacks();
            System.out.println("frame callbacks round " + round + ": " + callbacks.size()
                    + " at " + interpreter.steps() + " steps");
            if (callbacks.isEmpty()) {
                break;
            }
            boolean advanced = false;
            for (Object callback : callbacks) {
                System.out.println("frame callback: " + describe(callback));
                if (!(callback instanceof VmObject)) {
                    continue;
                }
                try {
                    invokeVirtual(dexes, interpreter, (VmObject) callback, "doFrame", 1,
                            System.nanoTime());
                    advanced = true;
                } catch (RuntimeException e) {
                    System.out.println("frame blocked at: " + e.getMessage());
                    if (e instanceof VmThrow) {
                        System.out.println("thrown: " + describe(((VmThrow) e).value));
                    }
                    printTrace(interpreter);
                    e.printStackTrace(System.err);
                    return;
                }
            }
            if (!advanced) {
                break;
            }
        }
        try {
            Object root = invokeVirtual(dexes, interpreter, owner, "getRoot", 0);
            Object children = root instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) root,
                            "getChildren$ui_release", 0)
                    : null;
            Object childCount = children instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) children, "size", 0)
                    : null;
            System.out.println("root children after frame: " + childCount);
            driveMeasureLayout(dexes, interpreter, owner);
            driveDraw(dexes, interpreter, owner);
        } catch (RuntimeException e) {
            System.out.println("frame blocked at: " + e.getMessage());
            if (e instanceof VmThrow) {
                System.out.println("thrown: " + describe(((VmThrow) e).value));
            }
            printTrace(interpreter);
        }
    }

    /**
     * Runs measure and layout on the owner with EXACTLY specs matching
     * the headless window (480x960), the driver's equivalent of the first
     * traversal. Then reports the root's measured size.
     */
    private static void driveMeasureLayout(DexSet dexes, Interpreter interpreter,
            VmObject owner) {
        int widthSpec = 480 | (1 << 30);
        int heightSpec = 960 | (1 << 30);
        try {
            invokeVirtual(dexes, interpreter, owner, "onMeasure", 2, widthSpec, heightSpec);
            System.out.println("onMeasure returned after " + interpreter.steps() + " steps");
            invokeVirtual(dexes, interpreter, owner, "onLayout", 5, 0, 0, 0, 480, 960);
            System.out.println("onLayout returned after " + interpreter.steps() + " steps");
            Object root = invokeVirtual(dexes, interpreter, owner, "getRoot", 0);
            Object width = root instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) root, "getWidth", 0)
                    : null;
            Object height = root instanceof VmObject
                    ? invokeVirtual(dexes, interpreter, (VmObject) root, "getHeight", 0)
                    : null;
            System.out.println("root size: " + width + "x" + height);
        } catch (RuntimeException e) {
            System.out.println("measure blocked at: " + e.getMessage());
            if (e instanceof VmThrow) {
                System.out.println("thrown: " + describe(((VmThrow) e).value));
            }
            printTrace(interpreter);
            e.printStackTrace(System.err);
        }
    }

    /**
     * Drives the first draw pass: a host-built Skia surface canvas (the
     * renderer role per AGENTS graphics mapping) is bound through
     * {@link Interpreter#hostObjectFor} and handed to the guest
     * {@code dispatchDraw}, the AndroidComposeView entry on-device draws
     * take after ViewRootImpl's traversal. Painted-pixel counts are the
     * first-draw evidence, independent of the desktop window.
     */
    private static void driveDraw(DexSet dexes, Interpreter interpreter,
            VmObject owner) {
        GfxSurface surface = RasterPresenter.createSurface(480, 960);
        android.graphics.Canvas hostCanvas =
                RasterPresenter.createSurfaceCanvas(surface);
        VmObject canvasObject =
                interpreter.hostObjectFor("Landroid/graphics/Canvas;", hostCanvas);
        // Frame handed to the desktop window (gui mode); a blocked draw still
        // shows the partially composed surface, like the first on-device frame.
        firstDrawFrame = surface;
        try {
            invokeVirtual(dexes, interpreter, owner, "dispatchDraw", 1, canvasObject);
            System.out.println(
                    "dispatchDraw completed at " + interpreter.steps() + " steps");
        } catch (RuntimeException e) {
            System.out.println("draw blocked at: " + e.getMessage());
            if (e instanceof VmThrow) {
                System.out.println("thrown: " + describe(((VmThrow) e).value));
            }
            printTrace(interpreter);
        }
        try {
            int[] pixels = surface.readPixels();
            long painted = 0;
            int modeColor = 0;
            int modeCount = 0;
            java.util.Map<Integer, Integer> histogram = new java.util.HashMap<>();
            for (int pixel : pixels) {
                if ((pixel >>> 24) != 0) {
                    painted++;
                }
                Integer count = histogram.get(pixel);
                count = count == null ? 1 : count + 1;
                histogram.put(pixel, count);
                if (count > modeCount) {
                    modeCount = count;
                    modeColor = pixel;
                }
            }
            long offMode = pixels.length - modeCount;
            System.out.println("pixels: " + painted + "/" + pixels.length
                    + " painted, center=0x"
                    + Integer.toHexString(pixels[480 / 2 + (960 / 2) * 480])
                    + ", corner=0x" + Integer.toHexString(pixels[0])
                    + ", distinct=" + histogram.size()
                    + ", mode=0x" + Integer.toHexString(modeColor)
                    + " (" + modeCount + "), offMode=" + offMode);
        } catch (RuntimeException | LinkageError e) {
            System.out.println("readback failed: " + e);
        }
    }

    private static FwIndex loadFramework(BinaryXml.Element manifest,
            Interpreter interpreter) {
        int targetSdk = -1;
        BinaryXml.Element usesSdk = manifest.child("uses-sdk");
        if (usesSdk != null) {
            try {
                targetSdk = Integer.parseInt(usesSdk.attr("targetSdkVersion"));
            } catch (NumberFormatException | NullPointerException e) {
                targetSdk = -1;
            }
        }
        if (targetSdk > 0) {
            interpreter.setSdkInt(targetSdk);
        }
        java.nio.file.Path jar = Sdk.findJar(targetSdk);
        if (jar == null) {
            System.out.println("framework: no SDK android.jar (hierarchy unavailable)");
            return null;
        }
        try {
            FwIndex framework = FwIndex.loadJar(jar);
            System.out.println("framework: " + jar.getParent().getFileName() + "/"
                    + jar.getFileName() + " (" + framework.classCount() + " classes)");
            return framework;
        } catch (Exception e) {
            System.out.println("framework: cannot read " + jar + " (" + e.getMessage() + ")");
            return null;
        }
    }

    private static String findLauncher(BinaryXml.Element manifest) {
        BinaryXml.Element app = manifest.child("application");
        if (app == null) {
            return null;
        }
        for (BinaryXml.Element child : app.children) {
            if (!child.name.equals("activity") && !child.name.equals("activity-alias")) {
                continue;
            }
            for (BinaryXml.Element filter : child.children) {
                if (!filter.name.equals("intent-filter")) {
                    continue;
                }
                boolean main = false;
                boolean launcher = false;
                for (BinaryXml.Element part : filter.children) {
                    if (part.name.equals("action")
                            && "android.intent.action.MAIN".equals(part.attr("name"))) {
                        main = true;
                    }
                    if (part.name.equals("category")
                            && "android.intent.category.LAUNCHER".equals(part.attr("name"))) {
                        launcher = true;
                    }
                }
                if (main && launcher) {
                    return child.attr("name");
                }
            }
        }
        return null;
    }

    private static String qualify(String packageName, String name) {
        if (name == null) {
            return null;
        }
        if (name.startsWith(".")) {
            return packageName + name;
        }
        if (!name.contains(".")) {
            return packageName + "." + name;
        }
        return name;
    }
}
