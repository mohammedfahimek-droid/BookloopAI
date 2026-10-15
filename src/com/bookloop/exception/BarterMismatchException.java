package com.bookloop.exception;

/**
 * Thrown when two books or students cannot form a valid barter loop.
 */
public class BarterMismatchException extends BookLoopException {
    public BarterMismatchException(String message) {
        super(message);
    }
}
