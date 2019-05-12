package org.vighnesh.revolut.exception;

import java.math.BigDecimal;

public class InSufficientFundsException extends Exception {
    private final Long id;
    private final BigDecimal amount;

    public InSufficientFundsException(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }


    @Override
    public String getMessage() {
        return "Account with id = " + id + " doesn't have enough balance to transfer this amount = " + amount;
    }
}
