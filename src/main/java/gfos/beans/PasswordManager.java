package gfos.beans;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordManager {
    private static final Random RANDOM = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return toHexString(salt);
    }

    public static String getHash(String str, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        str += salt;
        byte[] hash = md.digest(str.getBytes(StandardCharsets.UTF_8));
        return toHexString(hash);
    }

    private static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        return number.toString(16);
    }
}