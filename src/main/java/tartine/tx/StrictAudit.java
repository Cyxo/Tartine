package tartine.tx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import tartine.dex.Dex;
import tartine.dex.DexClass;
import tartine.dex.DexSet;
import tartine.guest.SandboxDenied;

/**
 * Strict translation sweep over a whole dex set: the CI-facing counterpart
 * of {@link TxTranslator#setStrictGaps(boolean)}.
 *
 * <p>Translates every audited class with strict gaps on, classifies each
 * degraded method as an intentional sandbox denial or a translator gap, and
 * restores the process-wide flag afterwards. A non-empty translator-gap list
 * is the failure signal; denials stay aggregated so expected boundaries do
 * not drown the report. The sandbox is a compatibility boundary, not a
 * translator-correctness baseline: a denial is not a regression, a gap is.
 */
public final class StrictAudit {
    /**
     * Outcome of one sweep. {@code classes} is the dex total and
     * {@code audited} the subset selected by the optional filter. The
     * collections are copied on ingress and egress so the result stays an
     * immutable value object.
     */
    public static final class Result {
        private final int classes;
        private final int audited;
        private final int translated;
        private final int classDenials;
        private final List<TxTranslator.TxGap.Degradation> translatorGaps;
        private final Map<String, Integer> sandboxDenials;

        Result(int classes, int audited, int translated, int classDenials,
                List<TxTranslator.TxGap.Degradation> translatorGaps,
                Map<String, Integer> sandboxDenials) {
            this.classes = classes;
            this.audited = audited;
            this.translated = translated;
            this.classDenials = classDenials;
            this.translatorGaps = List.copyOf(translatorGaps);
            this.sandboxDenials = Collections.unmodifiableMap(
                    new TreeMap<>(sandboxDenials));
        }

        public int classes() {
            return classes;
        }

        public int audited() {
            return audited;
        }

        public int translated() {
            return translated;
        }

        public int classDenials() {
            return classDenials;
        }

        public List<TxTranslator.TxGap.Degradation> translatorGaps() {
            return List.copyOf(translatorGaps);
        }

        public Map<String, Integer> sandboxDenials() {
            return Collections.unmodifiableMap(
                    new TreeMap<>(sandboxDenials));
        }

        /** Whether the audited corpus translated without translator gaps. */
        public boolean clean() {
            return translatorGaps.isEmpty();
        }
    }

    private StrictAudit() {
    }

    /**
     * Runs the sweep. {@code classFilter} matches a class descriptor
     * substring (dots are normalized to slashes); null or empty audits every
     * class.
     */
    public static Result run(DexSet dex, TranslatedLoader loader,
            String classFilter) {
        String needle = classFilter == null ? "" : classFilter.replace('.', '/');
        boolean previous = TxTranslator.strictGaps();
        TxTranslator.setStrictGaps(true);
        try {
            int audited = 0;
            int translated = 0;
            int classDenials = 0;
            List<TxTranslator.TxGap.Degradation> gaps = new ArrayList<>();
            Map<String, Integer> denials = new TreeMap<>();
            for (Dex d : dex.dexes()) {
                for (DexClass cls : d.classes()) {
                    if (!needle.isEmpty() && !cls.type.contains(needle)) {
                        continue;
                    }
                    audited++;
                    try {
                        TxTranslator.translate(cls, loader.fieldSynths(),
                                loader.guestInterfaces(), loader.dexLookup(),
                                loader.getParent(), loader.resolver());
                        translated++;
                    } catch (TxTranslator.TxGap gap) {
                        collect(gap, cls, gaps, denials);
                    } catch (SandboxDenied denied) {
                        classDenials++;
                    }
                }
            }
            gaps.sort(Comparator.comparing(
                    TxTranslator.TxGap.Degradation::signature));
            return new Result(dex.classCount(), audited, translated,
                    classDenials, gaps, denials);
        } finally {
            TxTranslator.setStrictGaps(previous);
        }
    }

    private static void collect(TxTranslator.TxGap gap, DexClass cls,
            List<TxTranslator.TxGap.Degradation> gaps,
            Map<String, Integer> denials) {
        if (gap.degradations().isEmpty()) {
            gaps.add(new TxTranslator.TxGap.Degradation(
                    cls.type, String.valueOf(gap.getMessage())));
            return;
        }
        for (TxTranslator.TxGap.Degradation degradation
                : gap.degradations()) {
            if (degradation.reason() != null
                    && degradation.reason().startsWith("sandbox denied: ")) {
                denials.merge(degradation.reason(), 1, Integer::sum);
            } else {
                gaps.add(degradation);
            }
        }
    }
}
