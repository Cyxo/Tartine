package android.text;

public interface TextDirectionHeuristic {
    boolean isRtl(char[] array, int start, int count);
}
