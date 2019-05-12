package org.vighnesh.revolut.services.support;

import com.google.inject.Singleton;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The implementation of <code>AccountService</code> where the datastore is represented as Map.
 */
@Singleton
public class AccountServiceMap implements AccountService {

    private final ConcurrentMap<Long, Account> accounts = new ConcurrentHashMap<>();

    private final AtomicLong accountIdInc = new AtomicLong();

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Override
    public Account createAccount(BigDecimal balance) {
        long id = accountIdInc.incrementAndGet();
        accounts.put(id, new Account(id, balance));
        //If the API supports delete of account here we can get NULL in next line.
        //To take care about such situation the lock could be added.
        return accounts.get(id);
    }

    @Override
    public Account getAccount(Long id) throws AccountNotFoundException {
        Account account = accounts.get(id);
        if (Objects.isNull(account)) {
            LOGGER.error("Account with id " + id + " wasn't found.");
            throw new AccountNotFoundException(id);
        }
        return account;
    }
}
