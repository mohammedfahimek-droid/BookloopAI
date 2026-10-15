package com.bookloop.exception;

/**
 * Root exception for all BookLoop AI application domain errors.
 */
public class BookLoopException extends RuntimeException {
    public BookLoopException(String message) {
        super(message);
    }

    public BookLoopException(String message, Throwable cause) {
        super(message, cause);
    }
}
