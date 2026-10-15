package com.bookloop.exception;

/**
 * Thrown when an operation is invalid for the book's current listing mode.
 */
public class InvalidModeException extends BookLoopException {
    public InvalidModeException(String message) {
        super(message);
    }
}
