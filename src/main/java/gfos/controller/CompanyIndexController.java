package gfos.controller;

import gfos.pojos.FilterObject;
import gfos.pojos.Applicant;
import gfos.pojos.Application;
import gfos.pojos.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.EmployeeDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.beans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class CompanyIndexController implements Serializable {
    @Inject
    EmployeeDatabaseService edbs;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;

    private Offer offer;

    public ArrayList<Application> getAllApplications(int offerId) {
        return edbs.getAllApplications(offerId);
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

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
