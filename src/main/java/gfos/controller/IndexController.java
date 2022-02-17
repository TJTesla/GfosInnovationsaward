package gfos.controller;

import gfos.exceptions.UserException;
import gfos.beans.Applicant;
import gfos.beans.Company;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
public class IndexController {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    CurrentUser cu;

    boolean isCompany;  // Maybe useful for rendering company specific ui elements

    @PostConstruct
    public void init() {
        if (cu == null || cu.getCurrentUser() == null) {
            return;
        }
        isCompany = cu.getCurrentUser() instanceof Company;
    }

    public ArrayList<Offer> getAllOffers() {
        return odbs.fetchAll();
    }

    public String getCurrentUsername() throws UserException {
        return cu.getCurrentUser().getName();
    }

    public String checkLogIn() {
        if (cu.getCurrentUser() == null) {
            return "login1.xhtml";
        } else {
            return "";
        }
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }
}
