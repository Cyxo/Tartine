package tartine.apk;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.view.ViewBox;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxImage;
import tartine.graphics.WindowIcon;

/**
 * Resolves the launcher icon a desktop shell would show: the manifest's
 * {@code application android:icon} (fallback {@code android:roundIcon}),
 * followed through {@code resources.arsc} into {@code res/...}, rendered
 * into straight-alpha RGBA buffers at the sizes desktop shells ask for.
 *
 * <p>Bitmap sources (png/jpeg/webp) decode through the installed renderer
 * backend ({@link GfxBackend#decodeImage}). XML sources are
 * converted the way aapt2 emits them for icons: {@code <adaptive-icon>}
 * composes its layers, {@code <vector>} converts to SVG text, and jsvg
 * (MIT, Maven Central — the user-directed pick over hand-rolling SVG
 * semantics) renders at each target size. Android's vector {@code pathData}
 * is "exactly same format as the {@code d} attribute in SVG's path data"
 * (AOSP), so the ported subset is small: groups, paths, gradients,
 * clip-paths. Anything unsupported ({@code <inset>} nested layers,
 * {@code ?attr} theme colors, sweep gradients...) logs an {@code icon:}
 * gap line and yields no icon rather than a wrong-looking one.
 *
 * <p>Semantics mirrored from AOSP sources (never memory-ported):
 * <ul>
 * <li>{@code frameworks/base/graphics/java/android/graphics/drawable/
 *     VectorDrawable.java}, {@code <group>} documentation: "The
 *     transformations are applied in the order of scale, rotate then
 *     translate" with the pivot wrapped around scale and rotation alike.
 *     SVG applies transforms right-most first, so the ported group
 *     transform is {@code translate(t) translate(p) rotate(rot) scale(s)
 *     translate(-p)}.</li>
 * <li>{@code libs/androidfw/include/androidfw/ResourceTypes.h}, Res_value
 *     complex layout: unit bits 0-3, radix bits 4-5, mantissa bits 8+;
 *     value = {@code (data & 0xFFFFFF00) * RADIX_MULTS[radix]},
 *     {@code RADIX_MULTS = {0.00390625F, 3.051758E-05F, 1.1920929E-07F,
 *     7.629395E-06F}}. Confirmed against a real fixture: 108dp encodes
 *     {@code 0x6c01} (108&lt;&lt;8 | dip).</li>
 * <li>{@code GradientColor} inline resources: aapt2 splits
 *     {@code <aapt:attr>} gradient payloads into {@code $name__N.xml}
 *     drawables that paths reference by typed resource id.</li>
 * </ul>
 */
public final class AppIcon {
    /** Sizes a desktop shell asks for window icons, ascending. */
    private static final int[] SIZES = {16, 32, 48};

    private static final float[] RADIX_MULTS = {0.00390625F, 3.051758E-05F,
            1.1920929E-07F, 7.629395E-06F};

    /** One-line resolution report (stdout for the CLI). */
    public interface Reporter {
        void say(String line);
    }

    private final ApkReader apk;
    private final ResourceTable table;
    private final Reporter say;
    private final Set<String> reportedGaps = new LinkedHashSet<>();

    private AppIcon(ApkReader apk, ResourceTable table, Reporter say) {
        this.apk = apk;
        this.table = table;
        this.say = say;
    }

    /**
     * Resolves and renders the icon; never throws — failures are
     * reported lines and the result simply stays empty.
     */
    public static List<WindowIcon> load(ApkReader apk, Reporter say) {
        try {
            return new AppIcon(apk,
                    ResourceTable.parse(apk.readEntry("resources.arsc")),
                    say).resolve();
        } catch (IOException | RuntimeException | LinkageError error) {
            say.say("icon: unavailable (" + error.getMessage() + ")");
            return List.of();
        }
    }

    private List<WindowIcon> resolve() throws IOException {
        BinaryXml.Element manifest = apk.manifest();
        BinaryXml.Element application = manifest.child("application");
        if (application == null) {
            return gap("no application element");
        }
        int reference = refValue(application.typed("icon"));
        if (reference == -1) {
            reference = refValue(application.typed("roundIcon"));
        }
        if (reference == -1) {
            return gap("manifest has no icon");
        }
        Source source = sourceFor(reference, 0);
        if (source instanceof GapSource gap) {
            return gap(gap.why());
        }
        return render(source);
    }

    /** Packed resource id from a typed attribute, or -1. */
    private static int refValue(BinaryXml.Attribute attribute) {
        if (attribute == null) {
            return -1;
        }
        if (attribute.dataType == BinaryXml.TYPE_REFERENCE) {
            return attribute.data;
        }
        // Textual "@0x7f080000" fallback (older compilers).
        if (attribute.value != null && attribute.value.startsWith("@0x")) {
            try {
                return (int) Long.parseLong(attribute.value.substring(3), 16);
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }
        return -1;
    }

    // ---- source model ---------------------------------------------------

    /** What one resource entry resolves to. */
    private sealed interface Source
            permits VectorSource, ColorSource, RasterSource, GapSource {
    }

    /**
     * Converted {@code <vector>}: SVG defs+body in viewport space, plus
     * the declared intrinsic width/height (dp-as-px).
     */
    private record VectorSource(float viewportWidth, float viewportHeight,
            float width, float height, String body) implements Source {
    }

    private record ColorSource(int argb) implements Source {
    }

    /** Decoded bitmap: straight-alpha ARGB ints, top-down rows. */
    private record RasterSource(int width, int height, int[] argb)
            implements Source {
    }

    private record GapSource(String why) implements Source {
    }

    /** Follows one packed reference into drawable/color/gradient data. */
    private Source sourceFor(int reference, int depth) throws IOException {
        List<ResourceTable.ConfValue> values = table.byId(reference);
        if (values.isEmpty()) {
            return new GapSource("resource 0x" + Integer.toHexString(reference)
                    + " is not in the resource table");
        }
        return sourceFor(best(values), depth);
    }

    private Source sourceFor(ResourceTable.ConfValue value, int depth)
            throws IOException {
        if (depth > 4) {
            return new GapSource("reference chain too deep");
        }
        switch (value.dataType()) {
            case BinaryXml.TYPE_STRING:
                String path = table.string(value.data());
                if (path == null) {
                    return new GapSource("string value missing");
                }
                return (path.endsWith(".xml") || path.indexOf('.') < 0)
                        ? xmlSource(path, depth)
                        : rasterSource(path);
            case BinaryXml.TYPE_REFERENCE, BinaryXml.TYPE_DYNAMIC_REFERENCE:
                return sourceFor(value.data(), depth + 1);
            case BinaryXml.TYPE_COLOR_ARGB4, BinaryXml.TYPE_COLOR_RGB4,
                    BinaryXml.TYPE_COLOR_ARGB8, BinaryXml.TYPE_COLOR_RGB8:
                return new ColorSource(value.data());
            default:
                return new GapSource("unsupported value type 0x"
                        + Integer.toHexString(value.dataType()));
        }
    }

    /**
     * Picks the configuration like a launcher would: version-qualified XML
     * drawables (v21 adaptive/vector) outrank bitmaps; among bitmaps the
     * densest wins, since window icons only ever downscale.
     */
    private ResourceTable.ConfValue best(List<ResourceTable.ConfValue> values) {
        ResourceTable.ConfValue win = null;
        long winScore = Long.MIN_VALUE;
        for (ResourceTable.ConfValue candidate : values) {
            long score;
            switch (candidate.dataType()) {
                case BinaryXml.TYPE_REFERENCE, BinaryXml.TYPE_COLOR_ARGB4,
                        BinaryXml.TYPE_COLOR_RGB4, BinaryXml.TYPE_COLOR_ARGB8,
                        BinaryXml.TYPE_COLOR_RGB8:
                    score = 1_000_000L + candidate.sdkVersion();
                    break;
                case BinaryXml.TYPE_STRING: {
                    // XML drawables (version-qualified) outrank bitmaps;
                    // among bitmaps the densest wins (window icons only
                    // ever downscale).
                    String path = table.string(candidate.data());
                    boolean xml = path != null
                            && (path.endsWith(".xml") || path.indexOf('.') < 0);
                    score = xml
                            ? 1_000_000L + candidate.sdkVersion()
                            : Math.max(candidate.density(), 160);
                    break;
                }
                default:
                    // Inline bitmaps and anything else: density only.
                    score = Math.max(candidate.density(), 160);
            }
            if (score > winScore) {
                winScore = score;
                win = candidate;
            }
        }
        return win;
    }

    private Source rasterSource(String path) throws IOException {
        byte[] bytes;
        try {
            bytes = apk.readEntry(path);
        } catch (IOException error) {
            return new GapSource("entry missing: " + path);
        }
        GfxImage image = GfxBackend.get().decodeImage(bytes);
        if (image == null) {
            return new GapSource("cannot decode " + path);
        }
        try {
            return new RasterSource(image.getWidth(), image.getHeight(),
                    image.readPixels());
        } finally {
            image.close();
        }
    }

    private Source xmlSource(String path, int depth) throws IOException {
        byte[] bytes;
        try {
            bytes = apk.readEntry(path);
        } catch (IOException error) {
            return new GapSource("entry missing: " + path);
        }
        BinaryXml.Element root = BinaryXml.parse(bytes);
        switch (root.name) {
            case "adaptive-icon":
                return adaptiveSource(root, depth);
            case "vector":
                return vectorSource(root);
            case "gradient":
                // Gradient drawables only exist as inline paint of a
                // vector path; as a layer they are not a shape.
                return new GapSource(path + ": gradient is not a drawable layer");
            default:
                return new GapSource(path + ": drawable not converted ("
                        + root.name + ")");
        }
    }

    /**
     * {@code <adaptive-icon>} composites background then foreground
     * full-bleed in the shared 108 viewport. Layers are authored that way
     * on device too; launchers additionally mask them, which the desktop
     * window icon leaves out (squares preserve the whole safe-zone
     * content). Monochrome layers are theme-only and ignored, matching
     * unmasked renderers.
     */
    private Source adaptiveSource(BinaryXml.Element icon, int depth)
            throws IOException {
        Source background = layer(icon.child("background"), depth);
        Source foreground = layer(icon.child("foreground"), depth);
        if (background instanceof GapSource
                && foreground instanceof GapSource) {
            return background;
        }
        SvgBody svg = new SvgBody();
        merge(svg, background, "background");
        merge(svg, foreground, "foreground");
        return new VectorSource(108f, 108f, 108f, 108f, svg.body());
    }

    private void merge(SvgBody svg, Source layer, String role) {
        if (layer instanceof ColorSource color) {
            svg.content().append("<path d=\"M0,0h108v108h-108z\" fill=\"")
                    .append(css(color.argb())).append("\"/>");
        } else if (layer instanceof VectorSource vector) {
            double sx = vector.viewportWidth() == 108f
                    ? 1 : 108.0 / vector.viewportWidth();
            double sy = vector.viewportHeight() == 108f
                    ? 1 : 108.0 / vector.viewportHeight();
            svg.content().append(sx == 1 && sy == 1
                    ? vector.body()
                    : "<g transform=\"scale(" + sx + " " + sy + ")\">"
                            + vector.body() + "</g>");
        } else if (layer instanceof GapSource gap) {
            gapLayer(role + ": " + gap.why());
        }
        // RasterSource layers are unsupported inside adaptive icons
        // (reported by layer()).
    }

    private Source layer(BinaryXml.Element element, int depth) throws IOException {
        if (element == null) {
            return new GapSource("layer element missing");
        }
        BinaryXml.Attribute drawable = element.typed("drawable");
        if (drawable != null && drawable.dataType == BinaryXml.TYPE_REFERENCE) {
            return sourceFor(drawable.data, depth + 1);
        }
        BinaryXml.Attribute typed = element.typed("drawable");
        if (typed != null && (typed.dataType == BinaryXml.TYPE_COLOR_ARGB4
                || typed.dataType == BinaryXml.TYPE_COLOR_RGB4
                || typed.dataType == BinaryXml.TYPE_COLOR_ARGB8
                || typed.dataType == BinaryXml.TYPE_COLOR_RGB8)) {
            return new ColorSource(typed.data);
        }
        // Non-typed forms: color literal or @name text reference.
        String text = element.attr("drawable");
        if (text != null && text.startsWith("@")) {
            List<ResourceTable.ConfValue> values = table.byName(text.substring(1));
            if (!values.isEmpty()) {
                return sourceFor(best(values), depth + 1);
            }
            return new GapSource("unresolvable layer reference: " + text);
        }
        if (text != null) {
            Integer literal = argbLiteral(text);
            if (literal != null) {
                return new ColorSource(literal);
            }
        }
        return new GapSource("layer has no drawable reference");
    }

    private Source vectorSource(BinaryXml.Element vector) {
        float viewportWidth = number(vector, "viewportWidth", -1f);
        float viewportHeight = number(vector, "viewportHeight", -1f);
        if (viewportWidth <= 0f || viewportHeight <= 0f) {
            return new GapSource("vector without viewport");
        }
        SvgBody svg = new SvgBody();
        for (BinaryXml.Element child : vector.children) {
            svg.node(child);
        }
        float width = number(vector, "width", viewportWidth);
        float height = number(vector, "height", viewportHeight);
        return new VectorSource(viewportWidth, viewportHeight, width, height,
                svg.body());
    }

    // ---- vector -> SVG --------------------------------------------------

    /** SVG builder accumulating defs and body for one vector paint. */
    private final class SvgBody {
        private final StringBuilder defs = new StringBuilder();
        private final StringBuilder body = new StringBuilder();
        private int gradientCount;

        StringBuilder content() {
            return body;
        }

        String body() {
            StringBuilder out = new StringBuilder();
            if (defs.length() > 0) {
                out.append("<defs>").append(defs).append("</defs>");
            }
            return out.append(body).toString();
        }

        private void gap(String why) {
            reportedGaps.add(why);
        }

        /** Def id for a gradient emitted into defs. */
        private String nextGradientId() {
            gradientCount++;
            return "tartineGradient" + gradientCount;
        }

        void node(BinaryXml.Element element) {
            switch (element.name) {
                case "group":
                    group(element);
                    break;
                case "path":
                    path(element);
                    break;
                default:
                    break; // clip-path handled by group(), aapt:attr by path()
            }
        }

        private void group(BinaryXml.Element group) {
            // Android clips run: every clip-path child of the group
            // intersects with the next. Nested transform-free groups with
            // identical coordinate space reproduce that in SVG.
            List<String> clips = new ArrayList<>();
            for (BinaryXml.Element child : group.children) {
                if (child.name.equals("clip-path")) {
                    String id = clip(child);
                    if (id != null) {
                        clips.add(id);
                    }
                }
            }
            String transform = transformOf(group);
            float alpha = number(group, "alpha", 1f);
            StringBuilder open = new StringBuilder("<g");
            if (transform != null) {
                open.append(" transform=\"").append(transform).append("\"");
            }
            if (alpha != 1f) {
                open.append(" opacity=\"").append(clean(alpha)).append("\"");
            }
            if (clips.isEmpty()) {
                body.append(open).append(">");
                emitChildren(group);
                body.append("</g>");
                return;
            }
            body.append(open).append(">");
            StringBuilder close = new StringBuilder();
            // clips apply to the children in child-space; nest them.
            for (int i = 0; i < clips.size(); i++) {
                body.append("<g clip-path=\"url(#").append(clips.get(i))
                        .append(")\">");
                close.insert(0, "</g>");
            }
            emitChildren(group);
            body.append(close);
            body.append("</g>");
        }

        private void emitChildren(BinaryXml.Element group) {
            for (BinaryXml.Element child : group.children) {
                if (child.name.equals("path")) {
                    path(child);
                } else if (child.name.equals("group")) {
                    group(child);
                }
            }
        }

        private String clip(BinaryXml.Element clip) {
            BinaryXml.Attribute pathData = clip.typed("pathData");
            if (pathData == null) {
                gap("clip-path without pathData");
                return null;
            }
            String id = nextGradientId();
            defs.append("<clipPath id=\"").append(id)
                    .append("\"><path d=\"").append(escape(pathData.value))
                    .append("\"/></clipPath>");
            return id;
        }

        private void path(BinaryXml.Element element) {
            BinaryXml.Attribute pathData = element.typed("pathData");
            if (pathData == null || pathData.value == null) {
                gap("vector path without pathData");
                return;
            }
            int trimStart = (int) number(element, "trimPathStart", 0f);
            int trimEnd = (int) number(element, "trimPathEnd", 1f);
            if (trimStart != 0 || trimEnd != 1) {
                gap("vector trimPath not converted (path drawn whole)");
            }
            StringBuilder attrs = new StringBuilder("<path d=\"")
                    .append(escape(pathData.value)).append("\"");
            String fill = paint(element, "fillColor");
            if (fill != null) {
                attrs.append(" fill=\"").append(fill).append("\"");
            } else {
                // Android draws no fill when fillColor is absent; SVG
                // defaults to black, so suppress it explicitly.
                attrs.append(" fill=\"none\"");
            }
            float fillAlpha = number(element, "fillAlpha", 1f);
            if (fillAlpha != 1f) {
                attrs.append(" fill-opacity=\"")
                        .append(clean(fillAlpha)).append("\"");
            }
            String fillRule = fillRule(element);
            if (fillRule != null) {
                attrs.append(" fill-rule=\"").append(fillRule).append("\"");
            }
            String stroke = paint(element, "strokeColor");
            if (stroke != null) {
                attrs.append(" stroke=\"").append(stroke).append("\"");
                float strokeWidth = number(element, "strokeWidth", 0f);
                if (strokeWidth != 0f) {
                    attrs.append(" stroke-width=\"")
                            .append(clean(strokeWidth)).append("\"");
                }
                float strokeAlpha = number(element, "strokeAlpha", 1f);
                if (strokeAlpha != 1f) {
                    attrs.append(" stroke-opacity=\"")
                            .append(clean(strokeAlpha)).append("\"");
                }
                int lineCap = (int) number(element, "strokeLineCap", 0f);
                if (lineCap == 1) {
                    attrs.append(" stroke-linecap=\"round\"");
                } else if (lineCap == 2) {
                    attrs.append(" stroke-linecap=\"square\"");
                }
                int lineJoin = (int) number(element, "strokeLineJoin", 0f);
                if (lineJoin == 1) {
                    attrs.append(" stroke-linejoin=\"round\"");
                } else if (lineJoin == 2) {
                    attrs.append(" stroke-linejoin=\"bevel\"");
                }
                float miterLimit = number(element, "strokeMiterLimit", 4f);
                if (miterLimit != 4f) {
                    attrs.append(" stroke-miterlimit=\"")
                            .append(clean(miterLimit)).append("\"");
                }
            }
            body.append(attrs).append("/>");
        }

        /** Color or gradient url for a paint attribute, or null. */
        private String paint(BinaryXml.Element path, String name) {
            BinaryXml.Attribute attribute = path.typed(name);
            if (attribute != null) {
                switch (attribute.dataType) {
                    case BinaryXml.TYPE_COLOR_ARGB4, BinaryXml.TYPE_COLOR_RGB4,
                            BinaryXml.TYPE_COLOR_ARGB8, BinaryXml.TYPE_COLOR_RGB8:
                        return css(attribute.data);
                    case BinaryXml.TYPE_REFERENCE, BinaryXml.TYPE_DYNAMIC_REFERENCE:
                        List<ResourceTable.ConfValue> referenced =
                                table.byId(attribute.data);
                        if (referenced.isEmpty()) {
                            gap(name + ": paint reference 0x"
                                    + Integer.toHexString(attribute.data)
                                    + " is not in the resource table");
                            return null;
                        }
                        return paintValue(best(referenced), name);
                    case BinaryXml.TYPE_ATTRIBUTE:
                        gap(name + ": ?attr theme color not rendered");
                        return null;
                    default:
                        return paintFromString(attribute.value, name);
                }
            }
            return inlinePaintChild(path, name);
        }

        /** "@0x...", "@name/entry" or "#rrggbb" text paint fallback. */
        private String paintFromString(String text, String name) {
            if (text == null) {
                return null;
            }
            if (text.startsWith("#")) {
                Integer argb = argbLiteral(text);
                return argb == null ? null : css(argb);
            }
            if (text.startsWith("?")) {
                gap(name + ": ?attr theme color not rendered");
                return null;
            }
            if (text.startsWith("@") && !table.byName(text.substring(1)).isEmpty()) {
                return paintValue(best(table.byName(text.substring(1))), name);
            }
            return null;
        }

        /** Paint value: inline color, or the $name__N gradient file. */
        private String paintValue(ResourceTable.ConfValue value, String name) {
            switch (value.dataType()) {
                case BinaryXml.TYPE_COLOR_ARGB4, BinaryXml.TYPE_COLOR_RGB4,
                        BinaryXml.TYPE_COLOR_ARGB8, BinaryXml.TYPE_COLOR_RGB8:
                    return css(value.data());
                case BinaryXml.TYPE_REFERENCE, BinaryXml.TYPE_DYNAMIC_REFERENCE,
                        BinaryXml.TYPE_STRING: {
                    String path;
                    if (value.dataType() == BinaryXml.TYPE_STRING) {
                        path = table.string(value.data());
                    } else {
                        List<ResourceTable.ConfValue> inner =
                                table.byId(value.data());
                        path = inner.isEmpty()
                                ? null : table.string(best(inner).data());
                    }
                    if (path == null || !path.endsWith(".xml")) {
                        gap(name + ": paint reference does not resolve");
                        return null;
                    }
                    try {
                        BinaryXml.Element paint =
                                BinaryXml.parse(apk.readEntry(path));
                        if (paint.name.equals("gradient")) {
                            return gradientDef(paint, name);
                        }
                        gap(path + ": paint is " + paint.name + ", not gradient");
                        return null;
                    } catch (IOException error) {
                        gap(name + ": paint file unreadable: " + error.getMessage());
                        return null;
                    }
                }
                default:
                    gap(name + ": paint value type 0x"
                            + Integer.toHexString(value.dataType())
                            + " unsupported");
                    return null;
            }
        }

        /** Nested <aapt:attr name="android:fillColor"><gradient/></aapt:attr>. */
        private String inlinePaintChild(BinaryXml.Element path, String name) {
            for (BinaryXml.Element child : path.children) {
                String attrName = child.attr("name");
                if ((child.name.equals("attr") || child.name.equals("aapt:attr"))
                        && attrName != null
                        && attrName.endsWith(name)
                        && attrName.contains(name)) {
                    for (BinaryXml.Element payload : child.children) {
                        if (payload.name.equals("gradient")) {
                            return gradientDef(payload, name);
                        }
                        if (payload.name.equals("vector")) {
                            return null; // should not happen inside a path
                        }
                    }
                }
            }
            return null;
        }

        /**
         * GradientColor conversion (AOSP docs table: linear has startX/Y
         * + endX/Y; radial has centerX/Y + gradientRadius; items carry
         * color/offset and beat startColor/endColor/centerColor).
         */
        private String gradientDef(BinaryXml.Element gradient, String name) {
            int type = (int) number(gradient, "type", 0f);
            List<double[]> stops = stops(gradient);
            if (stops.isEmpty()) {
                gap(name + ": gradient without stops");
                return null;
            }
            String id = nextGradientId();
            String spread = tileMode(gradient);
            StringBuilder defsString = new StringBuilder();
            if (type == 0) {
                defsString.append("<linearGradient id=\"").append(id)
                        .append("\" gradientUnits=\"userSpaceOnUse\" x1=\"")
                        .append(clean(number(gradient, "startX", 0f)))
                        .append("\" y1=\"")
                        .append(clean(number(gradient, "startY", 0f)))
                        .append("\" x2=\"")
                        .append(clean(number(gradient, "endX", 0f)))
                        .append("\" y2=\"")
                        .append(clean(number(gradient, "endY", 0f))).append("\"");
            } else if (type == 1) {
                defsString.append("<radialGradient id=\"").append(id)
                        .append("\" gradientUnits=\"userSpaceOnUse\" cx=\"")
                        .append(clean(number(gradient, "centerX", 0f)))
                        .append("\" cy=\"")
                        .append(clean(number(gradient, "centerY", 0f)))
                        .append("\" r=\"")
                        .append(clean(number(gradient, "gradientRadius", 0f)))
                        .append("\"");
            } else {
                gap(name + ": sweep gradient not rendered");
                return null;
            }
            if (spread != null) {
                defsString.append(" spreadMethod=\"").append(spread).append("\"");
            }
            defsString.append(">");
            for (double[] stop : stops) {
                defsString.append("<stop offset=\"")
                        .append(clean((float) stop[0])).append("\"");
                int argb = (int) stop[1];
                defsString.append(" stop-color=\"#")
                        .append(String.format(Locale.ROOT, "%06x", argb & 0xFFFFFF))
                        .append("\"");
                int alpha = argb >>> 24;
                if (alpha != 255) {
                    defsString.append(" stop-opacity=\"")
                            .append(clean(alpha / 255f)).append("\"");
                }
                defsString.append("/>");
            }
            defs.append(defsString).append("</").append(type == 0
                    ? "linearGradient" : "radialGradient").append(">");
            return "url(#" + id + ")";
        }

        /** Inline {@code <item color offset/>} list or legacy color attrs. */
        private List<double[]> stops(BinaryXml.Element gradient) {
            List<double[]> stops = new ArrayList<>();
            for (BinaryXml.Element item : gradient.children) {
                if (!item.name.equals("item")) {
                    continue;
                }
                BinaryXml.Attribute color = item.typed("color");
                Integer argb = color == null ? null
                        : (isColorType(color.dataType)
                                ? color.data : argbLiteral(color.value));
                if (argb == null) {
                    continue;
                }
                double offset = number(item, "offset", Float.NaN);
                stops.add(new double[]{Double.isNaN(offset) ? Double.NaN : offset,
                        argb});
            }
            if (stops.isEmpty()) {
                Integer start = attrColor(gradient, "startColor");
                Integer center = attrColor(gradient, "centerColor");
                Integer end = attrColor(gradient, "endColor");
                if (start != null && end != null) {
                    if (center != null) {
                        stops.add(new double[]{0d, start});
                        stops.add(new double[]{0.5d, center});
                        stops.add(new double[]{1d, end});
                    } else {
                        stops.add(new double[]{0d, start});
                        stops.add(new double[]{1d, end});
                    }
                }
            }
            // missing offsets get even spread
            if (stops.stream().anyMatch(stop -> Double.isNaN(stop[0]))) {
                int count = stops.size();
                for (int i = 0; i < count; i++) {
                    if (Double.isNaN(stops.get(i)[0])) {
                        stops.set(i, new double[]{
                                count == 1 ? 0.0 : (double) i / (count - 1),
                                stops.get(i)[1]});
                    }
                }
            }
            return stops;
        }

        private Integer attrColor(BinaryXml.Element element, String name) {
            BinaryXml.Attribute attribute = element.typed(name);
            if (attribute == null) {
                return null;
            }
            if (isColorType(attribute.dataType)) {
                return attribute.data;
            }
            return argbLiteral(attribute.value);
        }

        private static String tileMode(BinaryXml.Element gradient) {
            BinaryXml.Attribute attribute = gradient.typed("tileMode");
            if (attribute == null) {
                return null;
            }
            int mode = attribute.dataType == BinaryXml.TYPE_INT_DEC
                    || attribute.dataType == BinaryXml.TYPE_INT_HEX
                    ? attribute.data
                    : switch (attribute.value != null
                            ? attribute.value.toLowerCase(Locale.ROOT) : "") {
                        case "repeat" -> 1;
                        case "mirror" -> 2;
                        default -> 0;
                    };
            return switch (mode) {
                case 1 -> "repeat";
                case 2 -> "reflect";
                default -> null;
            };
        }
    }

    private static boolean isColorType(int dataType) {
        return switch (dataType) {
            case BinaryXml.TYPE_COLOR_ARGB4, BinaryXml.TYPE_COLOR_RGB4,
                    BinaryXml.TYPE_COLOR_ARGB8, BinaryXml.TYPE_COLOR_RGB8 -> true;
            default -> false;
        };
    }

    /**
     * Group transform reproducing the AOSP update order: scale, rotation,
     * translation (VectorDrawable.java group contract), pivot-wrapped —
     * SVG applies right-most first.
     */
    private static String transformOf(BinaryXml.Element group) {
        float scaleX = number(group, "scaleX", 1f);
        float scaleY = number(group, "scaleY", 1f);
        float rotation = number(group, "rotation", 0f);
        float translateX = number(group, "translateX", 0f);
        float translateY = number(group, "translateY", 0f);
        float pivotX = number(group, "pivotX", 0f);
        float pivotY = number(group, "pivotY", 0f);
        if (scaleX == 1f && scaleY == 1f && rotation == 0f
                && translateX == 0f && translateY == 0f) {
            return null; // pure pivot: still references identity
        }
        StringBuilder out = new StringBuilder();
        if (translateX != 0f || translateY != 0f) {
            out.append("translate(").append(clean(translateX)).append(" ")
                    .append(clean(translateY)).append(") ");
        }
        if (pivotX != 0f || pivotY != 0f) {
            out.append("translate(").append(clean(pivotX)).append(" ")
                    .append(clean(pivotY)).append(") ");
        }
        if (rotation != 0f) {
            out.append("rotate(").append(clean(rotation)).append(") ");
        }
        if (scaleX != 1f || scaleY != 1f) {
            out.append("scale(").append(clean(scaleX)).append(" ")
                    .append(clean(scaleY)).append(") ");
        }
        if (pivotX != 0f || pivotY != 0f) {
            out.append("translate(").append(clean(-pivotX)).append(" ")
                    .append(clean(-pivotY)).append(") ");
        }
        if (out.length() == 0) {
            return null;
        }
        return out.toString().trim();
    }

    private static String fillRule(BinaryXml.Element element) {
        BinaryXml.Attribute attribute = element.typed("fillType");
        if (attribute == null) {
            return null;
        }
        if (attribute.dataType == BinaryXml.TYPE_INT_DEC
                || attribute.dataType == BinaryXml.TYPE_INT_HEX) {
            return attribute.data == 1 ? "evenodd" : null;
        }
        String text = attribute.value == null
                ? "" : attribute.value.toLowerCase(Locale.ROOT);
        if (text.contains("evenodd")) {
            return "evenodd";
        }
        return null;
    }

    /** Typed number: float, or a complex dimension ("108.0dip" etc). */
    private static float number(BinaryXml.Element element, String name,
            float fallback) {
        BinaryXml.Attribute attribute = element.typed(name);
        if (attribute == null) {
            return fallback;
        }
        switch (attribute.dataType) {
            case BinaryXml.TYPE_FLOAT:
                return Float.intBitsToFloat(attribute.data);
            case BinaryXml.TYPE_DIMENSION, BinaryXml.TYPE_FRACTION:
                return complexToFloat(attribute.data);
            case BinaryXml.TYPE_INT_DEC, BinaryXml.TYPE_INT_HEX:
                return attribute.data;
            default:
                return parseFloat(attribute.value, fallback);
        }
    }

    /** ResourceTypes.h complexToFloat semantics. */
    private static float complexToFloat(int data) {
        return (data & 0xFFFFFF00L) * RADIX_MULTS[(data >> 4) & 0x3];
    }

    private static float parseFloat(String text, float fallback) {
        if (text == null) {
            return fallback;
        }
        String value = text;
        for (String unit : new String[]{"dip", "dp", "sp", "px", "pt", "in",
                "mm"}) {
            if (value.endsWith(unit)) {
                value = value.substring(0, value.length() - unit.length());
                break;
            }
        }
        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    /** "#aarrggbb"/"#rrggbb"/"#argb" literal, or null when unparsable. */
    private static Integer argbLiteral(String text) {
        if (text == null) {
            return null;
        }
        String hex = text.startsWith("#") ? text.substring(1) : null;
        if (hex == null) {
            return null;
        }
        try {
            return switch (hex.length()) {
                case 6 -> (0xFF << 24) | (int) Long.parseLong(hex, 16);
                case 8 -> {
                    long typed = Long.parseLong(hex, 16);
                    yield ((int) (typed >>> 24) << 24) | (int) (typed & 0xFFFFFF);
                }
                case 3 -> { // #rgb
                    int value = Integer.parseInt(hex, 16);
                    yield (0xFF << 24) | dup(value >> 8 & 0xF) << 16
                            | dup(value >> 4 & 0xF) << 8 | dup(value & 0xF);
                }
                case 4 -> { // #argb
                    int value = Integer.parseInt(hex, 16);
                    yield dup(value >> 12 & 0xF) << 24 | dup(value >> 8 & 0xF) << 16
                            | dup(value >> 4 & 0xF) << 8 | dup(value & 0xF);
                }
                default -> null;
            };
        } catch (NumberFormatException error) {
            return null;
        }
    }

    /** "#argb"-style nibble expansion: n -> nn. */
    private static int dup(int nibble) {
        return (nibble << 4) | nibble;
    }

    private static String css(int argb) {
        return "#" + String.format(Locale.ROOT, "%06x", argb & 0xFFFFFF);
    }

    private static String clean(float value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e6) {
            return Long.toString((long) value);
        }
        return Float.toString(value);
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("\"", "&quot;");
    }

    // ---- rendering ------------------------------------------------------

    private List<WindowIcon> render(Source source) {
        if (source instanceof VectorSource vector) {
            return vectorIcons(vector);
        }
        if (source instanceof ColorSource color) {
            return colorIcons(color.argb());
        }
        if (source instanceof RasterSource raster) {
            return rasterIcons(raster);
        }
        return List.of();
    }

    private List<WindowIcon> vectorIcons(VectorSource vector) {
        List<WindowIcon> icons = new ArrayList<>();
        for (int size : SIZES) {
            String svg = standalone(vector, size);
            SVGLoader loader = new SVGLoader(); // not thread safe: one per call
            SVGDocument document;
            try {
                document = loader.load(new ByteArrayInputStream(
                        svg.getBytes(StandardCharsets.UTF_8)), null,
                        LoaderContext.createDefault());
            } catch (Exception error) {
                return gap("svg load failed (" + error.getMessage() + ")");
            }
            if (document == null) {
                return gap("svg document did not parse");
            }
            BufferedImage image = new BufferedImage(size, size,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            try {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints.VALUE_STROKE_PURE);
                document.render((Component) null, graphics,
                        new ViewBox(0, 0, size, size));
            } finally {
                graphics.dispose();
            }
            icons.add(new WindowIcon(size, size, rgba(image)));
        }
        StringBuilder note = new StringBuilder("icon: converted vector (")
                .append((int) vector.viewportWidth()).append("x")
                .append((int) vector.viewportHeight())
                .append(" viewport)");
        say.say(note.toString());
        return icons;
    }

    private String standalone(VectorSource vector, int size) {
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + size
                + "\" height=\"" + size
                + "\" viewBox=\"0 0 " + clean(vector.viewportWidth())
                + " " + clean(vector.viewportHeight()) + "\">"
                + vector.body() + "</svg>";
    }

    private List<WindowIcon> colorIcons(int argb) {
        List<WindowIcon> icons = new ArrayList<>();
        for (int size : SIZES) {
            byte[] rgba = new byte[size * size * 4];
            for (int pixel = 0; pixel < size * size; pixel++) {
                int base = pixel * 4;
                rgba[base] = (byte) (argb >>> 16);
                rgba[base + 1] = (byte) (argb >>> 8);
                rgba[base + 2] = (byte) argb;
                rgba[base + 3] = (byte) (argb >>> 24);
            }
            icons.add(new WindowIcon(size, size, rgba));
        }
        say.say("icon: solid color "
                + String.format(Locale.ROOT, "%08x", argb));
        return icons;
    }

    private List<WindowIcon> rasterIcons(RasterSource raster) {
        BufferedImage source = new BufferedImage(raster.width(), raster.height(),
                BufferedImage.TYPE_INT_ARGB);
        source.getRaster().setDataElements(0, 0, raster.width(), raster.height(),
                raster.argb());
        List<WindowIcon> icons = new ArrayList<>();
        for (int size : SIZES) {
            BufferedImage target = new BufferedImage(size, size,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = target.createGraphics();
            try {
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                graphics.drawImage(source, 0, 0, size, size, null);
            } finally {
                graphics.dispose();
            }
            icons.add(new WindowIcon(size, size, rgba(target)));
        }
        say.say("icon: bitmap " + raster.width() + "x" + raster.height());
        return icons;
    }

    private static byte[] rgba(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
                null, 0, image.getWidth());
        byte[] rgba = new byte[pixels.length * 4];
        for (int pixel = 0; pixel < pixels.length; pixel++) {
            int argb = pixels[pixel];
            rgba[pixel * 4] = (byte) (argb >>> 16 & 0xFF);
            rgba[pixel * 4 + 1] = (byte) (argb >>> 8 & 0xFF);
            rgba[pixel * 4 + 2] = (byte) (argb & 0xFF);
            rgba[pixel * 4 + 3] = (byte) (argb >>> 24 & 0xFF);
        }
        return rgba;
    }

    private List<WindowIcon> gap(String why) {
        if (reportedGaps.add(why)) {
            say.say("icon: " + why);
        }
        return List.of();
    }

    private void gapLayer(String why) {
        if (reportedGaps.add(why)) {
            say.say("icon: " + why);
        }
    }
}
