package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.io.File;

// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail
public class UserManager {
	// Password encoder constants
	private static final int MEMORY = 20 * 1024;
	private static final int SALT_LENGTH = 16;
	private static final int HASH_LENGTH = 32;
	private static final int PARALLELISM = 1;
	private static final int ITERATIONS = 20;
	private static final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS);

	// Database
	private static final File userDatabaseFile = new File("src/main/java/com/github/quaoz/tests/db/users.db");
	private static final File userConfigFile = new File("src/main/java/com/github/quaoz/tests/db/users.json");
	private static final DataBaseConfig userConfig = new DataBaseConfig();

	static {
		userConfig.recordLength = 418;
		// usernames 64, email address 254, password 100
		// https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698
		userConfig.fields = new Integer[]{64, 319, 418};
	}

	private static final DataBase userDatabase = new DataBase(userDatabaseFile.toPath(), userConfigFile.toPath(), userConfig);

	private static String encode(String password) {
		return passwordEncoder.encode(password);
	}

	public static boolean addUser(String username, String email, String password) {
		String record = String.format("%-64s %-254s %-32s", username, email, encode(password));

		if (record.length() != userConfig.recordLength) {
			return false;
		}

		userDatabase.add(record);
		return true;
	}

	public static boolean validateUser(String username, String password) {
		String userRecord = userDatabase.get(username, 0);
		password = encode(password);

		if (userRecord != null) {
			return passwordEncoder.matches(password, userRecord.substring(userConfig.fields[1], userConfig.fields[2]).strip());
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
