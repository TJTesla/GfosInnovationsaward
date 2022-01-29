package gfos.controller;

import gfos.database.UserDatabaseService;
import gfos.beans.Applicant;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class registrationController implements Serializable {
    @Inject
    UserDatabaseService udbs;

    private String salutation;
    private String[] titles = new String[3];
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
        Applicant applicant = new Applicant(

        );

        return null;
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
