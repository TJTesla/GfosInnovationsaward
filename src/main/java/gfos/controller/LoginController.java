package gfos.controller;

import gfos.beans.Applicant;
import gfos.database.ApplicantDatabaseService;
import gfos.sessionBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginController implements Serializable {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicantDatabaseService udbs;

    private String username;
    private String password;

    public String login() {
        Applicant applicant = udbs.loginAttempt(username, password);
        if (applicant != null) {
            cu.setCurrentUser(applicant);
            return "index.xhtml?faces-redirect=true";
        } else {
            return "";
        }
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
}
