package org.vighnesh.revolut.services;

import org.vighnesh.revolut.TestBase;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.exception.InSufficientFundsException;
import org.vighnesh.revolut.exception.SameAccountException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.model.Transaction;
import org.vighnesh.revolut.services.support.AccountServiceMap;
import org.vighnesh.revolut.services.support.AccountServiceSql2o;
import org.vighnesh.revolut.services.support.TransactionServiceMap;
import org.vighnesh.revolut.services.support.TransactionServiceSql2o;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class TransactionServiceStressTest extends TestBase {

    @Test
    public void testMultiplyThreadsMap() throws InterruptedException, AccountNotFoundException {
        AccountService accountService = new AccountServiceMap();
        TransactionService transactionService = new TransactionServiceMap(accountService);
        BigDecimal originalBalance = BigDecimal.ZERO;
        for (long i = 1; i <= 50; i++) {
            Account account = accountService.createAccount(BigDecimal.valueOf(1000));
            originalBalance = originalBalance.add(account.getBalance());
        }

        MultithreadedStressTester stressTester = new MultithreadedStressTester(1000);
        test(transactionService, 50, stressTester);
        BigDecimal resultBalance = BigDecimal.ZERO;

        for (long i = 1; i <= 50; i++) {
            Account account = accountService.getAccount(i);
            resultBalance = resultBalance.add(account.getBalance());
        }
        assertEquals(originalBalance, resultBalance);
    }

    @Test
    public void testTwoAccounts() throws InterruptedException, AccountNotFoundException {
        AccountService accountService = new AccountServiceMap();
        TransactionService transactionService = new TransactionServiceMap(accountService);
        BigDecimal originalBalance = BigDecimal.ZERO;
        int nAccounts = 2;
        for (long i = 1; i <= nAccounts; i++) {
            Account account = accountService.createAccount(BigDecimal.valueOf(2500));
            originalBalance = originalBalance.add(account.getBalance());
        }

        MultithreadedStressTester stressTester = new MultithreadedStressTester(1000);
        //Always transfer from first to second account
        test(transactionService, 1, stressTester);

        BigDecimal resultBalance = BigDecimal.ZERO;

        for (long i = 1; i <= nAccounts; i++) {
            Account account = accountService.getAccount(i);
            resultBalance = resultBalance.add(account.getBalance());
        }
        assertEquals(originalBalance, resultBalance);

        //as result the first account should contains 2500 - 1000
        assertEquals(BigDecimal.valueOf(1500), accountService.getAccount(1L).getBalance());
        //as result second account should contains 2500 + 1000
        assertEquals(BigDecimal.valueOf(3500), accountService.getAccount(2L).getBalance());
    }

    @Test
    public void testMultiplyThreadsSql() throws InterruptedException, AccountNotFoundException {
        AccountService accountService = new AccountServiceSql2o(sql2o);
        TransactionService transactionService = new TransactionServiceSql2o(sql2o);
        BigDecimal originalBalance = BigDecimal.ZERO;
        for (long i = 1; i <= 50; i++) {
            Account account = accountService.createAccount(BigDecimal.valueOf(1000));
            originalBalance = originalBalance.add(account.getBalance());
        }
        MultithreadedStressTester stressTester = new MultithreadedStressTester(100);
        test(transactionService, 50, stressTester);

        BigDecimal resultBalance = BigDecimal.ZERO;

        for (long i = 1; i <= 50; i++) {
            Account account = accountService.getAccount(i);
            resultBalance = resultBalance.add(account.getBalance());
        }
        assertEquals(originalBalance, resultBalance);
    }

    private void test(TransactionService transactionService, int nAccounts, MultithreadedStressTester stressTester)
            throws InterruptedException {

        stressTester.stress(() -> {
            long randomFromId = ThreadLocalRandom.current().nextLong(1, nAccounts + 1);
            long randomToId = ThreadLocalRandom.current().nextLong(1, nAccounts + 1);
            long toId = randomToId == randomFromId ? randomToId > 1 ? randomToId - 1 : randomToId + 1 : randomToId;

            Transaction transaction = new Transaction(randomFromId, toId, BigDecimal.ONE);
            try {
                transactionService.transfer(transaction);
            } catch (InSufficientFundsException | SameAccountException | AccountNotFoundException e) {
                e.printStackTrace();
            }
        });
        stressTester.shutdown();
    }
}
