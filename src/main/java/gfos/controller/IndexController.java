package gfos.controller;

import gfos.requestBeans.Applicant;
import gfos.database.UserDatabaseService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
public class IndexController {
    @Inject
    UserDatabaseService udbs;

    public ArrayList<Applicant> getAllApplicants() {
        return udbs.fetchAll();
    }
}
