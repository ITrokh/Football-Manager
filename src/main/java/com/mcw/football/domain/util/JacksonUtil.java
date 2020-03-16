package com.mcw.football.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtil {
    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();


    //Methods
    public static String objectToJson(Object obj) throws JsonProcessingException {
        synchronized (JACKSON_MAPPER) {
            return JACKSON_MAPPER.writeValueAsString(obj);
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) throws IOException {
        synchronized (JACKSON_MAPPER) {
            return JACKSON_MAPPER.readValue(json, clazz);
        }
    }

    public static String objectToPrettyJson(Object obj) throws JsonProcessingException {
        synchronized (JACKSON_MAPPER) {
            return JACKSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
    }
}