package br.com.samuelmarvila.ajornadadabiblia;

/** Small shared guard for the sequential journey. */
public final class CompletionGuard {
    private CompletionGuard() {}
    public static boolean isValidIndex(int index, int size) { return index >= 0 && index < size; }
}
