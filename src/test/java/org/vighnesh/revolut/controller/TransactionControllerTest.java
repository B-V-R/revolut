package org.vighnesh.revolut.controller;


import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.model.Transaction;
import org.vighnesh.revolut.services.AccountService;
import org.vighnesh.revolut.services.support.AccountServiceMap;
import org.vighnesh.revolut.services.support.TransactionServiceMap;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TransactionControllerTest {

    public static class TransactionControllerTestApplication implements SparkApplication {
        @Override
        public void init() {
            new TransactionController(new TransactionServiceMap(accountService));
            new ExceptionController();
        }
    }

    private static final AccountService accountService = new AccountServiceMap();

    @ClassRule
    public static SparkServer<TransactionControllerTestApplication> testServer =
            new SparkServer<>(TransactionControllerTestApplication.class, 4567);

    @Test
    public void testTransfer() throws Exception {
        Account fromAccount = accountService.createAccount(BigDecimal.valueOf(500));
        Account toAccount = accountService.createAccount(BigDecimal.valueOf(100));
        Transaction transaction = new Transaction(fromAccount.getId(), toAccount.getId(), BigDecimal.valueOf(150));
        PostMethod post = testServer.post("/transfer", Utility.convertToJson(transaction), false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
    }

    @Test
    public void testTransferSameAccounts() throws Exception {
        Account account = accountService.createAccount(BigDecimal.valueOf(500));
        Transaction transaction = new Transaction(account.getId(), account.getId(), BigDecimal.valueOf(150));
        PostMethod post = testServer.post("/transfer", Utility.convertToJson(transaction), false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(404, httpResponse.code());
        assertEquals("Can't transfer the money to the same account.", new String(httpResponse.body()));

    }

    @Test
    public void testTransferNotEnoughMoneyAccounts() throws Exception {
        Account fromAccount = accountService.createAccount(BigDecimal.valueOf(0));
        Account toAccount = accountService.createAccount(BigDecimal.valueOf(100));
        Transaction transaction = new Transaction(fromAccount.getId(), toAccount.getId(), BigDecimal.valueOf(150));
        PostMethod post = testServer.post("/transfer", Utility.convertToJson(transaction), false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(404, httpResponse.code());
        assertEquals("Account with id = " + fromAccount.getId() + " doesn't have enough balance to transfer this amount = 150",
                new String(httpResponse.body()));

    }

}