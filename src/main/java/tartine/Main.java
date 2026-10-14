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

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
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
        String activity = runOnCreate(apk);
        if (activity == null) {
            System.out.println("gui: onCreate did not return; no window");
            return;
        }
        DesktopWindow.show(activity, timeoutSecs);
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
            FwIndex framework = loadFramework(manifest);
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
            interpreter.setMaxSteps(100000000L);
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
                System.out.println("content lambda: no getValue to read it");
                return;
            }
            Object lambda = interpreter.invoke(stateObject.type, getValue, stateObject);
            System.out.println("content lambda: " + (lambda instanceof VmObject
                    ? ((VmObject) lambda).type.type : lambda));
        } catch (RuntimeException e) {
            System.out.println("content lambda: unreadable (" + e.getMessage() + ")");
        }
    }

    private static FwIndex loadFramework(BinaryXml.Element manifest) {
        int targetSdk = -1;
        BinaryXml.Element usesSdk = manifest.child("uses-sdk");
        if (usesSdk != null) {
            try {
                targetSdk = Integer.parseInt(usesSdk.attr("targetSdkVersion"));
            } catch (NumberFormatException | NullPointerException e) {
                targetSdk = -1;
            }
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
