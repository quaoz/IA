package com.github.quaoz;

import com.github.quaoz.database.DataBase;
import com.github.quaoz.database.DataBaseConfig;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class UserManager {
    private static final int MEMORY = 20 * 1024;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;
    private static final int ITERATIONS = 20;

    private static Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS);

    // usernames up to 64 characters, email 254 https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/574698#574698https://stackoverflow.com/questions/3797098/what-are-the-standard-minimum-and-maximum-lengths-of-username-password-and-emai#:~:text=According%20to%20RFC%205321%20(SMTP,That's%20bytes%2C%20not%20characters%3B%20in
    private static final DataBaseConfig dataBaseConfig = new DataBaseConfig(128, new Integer[]{64, 318 });
    private static final Path dataBaseFile = Path.of("src/main/java/com/github/quaoz/tests/users.db");
    private static final Path dataBaseConfigFile = Path.of("src/main/java/com/github/quaoz/tests/users.json");
    private static DataBase userDataBase = new DataBase(dataBaseFile, dataBaseConfigFile);

    private static String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public static void addUser(String username, String email, String password) {

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
