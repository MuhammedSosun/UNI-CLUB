package com.uniClub.common.exceptions.exception;

import org.springdoc.api.ErrorMessage;

public class BaseException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public BaseException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
