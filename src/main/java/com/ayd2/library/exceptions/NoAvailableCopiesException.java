package com.ayd2.library.exceptions;

public class NoAvailableCopiesException extends ServiceException {
    public NoAvailableCopiesException() {}

    public NoAvailableCopiesException(String message) {
        super(message);
    }
}
