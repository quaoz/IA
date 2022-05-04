package com.github.quaoz.tests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBaseTester {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        DataBaseConfig db;

        try {
            db = objectMapper.readValue(new File("src/main/resources/test-db/db.json"), DataBaseConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> fields = db.getFields();

        AtomicInteger lineLength = new AtomicInteger();
        fields.forEach((s, integer) -> lineLength.addAndGet(integer + 1));

        System.out.println(lineLength.get());
    }
}
