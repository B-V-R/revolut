package org.vighnesh.revolut.controller;

import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.exception.InSufficientFundsException;
import org.vighnesh.revolut.exception.SameAccountException;

import static spark.Spark.exception;

/**
 * The <code>ExceptionController</code> is responsible for exception handling.
 */
public class ExceptionController {

    ExceptionController() {
        exception(AccountNotFoundException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(InSufficientFundsException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(SameAccountException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

    }
}
