package gfos.controller;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Offer;
import gfos.database.CompanyDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
public class CompanyIndexController {
    @Inject
    CompanyDatabaseService cdbs;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    CurrentUser cu;

    public ArrayList<Application> getAllApplications(int offerId) {
        return cdbs.getAllApplications(offerId);
    }

    public ArrayList<Offer> getAllOffers() {
        return odbs.getAllOffers(cu.getCurrentUser().getId());
    }

    public String checkUserRights() {
        if (cu.getCurrentUser() == null || cu.getCurrentUser() instanceof Applicant) {
            return "/00-loginRegistration/login.xhtml";
        }
        return "";
    }
}
