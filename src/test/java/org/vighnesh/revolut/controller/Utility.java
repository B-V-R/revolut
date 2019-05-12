package org.vighnesh.revolut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

class Utility {

    private static final ObjectMapper mapper = new ObjectMapper();

    static String convertToJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
}
