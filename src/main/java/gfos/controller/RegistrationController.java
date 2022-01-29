package gfos.controller;

import gfos.database.MiscellaneousDatabaseService;
import gfos.database.UserDatabaseService;
import gfos.beans.User;
import gfos.sessionBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class RegistrationController implements Serializable {
    @Inject
    UserDatabaseService udbs;
    @Inject
    MiscellaneousDatabaseService mdbs;
    @Inject
    CurrentUser cu;

    private String salutation;
    private String[] titles = new String[3];
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String emailRepeat;
    private String password;
    private String passwordRepeat;
    private String userType = "private";

    private boolean loginError = false;
    private String loginErrorMsg;


    public String register() {
        if (!email.equals(emailRepeat)) {
            loginError = true;
            loginErrorMsg = "Nicht übereinstimmende E-Mails";
            return "";
        }
        if (!password.equals(passwordRepeat)) {
            loginError = true;
            loginErrorMsg = "Nicht übereinstimmende Passwörter";
            return "";
        }
        if (salutation.equals("")) {
            loginError = true;
            loginErrorMsg = "Bestimmen Sie eine Anrede";
            return "";
        }

        User user = new User(0, username, password, firstname, lastname, Integer.parseInt(salutation));
        if (udbs.exists(user)) {
            loginError = true;
            loginErrorMsg = "Der Nutzer existiert bereits.";
            return "";
        }

        if (userType.equals("private")) {
            udbs.createOne(user);

            cu.setCurrentUser(user);
        } else {
            // TODO: Register as comapny
        }

        return "index.xhtml?faces-redirect=true";
    }

    public String[] getAllTitles() {
        ArrayList<String> arr = mdbs.getAllTitles();
        String[] res = new String[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            res[i] = arr.get(i);
        }

        return res;
    }



    public boolean isLoginError() {
        return loginError;
    }

    public void setLoginError(boolean loginError) {
        this.loginError = loginError;
    }

    public String getLoginErrorMsg() {
        return loginErrorMsg;
    }

    public void setLoginErrorMsg(String loginErrorMsg) {
        this.loginErrorMsg = loginErrorMsg;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getEmailRepeat() {
        return emailRepeat;
    }

    public void setEmailRepeat(String emailRepeat) {
        this.emailRepeat = emailRepeat;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
