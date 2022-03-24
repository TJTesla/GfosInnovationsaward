package gfos.controller;

import gfos.FilterObject;
import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.EmployeeDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
public class CompanyIndexController {
    @Inject
    EmployeeDatabaseService edbs;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;

    public ArrayList<Application> getAllApplications(int offerId) {
        return edbs.getAllApplications();
    }

    public ArrayList<Offer> getAllFinalOffers() {
        return odbs.getAllFinalOffers(new FilterObject(), null);
    }

    public ArrayList<Offer> getAllDrafts() {
        return odbs.getAllDrafts(new FilterObject(), null);
    }

    public String checkUserRights() {
        if (cu.getCurrentUser() == null || cu.getCurrentUser() instanceof Applicant) {
            return "/00-loginRegistration/login.xhtml";
        }
        return "";
    }

    public String getApplicantName(int applicantId) {
        return adbs.getById(applicantId).getName();
    }
}
