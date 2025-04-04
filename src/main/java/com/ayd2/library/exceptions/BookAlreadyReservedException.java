package com.ayd2.library.exceptions;

public class BookAlreadyReservedException extends ServiceException {
    public BookAlreadyReservedException() {}

    public BookAlreadyReservedException(String message) {
        super(message);
    }
}
