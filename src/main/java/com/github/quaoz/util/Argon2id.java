package com.github.quaoz.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2id {
  private static final int ITERATIONS = 4;
  private static final int MEMORY = 256 * 1024; // 256 kibikytes
  private static final int PARALLELISM = 1;
  private static final int SALT_LENGTH = 16;
  private static final int HASH_LENGTH = 32;
  private static final Argon2Factory.Argon2Types TYPE = Argon2Factory.Argon2Types.ARGON2id;
  private static final Argon2 ARGON_2 = Argon2Factory.create(TYPE, SALT_LENGTH, HASH_LENGTH);

  // https://www.twelve21.io/how-to-use-argon2-for-password-hashing-in-java/
  // https://www.twelve21.io/how-to-choose-the-right-parameters-for-argon2/
  // https://cryptobook.nakov.com/mac-and-key-derivation/password-encryption#secure-kdf-based-password-hashing-recommended

  /**
   * Returns the hashed password
   *
   * @param password The password
   * @return The hashed password
   */
  public static String hash(char[] password) {
    // Generate the hash from the user's password.
    String passwordHash = ARGON_2.hash(ITERATIONS, MEMORY, PARALLELISM, password);

    // Wipe confidential data
    ARGON_2.wipeArray(password);

    return passwordHash;
  }

  /**
   * Verifies whether the password matches the hash
   *
   * @param passwordHash The hashed password
   * @param password The password
   * @return {@code true} if the password matches the hash
   */
  public static boolean verify(String passwordHash, char[] password) {
    boolean valid = ARGON_2.verify(passwordHash, password);

    // Wipe confidential data
    ARGON_2.wipeArray(password);

    return valid;
  }
}
