package gfos.controller;

import gfos.beans.User;
import gfos.database.UserDatabaseService;
import gfos.sessionBeans.CurrentUser;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
public class IndexController {
    @Inject
    UserDatabaseService udbs;
    @Inject
    CurrentUser cu;

    public ArrayList<User> getAllApplicants() {
        return udbs.fetchAll();
    }

    public String getCurrentUsername() {
        return cu.getCurrentUser().getUsername();
    }

    public String checkLogIn() {
        if (cu.getCurrentUser().getUsername().equals("")) {
            return "login.xhtml";
        } else {
            return "";
        }
    }
}
