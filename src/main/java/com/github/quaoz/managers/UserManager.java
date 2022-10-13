package com.github.quaoz.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quaoz.Main;
import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import com.github.quaoz.structures.BinarySearchTree;
import com.github.quaoz.util.Argon2id;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail
// usernames 64, email address 254, password 100
// https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698
public class UserManager implements Closeable {

	private static UserManager userManager;
	private final BinarySearchTree<String> commonPasswords = new BinarySearchTree<>();
	private final DataBaseConfig userConfig;
	private final DataBase userDatabase;
	private UserAuthLevels userAuthLevel;
	private String user;

	@SuppressWarnings("unchecked")
	private UserManager() {
		Logger.info("Creating user manager...");

		final Path USER_DB_FILE = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("db", "users.db"));
		final Path USER_CONF_FILE = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("db", "users.json"));

		userConfig =
			new DataBaseConfig().init(423, new Integer[] { 64, 318, 418, 422 });
		userDatabase = new DataBase(USER_DB_FILE, USER_CONF_FILE, userConfig);

		authRequestsFile =
			Main
				.getInstance()
				.getInstallDir()
				.resolve(Paths.get("data", "auth.json"))
				.toFile();
		authRequests = new HashMap<>();

		try {
			if (!authRequestsFile.exists()) {
				Files.createDirectories(authRequestsFile.toPath().getParent());
				Files.createFile(authRequestsFile.toPath());
				new ObjectMapper()
					.writeValue(authRequestsFile, new HashMap<String, Integer>());
			}
			new ObjectMapper()
				.readValue(authRequestsFile, HashMap.class)
				.forEach((k, v) -> authRequests.put((String) k, (Integer) v));
		} catch (IOException e) {
			Logger.error(
				e,
				"Unable to read user auth requests file at: " + authRequestsFile
			);
			throw new RuntimeException(e);
		}

		userAuthLevel = UserAuthLevels.NONE;
		user = "";

		URL url;
		try {
			url =
				new URL(
					"https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/Common-Credentials/10-million-password-list-top-1000000.txt"
				);
		} catch (MalformedURLException e) {
			Logger.error(e, "Broken password url");
			throw new RuntimeException(e);
		}

		File commonPasswordsFile = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("data", "common_passwords.txt"))
			.toFile();

		if (!commonPasswordsFile.exists()) {
			Logger.info("Creating common password list...");

			try {
				Files.createDirectories(commonPasswordsFile.toPath().getParent());
				Files.createFile(commonPasswordsFile.toPath());
			} catch (IOException e) {
				Logger.error(e, "Unable to create common password file");
				throw new RuntimeException(e);
			}

			File source;

			try {
				source = File.createTempFile("temp_password_list", ".txt");
			} catch (IOException e) {
				Logger.error(e, "Unable to create temporary file");
				throw new RuntimeException(e);
			}

			try (
				FileOutputStream fileOutputStream = new FileOutputStream(source);
				BufferedReader br = new BufferedReader(new FileReader(source));
				PrintWriter printWriter = new PrintWriter(
					new FileWriter(commonPasswordsFile, true)
				)
			) {
				// Download the password list
				Logger.info("Downloading common passwords...");
				long start = System.currentTimeMillis();

				ReadableByteChannel readableByteChannel = Channels.newChannel(
					url.openStream()
				);
				fileOutputStream
					.getChannel()
					.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
				Logger.info(
					"Finished download, took: " +
					(System.currentTimeMillis() - start) +
					"ms"
				);

				Logger.info("Writing passwords to file...");

				for (String line = br.readLine(); line != null; line = br.readLine()) {
					if (line.length() >= 8) {
						printWriter.println(line);
					}
				}

				Logger.info(
					"Finished writing, took: " +
					(System.currentTimeMillis() - start) +
					"ms"
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (
			BufferedReader br = new BufferedReader(
				new FileReader(commonPasswordsFile)
			)
		) {
			// Import the lines into a binary search tree
			Logger.info("Constructing common passwords tree...");
			long start = System.currentTimeMillis();

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				commonPasswords.add(line);
			}

			commonPasswords.balance();
			Logger.info(
				"Finished tree construction, took: " +
				(System.currentTimeMillis() - start) +
				"ms. Tree height: " +
				commonPasswords.height()
			);
		} catch (IOException e) {
			Logger.error(e, "Unable to read common password file");
			e.printStackTrace();
		}

		Logger.info("Finished creating user manager");
	}

	private final File authRequestsFile;
	private final HashMap<String, Integer> authRequests;

	public void requestAuth() {
		authRequests.put(user, UserAuthLevels.get(userAuthLevel));
	}

	public HashMap<String, Integer> getAuthRequests() {
		return authRequests;
	}

	public static synchronized void init() {
		if (userManager == null) {
			userManager = new UserManager();
		}
	}

	public static synchronized UserManager getInstance() {
		return userManager;
	}

	public UserAuthLevels getUserAuthLevel() {
		return userAuthLevel;
	}

	public void upgradeUserAuthLevel(String user) {
		String newUser = String.format(
			"%-418s%-4s\n",
			userDatabase.get(user).substring(0, userConfig.fields[2]),
			authRequests.get(user) + 1
		);
		authRequests.remove(user);
		userDatabase.remove(user);
		userDatabase.add(newUser);
	}

	public void declineUserAuthRequest(String user) {
		authRequests.remove(user);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
		userAuthLevel = getAuthLevel(user);
	}

	public void addUser(String username, String email, char[] password) {
		String record = String.format(
			"%-64s%-254s%-100s%-4s\n",
			username,
			email,
			Argon2id.hash(password),
			1
		);
		userDatabase.add(record);
	}

	public boolean validateUser(String username, char[] password) {
		String userRecord = userDatabase.get(username);

		if (userRecord != null) {
			String hash = userRecord
				.substring(userConfig.fields[1], userConfig.fields[2])
				.strip();
			return Argon2id.verify(hash, password);
		} else {
			return false;
		}
	}

	public String getEmail(String username) {
		String user = userDatabase.get(username);
		return user.substring(userConfig.fields[0], userConfig.fields[1]).strip();
	}

	public UserAuthLevels getAuthLevel(String username) {
		String user = userDatabase.get(username);

		int level = 0;
		if (user != null) {
			level =
				Integer.parseInt(
					user.substring(userConfig.fields[2], userConfig.fields[3]).strip()
				);
		}

		return UserAuthLevels.get(level);
	}

	public boolean userExists(String username) {
		return userDatabase.get(username) != null;
	}

	public boolean isCommonPassword(char[] password) {
		return commonPasswords.contains(String.valueOf(password));
	}

	public void close() {
		try {
			userDatabase.close();
			new ObjectMapper().writeValue(authRequestsFile, authRequests);
		} catch (IOException e) {
			Logger.error(e, "Unable to close manager");
			throw new RuntimeException(e);
		}
	}

	public enum UserAuthLevels {
		NONE,
		USER,
		MODERATOR,
		ADMIN;

		public static UserAuthLevels get(int i) {
			return switch (i) {
				case 1 -> USER;
				case 2 -> MODERATOR;
				case 3 -> ADMIN;
				default -> NONE;
			};
		}

		@Contract(pure = true)
		public static int get(@NotNull UserAuthLevels i) {
			return switch (i) {
				case USER -> 1;
				case MODERATOR -> 2;
				case ADMIN -> 3;
				default -> 0;
			};
		}
	}
}
