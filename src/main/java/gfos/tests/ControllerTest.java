package gfos.tests;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Employee;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.EmployeeDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.inject.Named;
import javax.inject.Inject;

@Named
public class ControllerTest {
    @Inject
    EmployeeDatabaseService edbs;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;

    public boolean checkApplicant () {
        return cu.getCurrentUser() instanceof Applicant;
    }

    public boolean checkEmployee () {
        return cu.getCurrentUser() instanceof Employee;
    }

    public boolean checkUserRights () {
        return cu.getCurrentUser() != null;
    }

    public String logout() {
        cu.logout();
        return "/00-loginRegistration/login.xhtml?faces-redirect=true";
    }
}
