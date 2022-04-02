package gfos.controller;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.ApplicationDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.detailView.DetailApplication;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class ApplicationListController implements Serializable {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicationDatabaseService adbs;
    @Inject
    ApplicantDatabaseService udbs;
    @Inject
    OfferDatabaseService odbs;

    public ArrayList<DetailApplication> appliedOffers() {
        ArrayList<DetailApplication> result = new ArrayList<>();
        ArrayList<Application> applications = adbs.getAllApplications( ((Applicant)cu.getCurrentUser()).getId() );

        for (Application a : applications) {
            result.add(new DetailApplication(a, adbs, udbs, odbs));
        }

        return result;
    }
}
