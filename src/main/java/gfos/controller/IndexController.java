package gfos.controller;

import gfos.RequestBeans.User;
import gfos.database.UserDatabaseService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
public class IndexController {
    @Inject
    UserDatabaseService udbs;

    public ArrayList<User> getAllApplicants() {
        return udbs.fetchAll();
    }
}
