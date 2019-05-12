package org.vighnesh.revolut.cdi;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.vighnesh.revolut.services.AccountService;
import org.vighnesh.revolut.services.TransactionService;
import org.vighnesh.revolut.services.support.AccountServiceSql2o;
import org.vighnesh.revolut.services.support.TransactionServiceSql2o;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class AppSqlInjector extends AbstractModule {

    @Override
    protected void configure() {
        //bind the service to implementation class
        bind(TransactionService.class).to(TransactionServiceSql2o.class);
        bind(AccountService.class).to(AccountServiceSql2o.class);
    }

    @Provides
    public Sql2o provideSql2o() {
        String dbUrl = "jdbc:h2:~/revolut;";

        Sql2o sql2o = new Sql2o(dbUrl, null, null);
        try (Connection connection = sql2o.beginTransaction()) {
            connection.createQuery("DROP TABLE ACCOUNT IF EXISTS;")
                    .executeUpdate();
            connection.createQuery("CREATE table if not exists account(" +
                    "id bigint auto_increment PRIMARY KEY," +
                    "balance DOUBLE PRECISION" +
                    ");")
                    .executeUpdate();
            connection.commit();
        }
        return sql2o;
    }
}
