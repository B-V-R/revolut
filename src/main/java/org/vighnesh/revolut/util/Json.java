package org.vighnesh.revolut.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.vighnesh.revolut.model.Account;
import org.vighnesh.revolut.model.Transaction;

import java.io.IOException;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Account convertToAccount(String json) throws IOException {
        return mapper.readValue(json, Account.class);
    }

    public static Transaction convertToTransaction(String json) throws IOException {
        return mapper.readValue(json, Transaction.class);
    }
}