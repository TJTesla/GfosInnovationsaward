package gfos.controller;

import gfos.pojos.SuperUser;
import gfos.pojos.User;
import gfos.database.ApplicantDatabaseService;
import gfos.beans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Controller zum Einloggen als Bewerber, Angestellter oder Admin

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
            cu.setCurrentUser(new SuperUser());
            return "/00-loginRegistration/superUser.xhtml?faces-redirect=true";
        }

        // Methode gibt null zurück falls keine passenden Anmeldedaten gefunden wurden
        // Ansonsten ein Objekt für den passenden Nutzer
        // Employee und Applicant erben beide von User -> Polymorphie
        User u = adbs.loginAttempt(username, password);

        if (u == null) {
            loginError = true;
            return "";
        }

        cu.setCurrentUser(u);
        return "/index.xhtml?faces-redirect=true";
    }

    private boolean isSuperUser() {
        // Hash durch SHA-256 Hash für neues Passwort von Admin ersetzen
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

    // Getter und Setter //

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
