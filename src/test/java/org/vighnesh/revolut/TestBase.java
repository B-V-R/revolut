package org.vighnesh.revolut;

import org.junit.BeforeClass;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class TestBase {
    protected static final Sql2o sql2o = new Sql2o("jdbc:h2:~/revolut;", null, null);

    @BeforeClass
    public static void beforeClass() {
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
    }
}
