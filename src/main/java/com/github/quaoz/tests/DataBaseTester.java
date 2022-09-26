package com.github.quaoz.tests;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class DataBaseTester {

	// If this doesn't throw an error then the database probably works
	public static void main(String[] args) {
		DataBaseConfig config = new DataBaseConfig();
		config.recordLength = 17;
		config.fields = new Integer[] { 10, 17 };

		File databaseFile = new File(
			"src/main/java/com/github/quaoz/tests/db/test.db"
		);
		File configFile = new File(
			"src/main/java/com/github/quaoz/tests/db/config.json"
		);

		Runtime
			.getRuntime()
			.addShutdownHook(
				new Thread(() -> {
					System.out.println("Do you want to delete the files? [y/n]");
					Scanner s = new Scanner(System.in);
					String str = s.nextLine();

					if (str.toLowerCase(Locale.ROOT).startsWith("y")) {
						databaseFile.delete();
						configFile.delete();
					}
				})
			);

		try (
			DataBase dataBase = new DataBase(
				databaseFile.toPath(),
				configFile.toPath(),
				config
			)
		) {
			dataBase.add("test4     4aaa  \n");
			dataBase.add("test1     0aaa  \n");
			dataBase.add("test5     0aaa  \n");
			dataBase.add("test      0bbb  \n");
			dataBase.add("test2     2sad  \n");

			dataBase.add("test8     4sad  \n");
			dataBase.add("test6     4gwe  \n");
			dataBase.add("test7     9uiy  \n");
			dataBase.add("test9     4hhh  \n");
			dataBase.add("testa     1kop  \n");
			dataBase.add("testb     7mka  \n");
			dataBase.add("test3     3sad  \n");
			dataBase.add("testc     4sad  \n");

			System.out.println(dataBase.get("test3").strip());
			System.out.println(dataBase.getRecordCount());

			dataBase.remove("test3");
			System.out.println(dataBase.get("test3"));
			System.out.println(dataBase.getRecordCount());

			System.out.println(dataBase.get("test").strip());
			System.out.println(dataBase.get("test4").strip());
			System.out.println(
				Arrays.toString(dataBase.collect("2sad", 1).toArray())
			);
			System.out.println(
				Arrays.toString(dataBase.collect("0aaa", 1).toArray())
			);
			System.out.println(
				Arrays.toString(dataBase.collect("4gwe", 1).toArray())
			);
			System.out.println(
				Arrays.toString(dataBase.collect("8aaa", 1).toArray())
			);
			System.out.println(dataBase.getRecordCount());

			System.out.println(
				Arrays.toString(dataBase.collect("1sad", 1, 4).toArray())
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (
			DataBase dataBase = new DataBase(
				databaseFile.toPath(),
				configFile.toPath()
			)
		) {
			System.out.println(dataBase.getRecordCount());
			dataBase.remove("test");
			dataBase.remove("fake");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
