package com.github.quaoz.tests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class DataBaseTester {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DataBaseConfig db = objectMapper.readValue(new File("src/main/resources/test-db/db.txt"), DataBaseConfig.class);
        System.out.println(db.isOrdered());
    }
}
