package com.github.quaoz.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2id {
	private static final int ITERATIONS = 3;
	private static final int MEMORY = 262144;
	private static final int PARALLELISM = 1;
	private static final int SALT_LENGTH = 16;
	private static final int PASSWORD_LENGTH = 32;
	private static final Argon2 argon2id = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, SALT_LENGTH, PASSWORD_LENGTH);

	public static String hash(char[] password) {
		String passwordHash;

		// Generate the hash from the user's password.
		try {
			passwordHash = argon2id.hash(ITERATIONS, MEMORY, PARALLELISM, password);
			System.out.println("verify(passwordHash, password) = " + verify(passwordHash, password));
		} finally {
			// Wipe confidential data
			argon2id.wipeArray(password);
		}

		return passwordHash;
	}

	public static boolean verify(String keyStoreHash, char[] password) {
		try {
			return argon2id.verify(keyStoreHash, password);
		} finally {
			// Wipe confidential data
			argon2id.wipeArray(password);
		}
	}
}
