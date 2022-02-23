package gfos.controller;

import gfos.beans.Applicant;
import gfos.beans.Company;
import gfos.beans.User;
import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
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
        User u = adbs.loginAttempt(username, password);

        if (u == null) {
            loginError = true;
            return "";
        }

        if (u instanceof Applicant) {
            cu.setCurrentUser((Applicant)u);
        } else if (u instanceof Company) {
            cu.setCurrentUser((Company)u);
        }

        return "/index.xhtml?faces-redirect=true";
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
