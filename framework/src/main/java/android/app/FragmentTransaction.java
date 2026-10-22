package android.app;

/**
 * Tartine: minimal skeleton. Framework (non-support) fragments never
 * attach on the desktop shell, so the transaction is a recording sink:
 * <ul>
 *   <li>{@code add(Fragment, String)} and {@code add(int, Fragment)} — the
 *       AOSP BackStackRecord queues an Op; the shell records nothing and
 *       returns this (the guest ReportFragment's injected fragment must
 *       never schedule lifecycle work here).</li>
 *   <li>{@code commit()} — AOSP BackStackRecord.commit() schedules the
 *       back-stack entry and returns its id; the shell executes nothing
 *       and answers id 0 (AOSP uses mIndex + 1 starting at 0 for the
 *       first recorded entry; nothing is recorded, so 0 stays free).</li>
 * </ul>
 */
public class FragmentTransaction {

    /** AOSP FragmentTransaction.add(Fragment, String). */
    public FragmentTransaction add(Fragment arg0, String arg1) {
        return this;
    }

    /** AOSP FragmentTransaction.add(int, Fragment). */
    public FragmentTransaction add(int arg0, Fragment arg1) {
        return this;
    }

    /** AOSP BackStackRecord.commit() — nothing queued, shell id 0. */
    public int commit() {
        return 0;
    }
}
