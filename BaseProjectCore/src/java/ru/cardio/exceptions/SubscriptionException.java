package ru.cardio.exceptions;

/**
 *
 * @author rogvold
 */
public class SubscriptionException extends Exception {

    /**
     * Creates a new instance of
     * <code>SubscriptionException</code> without detail message.
     */
    public SubscriptionException() {
    }

    /**
     * Constructs an instance of
     * <code>SubscriptionException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SubscriptionException(String msg) {
        super(msg);
    }
}
