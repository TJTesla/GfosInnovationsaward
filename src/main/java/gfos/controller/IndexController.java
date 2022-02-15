package gfos.controller;

import gfos.UserException;
import gfos.beans.Applicant;
import gfos.beans.Company;
import gfos.beans.Offer;
import gfos.beans.User;
import gfos.database.ApplicantDatabaseService;
import gfos.database.DatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.sessionBeans.CurrentUser;

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
        isCompany = cu.getCurrentUser().getClass() == Company.class;
    }

    public ArrayList<Offer> getAllOffers() {
        return odbs.fetchAll();
    }

    public String getCurrentUsername() throws UserException {
        if (cu.getCurrentUser().getClass() == Applicant.class) {
            return ((Applicant)cu.getCurrentUser()).getUsername();
        } else if (cu.getCurrentUser().getClass() == Company.class) {
            return ((Company)cu.getCurrentUser()).getName();
        }

        throw new UserException("Current user has an undefined behaviour for its current user class");
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
