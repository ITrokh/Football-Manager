package com.mcw.football.exceptions;

public class IllegalEnumException extends Exception {
    public IllegalEnumException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "IllegalEnumException:" + super.getMessage();
    }
}
