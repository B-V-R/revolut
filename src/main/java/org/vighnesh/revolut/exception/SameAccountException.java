package org.vighnesh.revolut.exception;

public class SameAccountException extends Exception {
    @Override
    public String getMessage() {
        return "Can't transfer the money to the same account.";
    }
}
