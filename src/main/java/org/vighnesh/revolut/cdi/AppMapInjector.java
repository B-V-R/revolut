package org.vighnesh.revolut.cdi;

import com.google.inject.AbstractModule;
import org.vighnesh.revolut.services.AccountService;
import org.vighnesh.revolut.services.TransactionService;
import org.vighnesh.revolut.services.support.AccountServiceMap;
import org.vighnesh.revolut.services.support.TransactionServiceMap;

public class AppMapInjector extends AbstractModule {

    @Override
    protected void configure() {
        //bind the service to implementation class
        bind(TransactionService.class).to(TransactionServiceMap.class);
        bind(AccountService.class).to(AccountServiceMap.class);
    }
}
