package org.vighnesh.revolut;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.vighnesh.revolut.controller.AccountController;
import org.vighnesh.revolut.controller.ExceptionController;
import org.vighnesh.revolut.controller.TransactionController;
import org.vighnesh.revolut.cdi.AppMapInjector;
import org.vighnesh.revolut.cdi.AppSqlInjector;
import org.vighnesh.revolut.cdi.ImplType;
import org.vighnesh.revolut.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);


    public static void main(String[] args) {

        port(8080); // Spark will run on port 8080
        ImplType implType = ImplType.MAP;
        if (args.length > 0 && args[0].equals("SQL")) {
            implType = ImplType.SQL;
        }
        LOGGER.info("Application is starting in " + implType + " mode;");
        Injector injector = getInjector(implType);
        injector.getInstance(AccountController.class);
        injector.getInstance(TransactionController.class);
        injector.getInstance(ExceptionController.class);
    }

    private static Injector getInjector(ImplType implType) {
        switch (implType) {
            case MAP:
                return Guice.createInjector(new AppMapInjector());
            case SQL:
                return Guice.createInjector(new AppSqlInjector());
            default:
                return Guice.createInjector(new AppMapInjector());
        }
    }
}