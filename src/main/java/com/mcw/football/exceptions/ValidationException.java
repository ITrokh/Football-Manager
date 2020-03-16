package com.mcw.football.exceptions;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {
    private final Errors bindingResult;

    public ValidationException(Errors bindingResult) {
        this.bindingResult = bindingResult;
    }

    public Errors getBindingResult() {
        return bindingResult;
    }
}
