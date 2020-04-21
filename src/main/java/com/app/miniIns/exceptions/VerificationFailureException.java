package com.app.miniIns.exceptions;

public class VerificationFailureException extends  Exception {
    public VerificationFailureException(String errorMessage) {
        super(errorMessage);
    }
}
