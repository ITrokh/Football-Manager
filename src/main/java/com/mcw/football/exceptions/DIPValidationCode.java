package com.mcw.football.exceptions;

public enum DIPValidationCode {
DIP_VALIDATION_TEAM_NAME("Team name must be not empty!");

    private final String message;

    DIPValidationCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
