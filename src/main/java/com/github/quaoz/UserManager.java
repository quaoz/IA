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
	private static final DataBaseConfig userConfig = new DataBaseConfig().init(418, new Integer[]{64, 318, 418});
	private static final DataBase userDatabase = new DataBase(userDatabaseFile.toPath(), userConfigFile.toPath(), userConfig);

	public static void addUser(String username, String email, char[] password) {
		String record = String.format("%-64s%-254s%-100s", username, email, Argon2id.hash(password));
		userDatabase.add(record);
	}

	public static boolean validateUser(String username, char[] password) {
		String userRecord = userDatabase.get(username, 0);

		if (userRecord != null) {
			String hash = userRecord.substring(userConfig.fields[1], userConfig.fields[2]).strip();
			return Argon2id.verify(hash, password);
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
