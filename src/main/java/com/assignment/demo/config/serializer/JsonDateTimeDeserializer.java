package com.assignment.demo.config.serializer;

import com.assignment.demo.constant.DemoConstant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class JsonDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        var oc = jsonParser.getCodec();
        var node = (TextNode) oc.readTree(jsonParser);
        var dateString = node.textValue();
        var formatter = DateTimeFormatter.ofPattern(DemoConstant.DATE_TIME_FORMATTER);

        return LocalDateTime.parse(dateString, formatter);
    }
}