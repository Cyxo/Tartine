package tartine.fw;

import java.util.List;

/**
 * Host seam over the installed APK's compiled resources.
 *
 * <p>The desktop host parses {@code resources.arsc} and the entry's AXML with
 * the runtime's {@code tartine.apk} readers and installs an implementation
 * here before guest code launches; the framework's
 * {@code android.content.res} layer reads values and documents through this
 * interface and never touches raw resource bytes. The app-package portion of
 * the table lives host-side; {@code android}-package system resources are
 * answered from the host's own baseline where it has one.
 *
 * <p>All ids are the packed {@code 0xpptteeee} form. Methods return
 * {@code null} (or 0) when the table has no such resource, so the framework
 * degrades to its AOSP default (a missing value, an empty asset) rather than
 * throwing.
 */
public interface AppResourceHost {

    /**
     * Resolves a resource id to its best value under the device
     * configuration, following {@code TYPE_REFERENCE} links. Returns
     * {@code null} when the table does not hold the id.
     */
    ResValue value(int resId);

    /**
     * Resolves a themed attribute against an ordered list of style/theme
     * resource ids (last one applied wins, as in AOSP {@code Theme}).
     * Follows the styles' parent chains and attribute references. Returns
     * {@code null} when no style defines the attribute.
     */
    ResValue attribute(int attrId, int[] styles);

    /** Parsed compiled XML for a layout/xml/... resource id, or null. */
    XmlNode xml(int resId);

    /**
     * The item values of an array/plural resource, in table order, or
     * {@code null} when the id is not a bag. References inside the bag are
     * resolved the same way {@link #value(int)} resolves them.
     */
    List<ResValue> arrayValues(int resId);

    /**
     * Raw asset bytes for an APK path (for example {@code assets/app.js} or a
     * raw-resource file path), or {@code null} when the entry is absent.
     */
    byte[] asset(String path);

    /** Sorted child names of an asset directory, or {@code null} if absent. */
    String[] assetList(String dir);

    /**
     * One part of a resource's name: {@code part} 0 package, 1 type, 2 entry.
     * Returns {@code null} when the id is unknown.
     */
    String resourceName(int resId, int part);

    /** Packed id for a name/type/package triplet, or 0 when unknown. */
    int identifier(String name, String type, String pkg);
}
