package org.vighnesh.revolut.services;


import org.vighnesh.revolut.TestBase;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.exception.InSufficientFundsException;
import org.vighnesh.revolut.exception.SameAccountException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.model.Transaction;
import org.vighnesh.revolut.services.support.TransactionServiceMap;
import org.vighnesh.revolut.services.support.TransactionServiceSql2o;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceTest extends TestBase {

    private AccountService accountService = mock(AccountService.class);

    private TransactionService serviceMap = new TransactionServiceMap(accountService);
    private TransactionService serviceSql = new TransactionServiceSql2o(sql2o);

    @Test
    public void testSuccessfulTransfer() throws Exception {
        Account fromAccount = new Account(1L, BigDecimal.valueOf(300));
        Account toAccount = new Account(2L, BigDecimal.valueOf(400));
        when(accountService.getAccount(1L)).thenReturn(fromAccount);
        when(accountService.getAccount(2L)).thenReturn(toAccount);
        Transaction transaction = new Transaction(1L, 2L, BigDecimal.valueOf(100));
        serviceMap.transfer(transaction);
        assertEquals(BigDecimal.valueOf(200), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(500), toAccount.getBalance());

    }

    @Test(expected = SameAccountException.class)
    public void testTransferWithToAccountWithTheSameId() throws Exception {
        Account account = new Account(1L, BigDecimal.valueOf(300));
        when(accountService.getAccount(1L)).thenReturn(account);
        Transaction transaction = new Transaction(1L, 1L, BigDecimal.valueOf(100));
        serviceMap.transfer(transaction);
    }

    @Test(expected = SameAccountException.class)
    public void testTransferWithToAccountWithTheSameIdSql() throws Exception {
        Account account = new Account(1L, BigDecimal.valueOf(300));
        when(accountService.getAccount(1L)).thenReturn(account);
        Transaction transaction = new Transaction(1L, 1L, BigDecimal.valueOf(100));
        serviceSql.transfer(transaction);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferFromNotExistingAccount() throws Exception {
        Account toAccount = new Account(2L, BigDecimal.valueOf(300));
        when(accountService.getAccount(1L)).thenThrow(new AccountNotFoundException(1L));
        when(accountService.getAccount(2L)).thenReturn(toAccount);
        Transaction transaction = new Transaction(1L, 2L, BigDecimal.valueOf(100));
        serviceMap.transfer(transaction);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferFromNotExistingAccountSql() throws Exception {
        when(accountService.getAccount(1L)).thenThrow(new AccountNotFoundException(1L));
        Transaction transaction = new Transaction(1L, 2L, BigDecimal.valueOf(100));
        serviceSql.transfer(transaction);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferToNotExistingAccount() throws Exception {
        Account fromAccount = new Account(1L, BigDecimal.valueOf(300));
        when(accountService.getAccount(1L)).thenReturn(fromAccount);
        when(accountService.getAccount(1L)).thenThrow(new AccountNotFoundException(2L));
        Transaction transaction = new Transaction(1L, 2L, BigDecimal.valueOf(100));
        serviceMap.transfer(transaction);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferToNotExistingAccountSql() throws Exception {
        Account fromAccount = new Account(1L, BigDecimal.valueOf(300));
        when(accountService.getAccount(1L)).thenReturn(fromAccount);
        when(accountService.getAccount(2L)).thenThrow(new AccountNotFoundException(2L));
        Transaction transaction = new Transaction(1L, 2L, BigDecimal.valueOf(100));
        serviceSql.transfer(transaction);
    }

    @Test(expected = InSufficientFundsException.class)
    public void testTransferNotEnoughBalance() throws Exception {
        Account fromAccount = new Account(1L, BigDecimal.valueOf(300));
        Account toAccount = new Account(2L, BigDecimal.valueOf(400));
        when(accountService.getAccount(1L)).thenReturn(fromAccount);
        when(accountService.getAccount(2L)).thenReturn(toAccount);
        Transaction transaction = new Transaction(1L, 2L, BigDecimal.valueOf(400));
        serviceMap.transfer(transaction);
    }

}