package gfos.controller;

import gfos.beans.Applicant;
import gfos.beans.Employee;
import gfos.beans.User;
import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Named
@ViewScoped
public class LoginController implements Serializable {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicantDatabaseService adbs;

    private String username;
    private String password;

    private boolean loginError = false;

    public String login() {
        loginError = false;
        if (isSuperUser()) {
            return "/00-loginRegistration/superUser.xhtml?faces-redirect=true";
        }

        User u = adbs.loginAttempt(username, password);

        if (u == null) {
            loginError = true;
            return "";
        }

        cu.setCurrentUser(u);
        return "/index.xhtml?faces-redirect=true";
    }

    private boolean isSuperUser() {
        String passwordHash = "24e5e1c2bbef565360c392851175f46821fc21d6725503a600353625b4c9209c";
        return username.equals("sudo") && createHash(password).equals(passwordHash);
    }

    // Source: https://www.geeksforgeeks.org/sha-256-hash-in-java/
    private static String createHash(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            System.out.println("Error while creating SHA-256 Hash for String: " + input + ":" + noSuchAlgorithmException.getMessage());
            return "";
        }

        byte[] byteArray = md.digest(input.getBytes(StandardCharsets.UTF_8));
        BigInteger number = new BigInteger(1, byteArray);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginError() {
        return loginError;
    }

    public void setLoginError(boolean loginError) {
        this.loginError = loginError;
    }
}
