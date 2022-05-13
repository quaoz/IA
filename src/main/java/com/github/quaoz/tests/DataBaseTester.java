package com.github.quaoz.tests;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;

import java.io.File;

public class DataBaseTester {
    public static void main(String[] args) {
        DataBaseConfig config = new DataBaseConfig();
        config.recordLength = 20;
        config.fields = new Integer[]{10, 10};

        DataBase<String> dataBase = new DataBase<>(
                new File("src/main/java/com/github/quaoz/tests/test.db").toPath(),
                new File("src/main/java/com/github/quaoz/tests/config.json").toPath(),
                config
        );
        dataBase.add("test      value     ");
        dataBase.add("test2     value2    ");
        dataBase.add("test3     value3    ");
        dataBase.add("test4     value4    ");
    }
}
