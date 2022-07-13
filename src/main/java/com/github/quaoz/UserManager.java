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
	// usernames 64, email address 254, password 100
	// https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698
	private static final DataBaseConfig userConfig = new DataBaseConfig().init(418, new Integer[]{64, 319, 418});
	private static final DataBase userDatabase = new DataBase(userDatabaseFile.toPath(), userConfigFile.toPath(), userConfig);

	private static String encode(char[] password) {
		return passwordEncoder.encode(String.valueOf(password));
	}

	public static boolean addUser(String username, String email, char[] password) {
		String encode = encode(password);
		String record = String.format("%-64s%-254s%-100s", username, email, encode);

		System.out.println(String.valueOf(password));
		System.out.println(record);
		System.out.println(record.length());

		// spaces dickhead!!!!!

		System.out.println(record.substring(userConfig.fields[1], userConfig.fields[2]));
		assert record.substring(userConfig.fields[1], userConfig.fields[2]).equals(encode);
		System.out.println(passwordEncoder.matches(String.valueOf(password), encode));
		System.out.println(passwordEncoder.matches(String.valueOf(password), record.substring(userConfig.fields[1], userConfig.fields[2])));
		System.out.println("\"" + encode + "\"");

		if (record.length() != userConfig.recordLength + 1) {
			return false;
		}

		userDatabase.add(record);
		return true;
	}

	public static boolean validateUser(String username, char[] password) {
		String userRecord = userDatabase.get(username, 0);

		String hashed = passwordEncoder.encode("1234567812345678123456781234567812345678");
		System.out.println(hashed);
		boolean matches = passwordEncoder.matches("1234567812345678123456781234567812345678", hashed);
		System.out.println(matches);

		System.out.println(String.valueOf(password));
		String hash = userRecord.substring(userConfig.fields[1], userConfig.fields[2]).strip();
		System.out.println(hash);
		System.out.println(passwordEncoder.matches(String.valueOf(password), hash));

		if (userRecord != null) {
			return passwordEncoder.matches(String.valueOf(password), userRecord.substring(userConfig.fields[1], userConfig.fields[2]).strip());
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
