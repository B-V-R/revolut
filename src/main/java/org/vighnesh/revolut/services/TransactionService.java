package org.vighnesh.revolut.services;

import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.exception.InSufficientFundsException;
import org.vighnesh.revolut.exception.SameAccountException;
import org.vighnesh.revolut.model.Transaction;

/**
 * The <code>TransactionService</code> is responsible for operations with Transactions.
 */
public interface TransactionService {
    /**
     * The <code>transfer</code> is responsible for transfer money between two accounts.
     *
     * @param transaction - the <code>Transaction</code> object that contains info about transaction
     * @throws InSufficientFundsException in case the balance in fromAccount is less the required to transfer amount
     * @throws SameAccountException       in case transfer is happening between the same accounts
     * @throws AccountNotFoundException   in case any of the accounts weren't found
     */
    void transfer(Transaction transaction) throws InSufficientFundsException, SameAccountException, AccountNotFoundException;
}
