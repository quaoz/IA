package com.github.quaoz.tests;

import com.github.quaoz.database.DataBase;

import java.io.File;

public class DataBaseTester {
    public static void main(String[] args) {
        DataBase<String> dataBase = new DataBase<String>(new File("src/main/java/com/github/quaoz/tests/test.db").toPath(), new File("src/main/java/com/github/quaoz/tests/config.json").toPath());
    }
}
