package com.bookloop.exception;

/**
 * Thrown when an identical book listing already exists for the owner.
 */
public class DuplicateListingException extends BookLoopException {
    public DuplicateListingException(String message) {
        super(message);
    }
}
