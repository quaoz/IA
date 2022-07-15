package com.github.quaoz.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Argon2id {
	private static final int ITERATIONS = 3;
	private static final int MEMORY = 262144;
	private static final int PARALLELISM = 1;
	private static final int SALT_LENGTH = 16;
	private static final int HASH_LENGTH = 32;
	private static final Argon2Factory.Argon2Types TYPE = Argon2Factory.Argon2Types.ARGON2id;
	private static final Argon2 argon2id = Argon2Factory.create(TYPE, SALT_LENGTH, HASH_LENGTH);

	// https://www.twelve21.io/how-to-use-argon2-for-password-hashing-in-java/
	// https://www.twelve21.io/how-to-choose-the-right-parameters-for-argon2/
	// https://cryptobook.nakov.com/mac-and-key-derivation/password-encryption#secure-kdf-based-password-hashing-recommended

	@Contract(" -> new")
	private static @NotNull Argon2 getArgon() {
		return Argon2Factory.create(TYPE, SALT_LENGTH, HASH_LENGTH);
	}

	public static String hash(char[] password) {
		String passwordHash;

		// Generate the hash from the user's password.
		try {
			passwordHash = argon2id.hash(ITERATIONS, MEMORY, PARALLELISM, password);
		} finally {
			// Wipe confidential data
			argon2id.wipeArray(password);
		}

		return passwordHash;
	}

	public static boolean verify(String keyStoreHash, char[] password) {
		try {
			return getArgon().verify(keyStoreHash, password);
		} finally {
			// Wipe confidential data
			// argon2id.wipeArray(password);
		}
	}
}
