package util;

/**
 * Simple pair class.
 * @param <T> The type of the first value in the pair
 * @param <U> The type of the second value in the pair
 * @author Harry Xu
 * @version 1.0 - January 11th 2024
 */
public class Pair<T, U> {
    private final T first;
    private final U second;

    /**
     * Constructs a {@link Pair}.
     * @param first the first element
     * @param second the second element
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * getFirst
     * Returns the first element in the pair.
     * @return the first element in the pair
     */
    public T getFirst() {
        return this.first;
    }

    /**
     * getSecond
     * Returns the second element in the pair.
     * @return the second element in the pair
     */
    public U getSecond() {
        return this.second;
    }
}
