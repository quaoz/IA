package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.BinarySearchTree;
import com.github.quaoz.util.Argon2id;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.tinylog.Logger;

// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail
// usernames 64, email address 254, password 100
// https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698
public class UserManager {
	private static final BinarySearchTree<String> commonPasswords = new BinarySearchTree<>();
	private static final URL URL;
	// Database
	private static final File USER_DB_FILE =
			new File("src/main/java/com/github/quaoz/tests/db/users.db");
	private static final File USER_CONF_FILE =
			new File("src/main/java/com/github/quaoz/tests/db/users.json");
	private static final DataBaseConfig userConfig =
			new DataBaseConfig().init(419, new Integer[] {64, 318, 418});
	private static final DataBase userDatabase =
			new DataBase(USER_DB_FILE.toPath(), USER_CONF_FILE.toPath(), userConfig);

	static {
		try {
			URL =
					new URL(
							"https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/Common-Credentials/10-million-password-list-top-1000000.txt");
		} catch (MalformedURLException e) {
			Logger.error(e, "Broken password url");
			throw new RuntimeException(e);
		}

		File commonPasswordsFile = new File("src/main/resources/common_passwords.txt");

		if (!commonPasswordsFile.exists()) {
			File destination = new File("src/main/resources/common_passwords.txt");
			File source;

			try {
				source = File.createTempFile("temp_password_list", ".txt");
			} catch (IOException e) {
				Logger.error(e, "Unable to create temporary file");
				throw new RuntimeException(e);
			}

			try (FileOutputStream fileOutputStream = new FileOutputStream(source);
					BufferedReader br = new BufferedReader(new FileReader(source));
					PrintWriter printWriter = new PrintWriter(new FileWriter(destination, true))) {
				// Download the password list
				Logger.info("Downloading common passwords...");
				long start = System.currentTimeMillis();

				ReadableByteChannel readableByteChannel = Channels.newChannel(URL.openStream());
				fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
				Logger.info(
						"Finished download, took: " + (System.currentTimeMillis() - start) + "ms");

				Logger.info("Writing passwords to file...");

				for (String line = br.readLine(); line != null; line = br.readLine()) {
					if (line.length() >= 8) {
						printWriter.println(line);
					}
				}

				Logger.info(
						"Finished writing, took: " + (System.currentTimeMillis() - start) + "ms");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (BufferedReader br = new BufferedReader(new FileReader(commonPasswordsFile))) {
			// Import the lines into a binary search tree
			Logger.info("Constructing common passwords tree...");
			long start = System.currentTimeMillis();

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				commonPasswords.add(line);
			}

			commonPasswords.balance();
			Logger.info(
					"Finished tree construction, took: "
							+ (System.currentTimeMillis() - start)
							+ "ms. Tree height: "
							+ commonPasswords.height());
		} catch (IOException e) {
			Logger.error(e, "Unable to read common password file");
			e.printStackTrace();
		}
	}

	public static void addUser(String username, String email, char[] password) {
		String record =
				String.format("%-64s%-254s%-100s\n", username, email, Argon2id.hash(password));
		userDatabase.add(record);
	}

	public static boolean validateUser(String username, char[] password) {
		String userRecord = userDatabase.get(username);

		if (userRecord != null) {
			String hash = userRecord.substring(userConfig.fields[1], userConfig.fields[2]).strip();
			return Argon2id.verify(hash, password);
		} else {
			return false;
		}
	}

	public static String getEmail(String username) {
		String user = userDatabase.get(username);
		return user.substring(userConfig.fields[0], userConfig.fields[1]).strip();
	}

	public static boolean userExists(String username) {
		return userDatabase.get(username) != null;
	}

	public static boolean isCommonPassword(char[] password) {
		return commonPasswords.contains(String.valueOf(password));
	}

	public static void close() {
		try {
			userDatabase.close();
		} catch (IOException e) {
			Logger.error(e, "Unable to close database");
			throw new RuntimeException(e);
		}
	}
}
