package android.app;

/**
 * Tartine: minimal skeleton. Framework (non-support) fragments never
 * attach or dispatch on the desktop shell, so the manager answers with a
 * transaction sink and an empty fragment store:
 * <ul>
 *   <li>{@code beginTransaction()} — AOSP
 *       core/java/android/app/FragmentManager.java returns a new
 *       BackStackRecord; the shell hands out its no-op
 *       {@link FragmentTransaction} (the guest ReportFragment injects an
 *       unattached framework fragment purely for lifecycle parity on
 *       API&nbsp;29- devices and its events must never schedule here).</li>
 *   <li>{@code executePendingTransactions()} — AOSP flushes the pending
 *       queue; the shell never enqueues, so it is inert.</li>
 *   <li>{@code findFragmentByTag()} — AOSP walks the active list; the
 *       shell's store is empty, so every tag misses (matches the no-op
 *       commit: nothing was ever added).</li>
 * </ul>
 */
public class FragmentManager {

    /** AOSP FragmentManager.beginTransaction(). */
    public FragmentTransaction beginTransaction() {
        return new FragmentTransaction();
    }

    /** AOSP FragmentManager.executePendingTransactions() - answers whether
     * any transactions executed; the shell never enqueues, so it answers
     * false (the guest ReportFragment injects an unattached framework
     * fragment purely for lifecycle parity on API-29 devices and its events
     * must never schedule here). */
    public boolean executePendingTransactions() {
        return false;
    }

    /** AOSP FragmentManager.findFragmentByTag(String). */
    public Fragment findFragmentByTag(String arg0) {
        return null;
    }
}
