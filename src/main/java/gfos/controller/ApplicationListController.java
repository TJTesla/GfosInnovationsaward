package gfos.controller;

import gfos.pojos.Applicant;
import gfos.pojos.Application;
import gfos.database.ApplicantDatabaseService;
import gfos.database.ApplicationDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.detailView.DetailApplication;
import gfos.beans.CurrentUser;

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

    private String filter;

    public String filter() {
        return "";
    }

    public String delete(Application a) {
        System.out.println("DELETE IN CONTROLLER");
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
        ArrayList<Application> applications = adbs.getAllApplications( ((Applicant)cu.getCurrentUser()).getId(), draft, toInt(filter) );

        for (Application a : applications) {
            result.add(new DetailApplication(a, adbs, udbs, odbs));
        }

        return result;
    }

    private int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("Could not parse " + s + " to int");
            return -1;
        }
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
