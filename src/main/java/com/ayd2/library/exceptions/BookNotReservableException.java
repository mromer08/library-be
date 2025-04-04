package com.ayd2.library.exceptions;

public class BookNotReservableException extends ServiceException{
    BookNotReservableException() {}
    public BookNotReservableException(String message) {
        super(message);
    }
}
