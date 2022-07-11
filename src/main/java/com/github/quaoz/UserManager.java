package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.nio.file.Path;
import java.time.Duration;

public class UserManager {
	private static final int MEMORY = 20 * 1024;
	private static final int SALT_LENGTH = 16;
	private static final int HASH_LENGTH = 32;
	private static final int PARALLELISM = 1;
	private static final int ITERATIONS = 20;
	private static final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS);

	// usernames: 64 characters, email address 254, password 100
	// https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698
	private static final int USERNAME_END = 64;
	private static final int EMAIL_END = 319;
	private static final int PASSWORD_END = 420;
	private static final Path dataBaseFile = Path.of("src/main/java/com/github/quaoz/tests/users.db");
	private static final Path dataBaseConfigFile = Path.of("src/main/java/com/github/quaoz/tests/users.json");
	private static final DataBase userDataBase = new DataBase(dataBaseFile, dataBaseConfigFile);

	static {
		final DataBaseConfig dataBaseConfig = new DataBaseConfig();
		dataBaseConfig.recordLength = 420;
		dataBaseConfig.fields = new Integer[]{USERNAME_END, EMAIL_END, PASSWORD_END};
	}

	private static String encode(String password) {
		return passwordEncoder.encode(password);
	}

	public static boolean addUser(String username, String email, String password) {
		String record = String.format("%-64s %-254s %-32s", username, email, encode(password));

		if (record.length() != 420) {
			return false;
		}

		userDataBase.add(record);
		return true;
	}

	public static String getEmail(String username) {
		String user = userDataBase.get(username, 0);
		return user.substring(USERNAME_END, EMAIL_END).strip();
	}

	public static boolean userExists(String username) {
		return userDataBase.get(username, 0) != null;
	}

	public static void main(String[] args) {
		// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
		// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail

		// Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
		// Matcher matcher = emailPattern.matcher("email@email.com");
		// System.out.println(matcher.matches());


	}

	private static void argon2spring() {
		int saltLength = 16; // 128 bits
		int hashLength = 32; // 256 bits
		int parallelism = 1;
		int memoryInKb = 20 * 1024; // 20 MB
		int iterations = 20;
		Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memoryInKb, iterations);

		long start = System.nanoTime();
		String hashed = passwordEncoder.encode("1234567812345678123456781234567812345678");
		long took = System.nanoTime() - start;

		System.out.println(hashed);
		System.out.println("Took " + Duration.ofNanos(took).toMillis() + " ms");

		start = System.nanoTime();
		boolean matches = new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memoryInKb, iterations).matches("1234567812345678123456781234567812345678", hashed);
		took = System.nanoTime() - start;
		System.out.println("Took " + Duration.ofNanos(took).toMillis() + " ms");

		if (matches) {
			System.out.println("Matches!");
		} else {
			System.out.println("Does NOT match!");
		}
	}
}
