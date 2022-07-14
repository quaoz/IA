package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.util.Argon2id;

import java.io.File;

// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail
public class UserManager {
	// Database
	private static final File userDatabaseFile = new File("src/main/java/com/github/quaoz/tests/db/users.db");
	private static final File userConfigFile = new File("src/main/java/com/github/quaoz/tests/db/users.json");
	// usernames 64, email address 254, password 100
	// https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698
	private static final DataBaseConfig userConfig = new DataBaseConfig().init(418, new Integer[]{64, 319, 418});
	private static final DataBase userDatabase = new DataBase(userDatabaseFile.toPath(), userConfigFile.toPath(), userConfig);

	public static boolean addUser(String username, String email, char[] password) {
		String hash = Argon2id.hash(password);
		String record = String.format("%-64s%-254s%-100s", username, email, hash);

		System.out.println("hash = " + hash);
		System.out.println("record = " + record);
		userDatabase.add(record);
		return true;
	}

	public static boolean validateUser(String username, char[] password) {
		String userRecord = userDatabase.get(username, 0);

		if (userRecord != null) {
			System.out.println("userRecord = " + userRecord);
			return Argon2id.verify(userRecord.substring(userConfig.fields[1], userConfig.fields[2]).strip(), password);
		} else {
			return false;
		}
	}

	public static String getEmail(String username) {
		String user = userDatabase.get(username, 0);
		return user.substring(userConfig.fields[0], userConfig.fields[1]).strip();
	}

	public static boolean userExists(String username) {
		return userDatabase.get(username, 0) != null;
	}
}
