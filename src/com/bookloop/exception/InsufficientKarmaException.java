package com.bookloop.exception;

/**
 * Thrown when an action requires more campus eco-karma points than the user holds.
 */
public class InsufficientKarmaException extends BookLoopException {
    public InsufficientKarmaException(String message) {
        super(message);
    }
}
