package tartine;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Looper;
import android.view.Choreographer;
import android.view.View;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;
import tartine.apk.Abi;
import tartine.apk.ApkReader;
import tartine.apk.AppIcon;
import tartine.apk.BinaryXml;
import tartine.config.HostConfig;
import tartine.dex.Dex;
import tartine.dex.DexClass;
import tartine.dex.DexDump;
import tartine.dex.DexSet;
import tartine.fw.DisplayConfig;
import tartine.gfx.GfxBackend;
import tartine.gfx.skija.SkijaBackend;
import tartine.graphics.GuestSurface;
import tartine.graphics.HostDisplay;
import tartine.graphics.HostDisplays;
import tartine.graphics.RenderLoop;
import tartine.graphics.WindowIcon;
import tartine.runtime.Vfs;
import tartine.tx.ReferenceResolver;
import tartine.tx.TranslatedLoader;
import tartine.tx.TxTranslator;

/**
 * Entry point.
 *
 * <ul>
 * <li>{@code apk <file.apk>} — headless APK summary: manifest, code, libraries.
 * <li>{@code run <file.apk>} — headless probe through the translation loader
 * ({@code tartine.tx}: real JVM bytecode, framework module classes, host JDK),
 * executing the launcher activity's {@code onCreate} until the first gap.
 * <li>{@code gui <file.apk> [timeoutSecs]} — the same translated launch, then
 * shows the activity window on desktop. Guest code runs on its main-looper
 * thread and posts the finished raster through {@code HostDisplay} to the
 * host UI thread that owns GLFW. {@code txrun}/{@code txgui} are kept as
 * aliases.
 * <li>{@code dump <file.dex>} — prints our instruction dump of a dex file.
 * {@code txdump} is kept as an alias for the translated-class dump.
 * <li>{@code refs <file.apk>} — unresolved-reference census over every dex
 * class, before any translation.
 * </ul>
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        HostConfig.load();
        // Renderer backend: installed once at boot, before any framework
        // graphics call (text measurement included). The framework resolves
        // it through GfxBackend.get() and never imports Skija.
        GfxBackend.install(
                new SkijaBackend());
        // Seed the default virtual sdcard (no-op when TARTINE_SDCARD or
        // sdcard.mount already configured an explicit mount).
        Vfs.ensureDefaultCard();
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
                    gui(new File(args[1]),
                            args.length > 2 ? Long.parseLong(args[2]) : 0);
                    break;
                // Backward-compatible aliases for the old translation task names.
                case "txrun":
                    require(args, 2);
                    txRun(new File(args[1]));
                    break;
                case "txgui":
                    require(args, 2);
                    gui(new File(args[1]),
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
                case "refs":
                    require(args, 2);
                    printReferenceProblems(new File(args[1]));
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
        System.out.println("Usage: tartine.Main (apk|run|gui|dump|refs) <file>");
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
     * Reference census ({@code refs}): resolves every method/field/type
     * reference and every class hierarchy edge against guest dex first,
     * then host reflection, printing what resolves nowhere.
     */
    private static void printReferenceProblems(File apk) throws Exception {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            TranslatedLoader loader = new TranslatedLoader(
                    Main.class.getClassLoader(), reader.dexSet());
            List<ReferenceResolver.Problem> problems = loader.referenceProblems();
            for (ReferenceResolver.Problem problem : problems) {
                System.out.println(problem);
            }
            System.out.println("refs: " + problems.size() + " unresolved reference(s)");
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
            TranslatedLoader loader = new TranslatedLoader(
                    Main.class.getClassLoader(), dexes);
            String desc = dottedOrSlash.contains("/")
                    ? dottedOrSlash
                    : dottedOrSlash.replace('.', '/');
            byte[] bytes;
            if (methodFilter != null) {
                DexClass cls =
                        loader.dexLookup().apply("L" + desc + ";");
                bytes = TxTranslator.emitProbeSingle(cls,
                        loader.fieldSynths(), loader.guestInterfaces(),
                        loader.dexLookup(), loader.getParent(), methodFilter);
            } else {
                bytes = loader.translateClass(desc);
            }
            TraceClassVisitor printer =
                    new TraceClassVisitor(new PrintWriter(
                            System.err));
            new ClassReader(bytes).accept(printer, 0);
        }
    }

    /** Launcher activity loaded through the translated loader, plus the
     *  still-open APK reader the loader's lazy per-class translation needs. */
    private record TxApp(ApkReader reader, TranslatedLoader loader,
            Object activity) {
    }

    /**
     * Translated-run probe ({@code run}): launcher {@code <init>} then
     * {@code onCreate} as real JVM bytecode. The framework module supplies
     * android.* skeletons (UOE bodies = the gap census); the dex classes come
     * from {@link tartine.tx.TranslatedLoader}.
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
        TranslatedLoader loader = new TranslatedLoader(
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
        Looper.prepareMainLooper();
        Object receiver;
        try {
            receiver = task.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | LinkageError e) {
            System.out.println("<init>: " + sanitize(e));
            printRootFrames(e);
            return new TxApp(reader, loader, null);
        }
        Method onCreate;
        try {
            // Android Activity.onCreate(Bundle) is protected — getMethod
            // (public only) misses it; walk the chain with accessibility.
            onCreate = null;
            for (Class<?> c = task; c != null && onCreate == null;
                    c = c.getSuperclass()) {
                try {
                    onCreate = c.getDeclaredMethod("onCreate",
                            Bundle.class);
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
        } catch (InvocationTargetException x) {
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
     * Desktop entry point for {@code gui}/{@code txgui}: start the host
     * display seam (GLFW on the host UI thread), then run the translated
     * guest pipeline on the guest main-looper thread. The window title and
     * icons are resolved before the guest loads so the display thread is
     * ready first.
     */
    private static void gui(File apk, long timeoutSecs) throws Exception {
        HostDisplays.run(launcherTitle(apk), timeoutSecs, loadWindowIcons(apk),
                display -> txGuiRun(apk, display));
    }

    /**
     * Translated GUI probe ({@code gui}): run the launcher through {@code
     * TranslatedLoader}, then drive the compose pipeline (createComposition,
     * setContent, attach owners, lifecycle to RESUMED) with plain reflection on
     * the translated classes, and hand the first frame to the host display.
     * Gaps print; a failed draw is logged and stops the render loop instead of
     * retrying a wedged traversal (the partial raster is not presented).
     */
    private static void txGuiRun(File apk, HostDisplay display)
            throws Exception {
        // The window exists before guest code runs: its framebuffer is the
        // guest pixel viewport, and its content scale is the guest density,
        // so Resources (created lazily by the launched activity) sees the
        // real display instead of the fixed 480x960 mdpi default.
        HostDisplay.WindowMetrics metrics = display.awaitMetrics();
        DisplayConfig.set(metrics.width(), metrics.height(),
                metrics.contentScale());
        System.out.println("gui: viewport " + metrics.width() + "x"
                + metrics.height() + " px @ " + metrics.contentScale()
                + "x scale (" + DisplayConfig.screenWidthDp() + "x"
                + DisplayConfig.screenHeightDp() + " dp, "
                + DisplayConfig.densityDpi() + " dpi)");
        TxApp app = txLaunch(apk);
        GuestSurface target = new GuestSurface(metrics.width(),
                metrics.height());
        try {
            if (app.activity() == null) {
                System.out.println("gui: launch failed; no window");
                return;
            }
            Looper looper = Looper.myLooper();
            if (looper == null) {
                System.out.println("gui: no guest looper; no event pump");
                return;
            }
            // The display publishes events on its own thread; this listener
            // only posts them to the guest queue, which the render loop
            // drains on the guest main-looper thread.
            AtomicBoolean windowClosed = new AtomicBoolean();
            display.setEventListener(
                    event -> postHostEvent(looper, windowClosed, target,
                            event));
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
            // Attach parity: joinWindow marks the content view attached at
            // setContentView time; the host shell's ViewRootImpl-style attach
            // chain has not landed, so the driver stands in and sets the
            // AttachInfo ref the same way.
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
            // content to the inner composition.
            txInvoke(composition, "setContent", lambda);
            System.out.println("gui: post-attach setContent returned");
            renderLoop(looper, display, owner, target, windowClosed);
            System.out.println("gui: render loop stopped");
        } finally {
            // The render loop closes the target; close again for early
            // returns and setup failures (GuestSurface.close is idempotent).
            target.close();
            app.reader().close();
        }
    }

    /**
     * Posts one host event to the guest main-looper queue. Runs on the
     * display thread, so it must only enqueue. The queued action runs on the
     * guest thread: a resize replaces the raster target there (the render
     * loop then notices the new pixel size and redraws), and close quits the
     * guest loop.
     */
    private static void postHostEvent(Looper looper, AtomicBoolean windowClosed,
            GuestSurface target, HostDisplay.Event event) {
        looper.getQueue().post(() -> {
            if (event instanceof HostDisplay.Closed) {
                System.out.println("gui: window close event");
                windowClosed.set(true);
                looper.quit();
                return;
            }
            if (event instanceof HostDisplay.Resized resized) {
                DisplayConfig.setSize(resized.width(), resized.height());
                boolean changed = target.resize(resized.width(),
                        resized.height());
                System.out.println("gui: window resized to "
                        + resized.width() + "x" + resized.height()
                        + (changed ? "" : " (unchanged)"));
                return;
            }
            System.out.println("gui: host event " + event);
        });
    }

    /**
     * The desktop VSync pass at the configured refresh rate
     * ({@code display.refresh_hz}, 60 Hz by default), driven by
     * {@link RenderLoop}: the loop owns draw-when-due pacing, stop rules and
     * readback reuse; this method only supplies the guest-side source (queue
     * drain, Choreographer pump, dirty check, measure/layout/draw).
     */
    private static void renderLoop(Looper looper, HostDisplay display,
            Object owner, GuestSurface target, AtomicBoolean windowClosed) {
        RenderLoop.FrameSource frames = new RenderLoop.FrameSource() {
            @Override
            public void drain() {
                drainGuestQueue(looper);
            }

            @Override
            public int pumpCallbacks(long frameTimeNanos) {
                return Choreographer.pumpFrameCallbacks(frameTimeNanos);
            }

            @Override
            public boolean needsRedraw() {
                return owner instanceof View view
                        && view.tartineConsumeDirty();
            }

            @Override
            public void draw() {
                txDrawFrame(owner, target);
            }

            @Override
            public boolean isClosed() {
                return windowClosed.get();
            }
        };
        new RenderLoop(display, target, frames,
                HostConfig.framePeriodNanos(), HostConfig.debugFrames(),
                System.out).run();
    }

    /** Runs queued guest actions (input, close) between frames. */
    private static void drainGuestQueue(Looper looper) {
        Runnable action;
        while ((action = looper.getQueue().poll()) != null) {
            action.run();
        }
    }

    /**
     * Measure, layout, then render the composed root into the target's
     * current surface (the desktop twin of the device's VSync traversal);
     * the caller reads the surface back. Dimensions come from the target
     * every frame, so a resize is honored on the next draw.
     */
    private static void txDrawFrame(Object owner, GuestSurface target) {
        int width = target.width();
        int height = target.height();
        txInvoke(owner, "onMeasure", width | (1 << 30),
                height | (1 << 30));
        txInvoke(owner, "onLayout", false, 0, 0, width, height);
        Canvas canvas = target.canvas();
        canvas.drawColor(0xFF000000);
        txInvoke(owner, "dispatchDraw", canvas);
    }

    /** Reads a private field walking the receiver's runtime class chain. */
    private static Object txField(Object owner, String name) {
        try {
            for (Class<?> c = owner.getClass(); c != null;
                    c = c.getSuperclass()) {
                try {
                    Field f = c.getDeclaredField(name);
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
                    Field f = c.getDeclaredField(name);
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
                for (Method m : c.getDeclaredMethods()) {
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
            if (e instanceof InvocationTargetException x) {
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
    private static Object txInvokeStatic(TranslatedLoader loader,
            String className, String name, Object... args) {
        try {
            Class<?> cls = loader.loadClass(className);
            for (Method m : cls.getDeclaredMethods()) {
                boolean stat = Modifier.isStatic(
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
            if (e instanceof InvocationTargetException x) {
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
     * Window title before the guest class is loaded: the same launcher
     * resolution {@code txLaunch} uses, so the display can open first.
     */
    private static String launcherTitle(File apk) {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            BinaryXml.Element manifest = reader.manifest();
            String activity = findLauncher(manifest);
            if (activity == null) {
                return apk.getName();
            }
            return qualify(manifest.attr("package"), activity);
        } catch (IOException error) {
            System.out.println("gui: launcher title unavailable ("
                    + error.getMessage() + ")");
            return apk.getName();
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
