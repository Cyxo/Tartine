package tartine;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Looper;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewRootImpl;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
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
import tartine.fw.PermissionManager;
import tartine.fw.PermissionPrompt;
import tartine.gfx.GfxBackend;
import tartine.gfx.skija.SkijaBackend;
import tartine.graphics.GuestSurface;
import tartine.graphics.HostDisplay;
import tartine.graphics.HostDisplays;
import tartine.graphics.RenderLoop;
import tartine.graphics.WindowIcon;
import tartine.runtime.Vfs;
import tartine.tx.ReferenceResolver;
import tartine.tx.StrictAudit;
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
 * <li>{@code strict <file.apk> [classFilter]} — strict translation audit:
 * translates every class (or descriptor substring matches) and fails on any
 * translator gap while aggregating expected sandbox denials.
 * </ul>
 */
public final class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    /** Runs the CLI subcommand named by the first argument. */
    public static void main(String[] args) {
        final String cliLogLevel = parseLogLevelArg(args);
        java.util.List<String> kept = new java.util.ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--log-level") || arg.equals("-l")) {
                i++; // skip the value already captured
            } else if (arg.startsWith("--log-level=")) {
                // value attached; already captured
            } else {
                kept.add(arg);
            }
        }
        String[] parsed = kept.toArray(new String[0]);
        args = parsed;
        if (args.length == 0) {
            usage();
            return;
        }
        if (cliLogLevel != null) {
            System.setProperty("tartine.log.level", cliLogLevel);
        }
        HostConfig.load();
        HostConfig.configureLogging(cliLogLevel);
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
                case "strict":
                    require(args, 2);
                    strictCheck(new File(args[1]),
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
            log.log(Level.SEVERE, "tartine", e);
            System.exit(1);
        }
    }

    /** Pulls {@code --log-level/-l} from argv before command dispatch. */
    private static String parseLogLevelArg(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--log-level") || arg.equals("-l")) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
            } else if (arg.startsWith("--log-level=")) {
                return arg.substring("--log-level=".length());
            }
        }
        return null;
    }

    private static void usage() {
        printResult("Usage: tartine.Main (apk|run|gui|strict|dump|refs) <file>");
    }

    /**
     * Command results go to stdout as plain text; diagnostics stay on the
     * JUL logger (stderr), so a piped result is never filtered by log.level.
     */
    private static void printResult(String text) {
        System.out.print(text);
        if (!text.endsWith("\n")) {
            System.out.println();
        }
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
                    printResult(DexDump.dump(cls));
                }
            }
        }
    }

    private static void printApkSummary(File apk) throws Exception {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            BinaryXml.Element manifest = reader.manifest();
            printResult("package: " + manifest.attr("package"));
            printResult("version: " + manifest.attr("versionCode")
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
            printResult("activities (" + activities.size() + "): " + activities);
            printResult("permissions: " + permissions);
            DexSet dexes = reader.dexSet();
            printResult("dex files: " + reader.dexEntries().size()
                    + ", classes: " + dexes.classCount()
                    + ", methods: " + dexes.methodCount());
            Set<String> abis = new TreeSet<>(reader.nativeAbis());
            printResult("native abis: " + (abis.isEmpty() ? "none" : abis)
                    + " (host picks " + Abi.pickBest(abis) + ")");
            printResult("resources: " + reader.hasResources());
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
                printResult(String.valueOf(problem));
            }
            printResult("refs: " + problems.size() + " unresolved reference(s)");
        }
    }

    /**
     * Strict translation audit ({@code strict}): translates the whole APK
     * with gap degradation disabled, prints translator gaps individually and
     * sandbox denials as an aggregate, and fails the command when a
     * translator gap remains.
     */
    private static void strictCheck(File apk, String classFilter)
            throws Exception {
        try (ApkReader reader = ApkReader.open(apk.toPath())) {
            DexSet dexes = reader.dexSet();
            TranslatedLoader loader = new TranslatedLoader(
                    Main.class.getClassLoader(), dexes);
            StrictAudit.Result result = StrictAudit.run(
                    dexes, loader, classFilter);
            printResult("strict: " + result.audited() + " of "
                    + result.classes() + " class(es) audited, "
                    + result.translated() + " translated, "
                    + result.classDenials()
                    + " class-level sandbox denial(s)");
            printResult("strict: " + result.translatorGaps().size()
                    + " translator gap(s)"
                    + (result.translatorGaps().isEmpty() ? "" : ":"));
            for (TxTranslator.TxGap.Degradation gap
                    : result.translatorGaps()) {
                printResult("  " + gap.signature() + ": " + gap.reason());
            }
            int methodDenials = result.sandboxDenials().values().stream()
                    .mapToInt(Integer::intValue).sum();
            printResult("strict: " + methodDenials
                    + " method-level sandbox denial(s)");
            result.sandboxDenials().forEach((reason, count) ->
                    printResult("  " + count + "x " + reason));
            if (!result.clean()) {
                throw new IllegalStateException(
                        "strict: translator gaps found (see above)");
            }
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
            StringWriter dump = new StringWriter();
            try (PrintWriter writer = new PrintWriter(dump)) {
                TraceClassVisitor printer = new TraceClassVisitor(writer);
                new ClassReader(bytes).accept(printer, 0);
            }
            printResult(dump.toString());
        }
    }

    /** Launcher activity loaded through the translated loader, plus the
     *  still-open APK reader the loader's lazy per-class translation needs. */
    private record TxApp(ApkReader reader, TranslatedLoader loader,
            Activity activity) {
    }

    /**
     * Translated-run probe ({@code run}): launcher {@code <init>}, then the
     * framework-driven create phase (pre/post lifecycle callbacks and
     * {@code onCreate}) as real JVM bytecode. The framework module supplies
     * android.* skeletons (UOE bodies = the gap census); the dex classes come
     * from {@link tartine.tx.TranslatedLoader}.
     */
    private static void txRun(File apk) throws Exception {
        TxApp app = txLaunch(apk);
        try {
            if (app.activity() == null) {
                return;
            }
            app.activity().performCreate(null);
            log.info("onCreate returned");
        } catch (RuntimeException | Error e) {
            log.info("onCreate threw: " + sanitize(e));
            printRootFrames(e);
        } finally {
            app.reader().close();
        }
    }

    /**
     * Translated launch: loader bring-up, main Looper, launcher {@code
     * <init>}. The host driver runs the activity lifecycle afterwards
     * ({@code Activity.performCreate} and friends, AOSP ActivityThread
     * parity). Keeps the APK reader open (the loader translates classes
     * lazily); callers close {@code TxApp.reader()} when done. Failures
     * print and come back with a null activity.
     */
    private static TxApp txLaunch(File apk) throws Exception {
        ApkReader reader = ApkReader.open(apk.toPath());
        BinaryXml.Element manifest = reader.manifest();
        String packageName = manifest.attr("package");
        configurePermissions(reader, packageName);
        String activity = findLauncher(manifest);
        if (activity == null) {
            log.info("no launcher activity in manifest");
            return new TxApp(reader, null, null);
        }
        activity = qualify(packageName, activity);
        log.info("launcher: " + activity);
        DexSet dexes = reader.dexSet();
        log.info("dex classes: " + dexes.classCount());
        TranslatedLoader loader = new TranslatedLoader(
                Main.class.getClassLoader(), dexes);
        Thread.currentThread().setContextClassLoader(loader);
        Class<?> task;
        try {
            task = loader.loadClass(activity);
        } catch (ClassNotFoundException | LinkageError | RuntimeException e) {
            log.info("load " + activity + ": " + sanitize(e));
            printRootFrames(e);
            return new TxApp(reader, loader, null);
        }
        log.info("loaded: " + task.getName());
        // Device parity with ActivityThread.main(): the guest's main
        // thread carries the main Looper before any Activity code runs.
        Looper.prepareMainLooper();
        Activity receiver;
        try {
            receiver = (Activity) task.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | LinkageError
                | ClassCastException e) {
            log.info("<init>: " + sanitize(e));
            printRootFrames(e);
            return new TxApp(reader, loader, null);
        }
        return new TxApp(reader, loader, receiver);
    }

    /**
     * Host half of the permission/consent layer: publish the manifest's
     * requested permissions and target SDK, and point the grant store at
     * {@code <Tartine root>/permissions/<package>.properties}. Consent is a
     * fidelity feature, not an enforcement boundary (ADR-0012).
     */
    private static void configurePermissions(ApkReader reader,
            String packageName) throws IOException {
        Path store = Vfs.root().resolve("permissions")
                .resolve(sanitizePackage(packageName) + ".properties");
        PermissionManager.configure(packageName,
                reader.declaredPermissions(), reader.targetSdk(), store);
    }

    /** Filesystem-safe file stem for a package name; never empty. */
    private static String sanitizePackage(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return "app";
        }
        StringBuilder safe = new StringBuilder(packageName.length());
        for (int i = 0; i < packageName.length(); i++) {
            char c = packageName.charAt(i);
            boolean keep = c >= 'a' && c <= 'z'
                    || c >= 'A' && c <= 'Z'
                    || c >= '0' && c <= '9'
                    || c == '.' || c == '_' || c == '-';
            safe.append(keep ? c : '_');
        }
        return safe.toString();
    }

    /**
     * Desktop consent dialog for GUI runs, carrying the app's window icons
     * onto the prompt so it shows the app icon and flashes for attention.
     * Headless runs leave the prompt unset, so {@link PermissionManager}
     * answers denied without recording a decision and a later GUI run can
     * still ask.
     */
    private static PermissionPrompt desktopPrompt(List<WindowIcon> icons) {
        List<Image> images = ConsentDialog.toAwtImages(icons);
        return (packageName, permission, label) -> {
            if (GraphicsEnvironment.isHeadless()) {
                return PermissionPrompt.Decision.UNAVAILABLE;
            }
            AtomicReference<PermissionPrompt.Decision> answer =
                    new AtomicReference<>(PermissionPrompt.Decision.UNAVAILABLE);
            try {
                SwingUtilities.invokeAndWait(() -> answer.set(
                        ConsentDialog.ask(packageName, label, images)));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return PermissionPrompt.Decision.UNAVAILABLE;
            } catch (InvocationTargetException e) {
                log.log(Level.WARNING, "permission dialog failed", e);
                return PermissionPrompt.Decision.UNAVAILABLE;
            }
            return answer.get();
        };
    }

    /** Root-cause stack frames for a launch failure (debug only). */
    // The != is a self-cause identity guard, not value equality.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    private static void printRootFrames(Throwable source) {
        Throwable root = source;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        int frameDepth = 16;
        for (StackTraceElement frame : root.getStackTrace()) {
            log.info("    at " + frame);
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
        List<WindowIcon> icons = loadWindowIcons(apk);
        PermissionManager.setPrompt(desktopPrompt(icons));
        try {
            HostDisplays.run(launcherTitle(apk), timeoutSecs, icons,
                    display -> txGuiRun(apk, display));
        } finally {
            PermissionManager.setPrompt(null);
        }
    }

    /**
     * Translated GUI probe ({@code gui}): run the launcher through {@code
     * TranslatedLoader}, drive the ordinary framework lifecycle to RESUMED,
     * then attach the decor to a {@link ViewRootImpl} and render its
     * traversals into the host surface. Gaps print; a failed draw is logged
     * and stops the render loop instead of retrying a wedged traversal (the
     * partial raster is not presented).
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
        log.info("gui: viewport " + metrics.width() + "x"
                + metrics.height() + " px @ " + metrics.contentScale()
                + "x scale (" + DisplayConfig.screenWidthDp() + "x"
                + DisplayConfig.screenHeightDp() + " dp, "
                + DisplayConfig.densityDpi() + " dpi)");
        TxApp app = txLaunch(apk);
        GuestSurface target = new GuestSurface(metrics.width(),
                metrics.height());
        Activity activity = app.activity();
        ViewRootImpl root = null;
        try {
            if (activity == null) {
                log.info("gui: launch failed; no window");
                return;
            }
            Looper looper = Looper.myLooper();
            if (looper == null) {
                log.info("gui: no guest looper; no event pump");
                return;
            }
            // ActivityThread.handleLaunchActivity parity: create, start,
            // resume, then add the decor to the window manager. The
            // framework drives both Application-level and activity-local
            // lifecycle callbacks, which is how androidx.lifecycle reaches
            // ON_RESUME before the first composition.
            try {
                activity.performCreate(null);
                activity.performStart();
                activity.performResume();
            } catch (RuntimeException | Error e) {
                log.info("gui: lifecycle failed: " + sanitize(e));
                printRootFrames(e);
                return;
            }
            log.info("gui: lifecycle resumed");
            // ActivityThread.handleResumeActivity's wm.addView(decor):
            // a real attach handshake, so AbstractComposeView creates its
            // composition from onAttachedToWindow/onMeasure exactly as on
            // a device.
            root = new ViewRootImpl(activity);
            View decor = activity.getWindow().getDecorView();
            root.setView(decor);
            root.windowFocusChanged(true);
            log.info("gui: decor attached ("
                    + decor.getClass().getName() + ")");
            // The display publishes events on its own thread; this listener
            // only posts them to the guest queue, which the render loop
            // drains on the guest main-looper thread.
            AtomicBoolean windowClosed = new AtomicBoolean();
            HostInput input = new HostInput(root, metrics.contentScale());
            ViewRootImpl attached = root;
            display.setEventListener(event -> postHostEvent(looper,
                    windowClosed, target, attached, input, event));
            renderLoop(looper, display, root, target, windowClosed);
            log.info("gui: render loop stopped");
        } finally {
            if (root != null) {
                teardown(activity, root);
            }
            // The render loop closes the target; close again for early
            // returns and setup failures (GuestSurface.close is idempotent).
            target.close();
            app.reader().close();
        }
    }

    /**
     * Window removal parity: pause/stop, detach the decor, destroy. Desktop
     * close is best-effort — a guest gap in teardown must not mask the
     * original render-loop outcome.
     */
    private static void teardown(Activity activity, ViewRootImpl root) {
        try {
            activity.performPause();
            activity.performStop();
        } catch (RuntimeException | Error e) {
            log.info("gui: teardown pause/stop: " + sanitize(e));
        }
        root.die(false);
        try {
            activity.performDestroy();
        } catch (RuntimeException | Error e) {
            log.info("gui: teardown destroy: " + sanitize(e));
        }
    }

    /**
     * Posts one host event to the guest main-looper queue. Runs on the
     * display thread, so it must only enqueue. The queued action runs on the
     * guest thread: resize replaces the raster target and requests a new
     * traversal, close quits the guest loop, and the rest goes through the
     * {@link ViewRootImpl} input entry points.
     */
    private static void postHostEvent(Looper looper, AtomicBoolean windowClosed,
            GuestSurface target, ViewRootImpl root, HostInput input,
            HostDisplay.Event event) {
        looper.getQueue().post(() -> {
            if (event instanceof HostDisplay.Closed) {
                log.info("gui: window close event");
                windowClosed.set(true);
                looper.quit();
                return;
            }
            if (event instanceof HostDisplay.Resized resized) {
                DisplayConfig.setSize(resized.width(), resized.height());
                boolean changed = target.resize(resized.width(),
                        resized.height());
                root.requestLayout();
                log.info("gui: window resized to "
                        + resized.width() + "x" + resized.height()
                        + (changed ? "" : " (unchanged)"));
                return;
            }
            // Cursor moves/focus edges are very chatty; keep them (and
            // keyboard/mouse details) at debug so default INFO stays calm.
            log.fine("gui: host event " + event);
            input.handle(event);
        });
    }

    /**
     * The desktop VSync pass at the configured refresh rate
     * ({@code display.refresh_hz}, 60 Hz by default), driven by
     * {@link RenderLoop}: the loop owns draw-when-due pacing, stop rules and
     * readback reuse; this method only supplies the guest-side source (queue
     * drain, Choreographer pump, traversal check, measure/layout/draw).
     */
    private static void renderLoop(Looper looper, HostDisplay display,
            ViewRootImpl root, GuestSurface target,
            AtomicBoolean windowClosed) {
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
                return root.tartineNeedsTraversal();
            }

            @Override
            public void draw() {
                Canvas canvas = target.canvas();
                canvas.drawColor(0xFF000000);
                root.performTraversals(canvas, target.width(),
                        target.height());
            }

            @Override
            public boolean isClosed() {
                return windowClosed.get();
            }
        };
        new RenderLoop(display, target, frames,
                HostConfig.framePeriodNanos(), HostConfig.debugFrames(),
                System.err).run();
    }

    /** Runs queued guest actions (input, close) between frames. */
    private static void drainGuestQueue(Looper looper) {
        Runnable action;
        while ((action = looper.getQueue().poll()) != null) {
            action.run();
        }
    }

    /**
     * Translates a reflective/launch failure to one printable line: the
     * exception chain with host frames trimmed away.
     */
    // The == is a self-cause identity guard, not value equality.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    private static String sanitize(Throwable error) {
        StringBuilder text = new StringBuilder();
        for (Throwable t = error; t != null && text.length() < 2000;
                t = t.getCause()) {
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
            return AppIcon.load(reader, line -> log.fine(line));
        } catch (IOException error) {
            log.info("icon: cannot reopen apk ("
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
            log.info("gui: launcher title unavailable ("
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
