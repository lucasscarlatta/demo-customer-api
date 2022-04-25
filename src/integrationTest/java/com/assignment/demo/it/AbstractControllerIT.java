package com.assignment.demo.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AbstractControllerIT extends AbstractIT {

    protected final String objectMapper(Object anObject) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        var ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(anObject);
    }
}
