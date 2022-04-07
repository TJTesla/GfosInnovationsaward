package gfos.tests;

import gfos.pojos.PasswordManager;

import java.security.NoSuchAlgorithmException;

public class PasswordTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String salt = PasswordManager.generateSalt();
        String hash = PasswordManager.getHash("root", salt);

        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);

        System.out.println("----------");

        salt = PasswordManager.generateSalt();
        hash = PasswordManager.getHash("root", salt);

        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
    }
}
