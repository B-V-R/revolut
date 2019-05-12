package org.vighnesh.revolut.controller;

import com.google.inject.Inject;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.services.AccountService;
import org.vighnesh.revolut.util.Json;

import static spark.Spark.*;

/**
 * The <code>AccountController</code> contains REST API for Account.
 */
public class AccountController {

    @Inject
    public AccountController(AccountService accountService) {
        get("/account/:id", (req, res) -> {
            Long id = Long.valueOf(req.params("id"));
            return accountService.getAccount(id);
        });

        post("/account", "application/json", (req, res) -> {
            Account account = Json.convertToAccount(req.body());
            return accountService.createAccount(account.getBalance());
        });
    }


}
