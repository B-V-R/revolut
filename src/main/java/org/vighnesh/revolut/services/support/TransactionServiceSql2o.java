package org.vighnesh.revolut.services.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.exception.InSufficientFundsException;
import org.vighnesh.revolut.exception.SameAccountException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.model.Transaction;
import org.vighnesh.revolut.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.math.BigDecimal;

/**
 * The implementation of <code>TransactionService</code> where the datastore is represented as H2 data base.
 */
@Singleton
public class TransactionServiceSql2o implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final Sql2o sql2o;

    @Inject
    public TransactionServiceSql2o(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void transfer(Transaction transaction) throws InSufficientFundsException, SameAccountException, AccountNotFoundException {
        if (transaction.getFromId().equals(transaction.getToId())) {
            LOGGER.error("Can't transfer to the same account.");
            throw new SameAccountException();
        }

        //To transfer the money we need to lock both account.
        //To take case about the situation when there are two transactions in the same time:
        // from account 1 to account 2 and vise versa (2 to 1) we first lock the account with smallest id.
        Long firstId = transaction.getFromId() > transaction.getToId() ? transaction.getToId() : transaction.getFromId();
        Long secondId = transaction.getFromId() > transaction.getToId() ? transaction.getFromId() : transaction.getToId();

        try (Connection conn = sql2o.beginTransaction()) {
            Account firstAccount = lockAndFetchAccount(conn, firstId);
            Account secondAccount = lockAndFetchAccount(conn, secondId);
            Account fromAccount = firstAccount.getId().equals(transaction.getFromId()) ? firstAccount : secondAccount;
            if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new InSufficientFundsException(fromAccount.getId(), transaction.getAmount());
            }
            updateBalance(conn, transaction.getFromId(), transaction.getAmount().negate());
            updateBalance(conn, transaction.getToId(), transaction.getAmount());
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            conn.commit();
        }

    }

    /**
     * The <code>lockAndFetchAccount</code> is responsible for locking and fetching the Account which we will update.
     *
     * @param conn - connection to DB
     * @param id   - the id of account that need to be locked.
     * @return the <code>Account</code> object that contains info about locked Account
     */
    private Account lockAndFetchAccount(Connection conn, Long id) throws AccountNotFoundException {
        Account account = conn.createQuery("select * from account where id = :id FOR UPDATE")
                .addParameter("id", id)
                .executeAndFetchFirst(Account.class);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    private void updateBalance(Connection conn, Long id, BigDecimal amount) {
        conn.createQuery("UPDATE ACCOUNT set BALANCE = BALANCE + :amount where id = :id")
                .addParameter("id", id)
                .addParameter("amount", amount)
                .executeUpdate();
    }

}
