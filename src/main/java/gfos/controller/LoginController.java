package gfos.controller;

import gfos.beans.User;
import gfos.database.UserDatabaseService;
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
    UserDatabaseService udbs;

    private String username;
    private String password;

    public String login() {
        User user = udbs.loginAttempt(username, password);
        if (user != null) {
            cu.setCurrentUser(user);
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
