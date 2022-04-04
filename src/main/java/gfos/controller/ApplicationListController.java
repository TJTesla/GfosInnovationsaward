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

    public String delete(Application a) {
        adbs.delete(a);
        return "";
    }

    public ArrayList<DetailApplication> appliedOffers() {
        return offerList(false);
    }

    public ArrayList<DetailApplication> getDrafts() {
        return offerList(true);
    }

    private ArrayList<DetailApplication> offerList(boolean draft) {
        ArrayList<DetailApplication> result = new ArrayList<>();
        ArrayList<Application> applications = adbs.getAllApplications( ((Applicant)cu.getCurrentUser()).getId(), draft );

        for (Application a : applications) {
            result.add(new DetailApplication(a, adbs, udbs, odbs));
        }

        return result;
    }
}
