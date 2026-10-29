package android.content.res;

/**
 * Holds style attribute resolution state for a Context. The desktop shell
 * has no theming system yet, so this is a type-only shell: getTheme()
 * answers a lazy per-context instance, nothing is ever resolved from it
 * (the instance is cached once per context in mTheme).
 */
public final class Theme {

    /** One Theme per Context, created lazily by ContextWrapper.getTheme(). */
    Theme() {
    }
}
