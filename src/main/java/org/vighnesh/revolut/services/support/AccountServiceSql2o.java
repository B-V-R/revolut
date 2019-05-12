package org.vighnesh.revolut.services.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.vighnesh.revolut.exception.AccountNotFoundException;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.services.AccountService;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The implementation of <code>AccountService</code> where the datastore is represented as H2 data base.
 */
@Singleton
public class AccountServiceSql2o implements AccountService {
    private Sql2o sql2o;

    @Inject
    public AccountServiceSql2o(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Account createAccount(BigDecimal balance) {
        try (Connection conn = sql2o.beginTransaction()) {
            Long id = conn.createQuery("insert into account (BALANCE) values (:balance)", true)
                    .addParameter("balance", balance)
                    .executeUpdate().getKey(Long.class);
            conn.commit();
            return getAccount(id);
        } catch (AccountNotFoundException e) {
            return null;
        }
    }

    @Override
    public Account getAccount(Long id) throws AccountNotFoundException {
        try (Connection conn = sql2o.beginTransaction()) {
            Account account = conn.createQuery("select * from account where id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Account.class);
            conn.commit();
            if (Objects.isNull(account)) {
                throw new AccountNotFoundException(id);
            }
            return account;
        }
    }
}
