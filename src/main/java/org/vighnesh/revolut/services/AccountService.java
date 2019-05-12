package org.vighnesh.revolut.services;

import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.model.Account;

import java.math.BigDecimal;

/**
 * The <code>AccountService</code> is responsible for operations with Account.
 */
public interface AccountService {

    /**
     * The <code>createAccount</code> is responsible for creating the account with started balance.
     *
     * @param balance - the started balance
     * @return the <code>Account</code> object that contains new account.
     */
    Account createAccount(BigDecimal balance);

    /**
     * The <code>getAccount</code> is responsible for getting the account.
     *
     * @param id - the id of account
     * @return the <code>Account</code> object that contains the account.
     * @throws AccountNotFoundException in case account was not found
     */
    Account getAccount(Long id) throws AccountNotFoundException;

}
