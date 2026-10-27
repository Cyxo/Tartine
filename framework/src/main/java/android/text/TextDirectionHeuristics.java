package android.text;

public final class TextDirectionHeuristics {
    public static final TextDirectionHeuristic LTR = new TextDirectionHeuristic() { public boolean isRtl(char[] a, int s, int c) { return false; } };
    public static final TextDirectionHeuristic RTL = new TextDirectionHeuristic() { public boolean isRtl(char[] a, int s, int c) { return true; } };
    public static final TextDirectionHeuristic FIRSTSTRONG_LTR = LTR;
    public static final TextDirectionHeuristic FIRSTSTRONG_RTL = LTR;
}
