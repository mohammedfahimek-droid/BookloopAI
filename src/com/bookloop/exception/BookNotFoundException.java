package com.bookloop.exception;

/**
 * Thrown when a requested book cannot be located in the catalog.
 */
public class BookNotFoundException extends BookLoopException {
    public BookNotFoundException(String message) {
        super(message);
    }

    public static BookNotFoundException forId(String bookId) {
        return new BookNotFoundException("Book with identifier '" + bookId + "' was not found in the catalog.");
    }
}
