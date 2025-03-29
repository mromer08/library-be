package com.ayd2.library.exceptions;

public class LoanLimitExceededException extends ServiceException {
    public LoanLimitExceededException() {}

    public LoanLimitExceededException(String message) {
        super(message);
    }
}
