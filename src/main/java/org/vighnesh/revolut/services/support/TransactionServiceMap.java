package org.vighnesh.revolut.services.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.exception.InSufficientFundsException;
import org.vighnesh.revolut.exception.SameAccountException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.model.Transaction;
import org.vighnesh.revolut.services.AccountService;
import org.vighnesh.revolut.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

/**
 * The implementation of <code>TransactionService</code> where the datastore is represented as Map.
 */
@Singleton
public class TransactionServiceMap implements TransactionService {

    private final AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Inject
    public TransactionServiceMap(final AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void transfer(Transaction transaction) throws InSufficientFundsException, SameAccountException, AccountNotFoundException {
        Account fromAccount = accountService.getAccount(transaction.getFromId());
        Account toAccount = accountService.getAccount(transaction.getToId());

        if (fromAccount.getId().equals(toAccount.getId())) {
            LOGGER.error("Can't transfer to the same account.");
            throw new SameAccountException();
        }

        //To transfer the money we need to lock both account.
        //To take case about the situation when there are two transactions in the same time:
        // from account 1 to account 2 and vise versa (2 to 1) we first lock the account with smallest id.
        Lock firstLock = fromAccount.getId() > toAccount.getId() ? toAccount.getLock() : fromAccount.getLock();
        Lock secondLock = fromAccount.getId() > toAccount.getId() ? fromAccount.getLock() : toAccount.getLock();

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                    throw new InSufficientFundsException(fromAccount.getId(), transaction.getAmount());
                }
                fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
                toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}
