package com.github.quaoz.tests;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;

import java.io.File;
import java.io.IOException;

public class DataBaseTester {
    public static void main(String[] args) {
        DataBaseConfig config = new DataBaseConfig();
        config.recordLength = 22;
        config.fields = new Integer[]{10, 22};

        File databaseFile = new File("src/main/java/com/github/quaoz/tests/test.db");
        File configFile = new File("src/main/java/com/github/quaoz/tests/config.json");

        try (DataBase dataBase = new DataBase(
                databaseFile.toPath(),
                configFile.toPath(),
                config
        )) {
            dataBase.add("test      value      \n");
            dataBase.add("test2     value2     \n");
            dataBase.add("test3     value3     \n");
            dataBase.add("test4     value4     \n");

            System.out.println(dataBase.get("test3", 0).strip());
            System.out.println(dataBase.getRecordCount());

            dataBase.remove("test3");
            System.out.println(dataBase.get("test3", 0));
            System.out.println(dataBase.getRecordCount());

            System.out.println(dataBase.get("test", 0).strip());
            System.out.println(dataBase.get("test4", 0).strip());
            System.out.println(dataBase.get("value2", 1).strip()); // bad
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (true) {
            databaseFile.delete();
            configFile.delete();
        }
    }
}
