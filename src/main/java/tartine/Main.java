package tartine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import tartine.apk.Abi;
import tartine.apk.ApkReader;
import tartine.apk.BinaryXml;
import tartine.dex.Dex;
import tartine.dex.DexClass;
import tartine.dex.DexDump;
import tartine.dex.DexMethod;
import tartine.dex.DexSet;
import tartine.fw.FwIndex;
import tartine.fw.Sdk;
import tartine.graphics.DesktopWindow;
import tartine.vm.Interpreter;
import tartine.vm.VmException;
import tartine.vm.VmObject;
import tartine.vm.VmThrow;

/**
 * Entry point.
 *
 * <ul>
 * <li>{@code apk <file.apk>} — headless APK summary: manifest, code, libraries.
 * <li>{@code run <file.apk>} — headless probe: executes the launcher
 * activity's {@code onCreate} until the first gap (no GUI yet).
 * <li>{@code gui <file.apk> [timeoutSecs]} — headless {@code onCreate}, then
 * shows the activity window on desktop (blank until composition runs).
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
    private static io.github.humbleui.skija.Surface firstDrawFrame;

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        tartine.config.HostConfig.load();
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
                    probeRun(new File(args[1]));
                    break;
                case "gui":
                    require(args, 2);
                    guiRun(new File(args[1]),
                            args.length > 2 ? Long.parseLong(args[2]) : 0);
                    break;
                case "dump":
                    require(args, 2);
                    dumpDex(new File(args[1]));
                    break;
                default:
                    if (args[0].endsWith(".apk")) {
                        printApkSummary(new File(args[0]));
                    } else {
                        dumpDex(new File(args[0]));
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("tartine: " + e);
            System.exit(1);
        }
    }

    private static void usage() {
        System.out.println("Usage: tartine.Main (apk|run|dump) <file>");
    }

    private static void require(String[] args, int count) {
        if (args.length < count) {
            throw new IllegalArgumentException("missing file argument");
        }
    }

    private static void dumpDex(File file) throws Exception {
        Dex dex = Dex.load(file);
        for (DexClass cls : dex.classes()) {
            System.out.print(DexDump.dump(cls));
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

    private static void probeRun(File apk) throws Exception {
        runOnCreate(apk);
    }

    private static void guiRun(File apk, long timeoutSecs) throws Exception {
        firstDrawFrame = null;
        String activity = runOnCreate(apk);
        if (activity == null) {
            System.out.println("gui: onCreate did not return; no window");
            return;
        }
        System.out.println("gui: window ready (frame: "
                + (firstDrawFrame != null ? "present" : "absent") + ")");
        DesktopWindow.show(activity, timeoutSecs, firstDrawFrame);
        System.out.println("window closed");
    }

    /**
     * Headless probe shared by {@code run} and {@code gui}: executes the
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
        io.github.humbleui.skija.Surface surface =
                tartine.graphics.SkiaPresenter.createSurface(480, 960);
        android.graphics.Canvas hostCanvas =
                tartine.graphics.SkiaPresenter.createSurfaceCanvas(surface);
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
            int[] pixels = tartine.graphics.SkiaPresenter.readPixels(surface, 480, 960);
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
