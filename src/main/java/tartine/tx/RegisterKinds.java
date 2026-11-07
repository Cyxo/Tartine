package tartine.tx;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Register-value kind lattice shared by {@link TxMethodWriter}'s preview
 * store census and its frame-join decisions.
 *
 * <p>DEX registers are typed per path and d8 reuses one register for
 * different values, while JVM locals need one verifier type across a merge.
 * These rules decide which registers must live in a boxed Object slot and
 * which mixes are too ambiguous to translate; keeping them pure makes the
 * core testable without emitting bytecode.
 */
final class RegisterKinds {
    /** Narrow-int family (boolean/byte/char/short/int). */
    static final char INT = 'I';
    /** Float. */
    static final char FLOAT = 'F';
    /** Reference. */
    static final char OBJ = 'A';
    /** Long. */
    static final char LONG = 'J';
    /** Double. */
    static final char DOUBLE = 'D';
    /** No kind observed yet. */
    static final char UNKNOWN = 0;
    /** Join result for a mix the translator cannot unify. */
    static final char CONFLICT = '?';

    private RegisterKinds() {
    }

    /**
     * Registers whose observed store kinds force an Object-typed unified
     * slot. References mix with any numeric, an int mixes with long/float,
     * and long mixes with double: those values box through their
     * {@code valueOf} bridge and loads unbox per demand.
     *
     * <p>Float/long/double mixes without an int/reference arm stay
     * raw-typed: their bit-reinterpret semantics die under boxing, and
     * valid DEX does not merge width-mixed register pairs at a read. A
     * truly merging numeric conflict degrades through the verifier probe.
     */
    static Set<Integer> conflicting(Map<Integer, Set<Character>> kinds) {
        Set<Integer> out = new HashSet<>();
        for (Map.Entry<Integer, Set<Character>> e : kinds.entrySet()) {
            Set<Character> ks = e.getValue();
            if ((ks.contains(OBJ) && (ks.contains(INT) || ks.contains(LONG)
                    || ks.contains(FLOAT) || ks.contains(DOUBLE)))
                    || (ks.contains(INT) && (ks.contains(LONG)
                            || ks.contains(FLOAT)))
                    || (ks.contains(LONG) && ks.contains(DOUBLE))) {
                out.add(e.getKey());
            }
        }
        return out;
    }

    /**
     * Kind to carry across a join from the non-unknown {@code kinds} set:
     * the single agreed kind, {@link #OBJ} for a mix the boxing lane
     * handles, or {@link #CONFLICT}. The caller owns the empty case (no
     * information).
     *
     * <p>{@code FLOAT+LONG} unifies here even though the store census does
     * not admit that pair alone: a register unified for another reason
     * (say int+float stores) can show only float/long at one particular
     * join. A register that was never unified cannot reach this mix in
     * valid DEX, whose verifier rejects width-mixed register pairs at the
     * read.
     */
    static char join(Set<Character> kinds) {
        if (kinds.size() == 1) {
            return kinds.iterator().next();
        }
        if ((kinds.contains(OBJ) && (kinds.contains(INT)
                || kinds.contains(LONG) || kinds.contains(FLOAT)
                || kinds.contains(DOUBLE)))
                || (kinds.contains(INT) && (kinds.contains(LONG)
                        || kinds.contains(FLOAT)))
                 || (kinds.contains(LONG) && kinds.contains(DOUBLE))
                 || (kinds.contains(FLOAT) && kinds.contains(LONG))) {
            return OBJ;
        }
        return CONFLICT;
    }
}
