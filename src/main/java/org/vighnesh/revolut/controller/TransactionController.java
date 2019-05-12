package org.vighnesh.revolut.controller;

import com.google.inject.Inject;
import org.vighnesh.revolut.model.Transaction;
import org.vighnesh.revolut.services.TransactionService;
import org.vighnesh.revolut.util.Json;

import static spark.Spark.post;

/**
 * The <code>TransactionController</code> contains REST API for Transactions.
 */
public class TransactionController {
    @Inject
    public TransactionController(final TransactionService transactionService) {

        post("/transfer", (req, res) -> {
            Transaction transaction = Json.convertToTransaction(req.body());
            transactionService.transfer(transaction);
            return "{Transfer Completed}";
        });

    }
}
