package com.bookloop.exception;

/**
 * Thrown when a specified campus user is not found.
 */
public class UserNotFoundException extends BookLoopException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException forId(String userId) {
        return new UserNotFoundException("Campus user with ID '" + userId + "' was not found.");
    }
}
