package org.vighnesh.revolut.services;


import org.vighnesh.revolut.TestBase;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.services.support.AccountServiceMap;
import org.vighnesh.revolut.services.support.AccountServiceSql2o;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest extends TestBase {

    private static AccountService serviceMap = new AccountServiceMap();
    private static AccountService serviceSql2o = new AccountServiceSql2o(sql2o);

    @BeforeClass
    public static void before() {
        serviceMap.createAccount(BigDecimal.valueOf(100));
        serviceSql2o.createAccount(BigDecimal.valueOf(200));
    }

    @Test
    public void testCreateAccount() {
        Account account = serviceMap.createAccount(BigDecimal.valueOf(300));
        assertEquals(Long.valueOf(2), account.getId());
        assertEquals(BigDecimal.valueOf(300), account.getBalance());

        account = serviceSql2o.createAccount(BigDecimal.valueOf(150));
        assertEquals(Long.valueOf(2), account.getId());
        assertEquals(BigDecimal.valueOf(150.0), account.getBalance());
    }

    @Test
    public void testGetAccount() throws Exception {
        Account account = serviceMap.getAccount(1L);
        assertEquals(Long.valueOf(1), account.getId());
        assertEquals(BigDecimal.valueOf(100), account.getBalance());

        account = serviceSql2o.getAccount(1L);
        assertEquals(Long.valueOf(1), account.getId());
        assertEquals(BigDecimal.valueOf(200.0), account.getBalance());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetNotExistingAccount() throws Exception {
        serviceMap.getAccount(5L);
    }


    @Test(expected = AccountNotFoundException.class)
    public void testGetNotExistingAccountSql() throws Exception {
        serviceSql2o.getAccount(5L);
    }
}