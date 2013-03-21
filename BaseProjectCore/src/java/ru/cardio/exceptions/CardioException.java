package ru.cardio.exceptions;

/**
 *
 * @author rogvold
 */
public class CardioException extends Exception {

    /**
     * Creates a new instance of
     * <code>CardioException</code> without detail message.
     */
    public CardioException() {
    }

    /**
     * Constructs an instance of
     * <code>CardioException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CardioException(String msg) {
        super(msg);
    }
}
