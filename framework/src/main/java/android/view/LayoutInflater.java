package android.view;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.xmlpull.v1.XmlPullParser;

/**
 * Tartine: the XML-to-view inflater, ported from AOSP
 * {@code android.view.LayoutInflater}. Reads a compiled layout document
 * (through {@code Resources.getLayout}, which the host backs with
 * {@code tartine.apk.BinaryXml}) and builds the view tree: element names map
 * to widget classes, {@code Factory}/{@code Factory2} hooks and the app's
 * {@code onCreateView} get first refusal, and each child's layout params come
 * from the parent container.
 *
 * <p>The platform's {@code PhoneLayoutInflater} name mapping is folded into
 * {@link #onCreateView(String, AttributeSet)}: a name without a package
 * prefix is tried against {@code android.widget.}, {@code android.webkit.}
 * and {@code android.app.}. Guest widget classes (androidx, an app's own
 * views) load through the thread context class loader the launcher installs.
 */
public class LayoutInflater {

    private static final String[] CLASS_PREFIXES = {
        "android.widget.", "android.webkit.", "android.app."
    };

    private final Context mContext;
    private LayoutInflater.Factory mFactory;
    private LayoutInflater.Factory2 mFactory2;
    private LayoutInflater.Filter mFilter;

    /**
     * The shell's inflater is the PhoneLayoutInflater equivalent (it carries
     * the platform name prefixes); AOSP's concrete subclass has a public
     * {@code (Context)} constructor, so widening this one keeps the shell's
     * service registry able to build it without recursing through
     * {@link #from(Context)}.
     */
    public LayoutInflater(Context context) {
        mContext = context;
    }

    protected LayoutInflater(LayoutInflater original, Context newContext) {
        mContext = newContext;
        mFactory = original.mFactory;
        mFactory2 = original.mFactory2;
        mFilter = original.mFilter;
    }

    public static LayoutInflater from(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            inflater = new LayoutInflater(context);
        }
        return inflater;
    }

    public LayoutInflater cloneInContext(Context newContext) {
        return new LayoutInflater(this, newContext);
    }

    public Context getContext() {
        return mContext;
    }

    public final LayoutInflater.Factory getFactory() {
        return mFactory;
    }

    public final LayoutInflater.Factory2 getFactory2() {
        return mFactory2;
    }

    public LayoutInflater.Filter getFilter() {
        return mFilter;
    }

    public void setFactory(LayoutInflater.Factory factory) {
        if (mFactory2 != null) {
            throw new IllegalStateException(
                    "A factory has already been set on this LayoutInflater");
        }
        mFactory2 = null;
        mFactory = factory;
    }

    public void setFactory2(LayoutInflater.Factory2 factory) {
        mFactory2 = factory;
        mFactory = null;
    }

    public void setFilter(LayoutInflater.Filter filter) {
        mFilter = filter;
    }

    public View inflate(int resource, ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        XmlResourceParser parser = getContext().getResources().getLayout(resource);
        try {
            return inflate(parser, root, attachToRoot);
        } finally {
            parser.close();
        }
    }

    public View inflate(XmlPullParser parser, ViewGroup root) {
        return inflate(parser, root, root != null);
    }

    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        synchronized (this) {
            try {
                AttributeSet attrs = asAttributes(parser);
                int type;
                while ((type = parser.next()) != XmlPullParser.START_TAG
                        && type != XmlPullParser.END_DOCUMENT) {
                    // skip text and processing instructions
                }
                if (type != XmlPullParser.START_TAG) {
                    throw new RuntimeException(
                            parser.getPositionDescription() + ": No start tag found!");
                }
                String name = parser.getName();
                if ("merge".equals(name)) {
                    if (root == null || !attachToRoot) {
                        throw new RuntimeException(
                                "<merge /> can be used only with a valid "
                                        + "ViewGroup root and attachToRoot=true");
                    }
                    rInflate(parser, root, attrs, false);
                    return root;
                }
                View temp = createViewFromTag(root, name, attrs);
                ViewGroup.LayoutParams params = null;
                if (root != null) {
                    params = root.generateLayoutParams(attrs);
                    if (!attachToRoot) {
                        temp.setLayoutParams(params);
                    }
                }
                rInflateChildren(parser, temp, attrs, true);
                if (root != null && attachToRoot) {
                    root.addView(temp, params);
                }
                return root == null || !attachToRoot ? temp : root;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Error inflating XML", e);
            }
        }
    }

    private void rInflateChildren(XmlPullParser parser, View parent,
            AttributeSet attrs, boolean finishInflate) throws Exception {
        rInflate(parser, parent, attrs, finishInflate);
    }

    /**
     * Recursive descent over the element's children, mirroring AOSP
     * {@code LayoutInflater.rInflate}: handles {@code <include>},
     * {@code <requestFocus>}, {@code <tag>} and {@code <merge>} specially,
     * and otherwise creates and attaches each child.
     */
    private void rInflate(XmlPullParser parser, View parent, AttributeSet attrs,
            boolean finishInflate) throws Exception {
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != XmlPullParser.END_TAG
                || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("requestFocus".equals(name)) {
                View focus = parent;
                if (focus instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) focus;
                    if (group.getChildCount() > 0) {
                        group.getChildAt(0).requestFocus();
                    }
                }
                continue;
            }
            if ("tag".equals(name)) {
                continue;
            }
            if ("include".equals(name)) {
                // Include handling requires resolving the layout attribute;
                // left to a later step, the include contributes no children.
                continue;
            }
            if ("merge".equals(name)) {
                rInflate(parser, parent, asAttributes(parser), finishInflate);
                continue;
            }
            View view = createViewFromTag(parent, name, asAttributes(parser));
            ViewGroup group = parent instanceof ViewGroup ? (ViewGroup) parent : null;
            ViewGroup.LayoutParams params = null;
            if (group != null) {
                params = group.generateLayoutParams(asAttributes(parser));
            }
            rInflateChildren(parser, view, asAttributes(parser), true);
            if (group != null) {
                group.addView(view, params);
            }
        }
        if (finishInflate && parent instanceof ViewGroup) {
            ((ViewGroup) parent).onFinishInflate();
        }
    }

    /**
     * AOSP {@code LayoutInflater.createViewFromTag}: a factory gets first
     * refusal, then the platform {@code onCreateView} resolution runs.
     */
    private View createViewFromTag(View parent, String name, AttributeSet attrs) {
        String viewName = name;
        if ("view".equals(viewName)) {
            viewName = attrs == null ? null
                    : attrs.getAttributeValue(null, "class");
        }
        if (viewName == null) {
            throw new RuntimeException("Unable to inflate a <view> without a class attribute");
        }
        if (mFactory2 != null) {
            View view = mFactory2.onCreateView(parent, viewName, mContext, attrs);
            if (view != null) {
                return view;
            }
        }
        if (mFactory != null) {
            View view = mFactory.onCreateView(viewName, mContext, attrs);
            if (view != null) {
                return view;
            }
        }
        return onCreateView(parent, viewName, attrs);
    }

    public final View createView(String name, String prefix, AttributeSet attrs) {
        return createView(mContext, name, prefix, attrs);
    }

    public final View createView(Context context, String name, String prefix,
            AttributeSet attrs) {
        // A factory gets first refusal.
        if (mFactory2 != null) {
            View view = mFactory2.onCreateView(null, name, context, attrs);
            if (view != null) {
                return view;
            }
        }
        if (mFactory != null) {
            View view = mFactory.onCreateView(name, context, attrs);
            if (view != null) {
                return view;
            }
        }
        return onCreateView(null, name, attrs);
    }

    /**
     * AOSP {@code PhoneLayoutInflater.onCreateView}: resolves simple names
     * against the platform prefixes, then falls back to the fully-qualified
     * name loaded through the guest class loader.
     */
    protected View onCreateView(View parent, String name, AttributeSet attrs) {
        if (name.indexOf('.') == -1) {
            for (String prefix : CLASS_PREFIXES) {
                View view = tryCreate(prefix + name, attrs);
                if (view != null) {
                    return view;
                }
            }
        }
        View view = tryCreate(name, attrs);
        if (view == null) {
            throw new RuntimeException(
                    "Unable to inflate view: " + name + " (no two-argument "
                            + "(Context, AttributeSet) constructor found)");
        }
        return view;
    }

    protected View onCreateView(String name, AttributeSet attrs) {
        return onCreateView(null, name, attrs);
    }

    public View onCreateView(Context context, View parent, String name,
            AttributeSet attrs) {
        return onCreateView(parent, name, attrs);
    }

    /** Instantiates a widget via its {@code (Context, AttributeSet)} ctor. */
    private View tryCreate(String className, AttributeSet attrs) {
        Class<?> clazz = loadClass(className);
        if (clazz == null || !View.class.isAssignableFrom(clazz)) {
            return null;
        }
        if (mFilter != null && !mFilter.onLoadClass(clazz)) {
            return null;
        }
        try {
            Constructor<?> ctor = clazz.getConstructor(
                    Context.class, AttributeSet.class);
            return (View) ctor.newInstance(mContext, attrs);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate view " + className, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException("Could not instantiate view " + className, cause);
        }
    }

    private static Class<?> loadClass(String className) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = LayoutInflater.class.getClassLoader();
        }
        try {
            return Class.forName(className, true, loader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static AttributeSet asAttributes(XmlPullParser parser) {
        return parser instanceof AttributeSet ? (AttributeSet) parser : null;
    }

    /** Scratch mirror constructor (SkeletonGen). */
    public LayoutInflater() {
        this(null);
    }

    public static interface Factory2 extends LayoutInflater.Factory {

        public abstract View onCreateView(View arg0, String arg1, Context arg2,
                AttributeSet arg3);
    }

    public static interface Factory {

        public abstract View onCreateView(String arg0, Context arg1, AttributeSet arg2);
    }

    public static interface Filter {

        public abstract boolean onLoadClass(Class arg0);
    }
}
