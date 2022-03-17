package gfos.controller;

import gfos.FilterObject;
import gfos.beans.Employee;
import gfos.database.MiscellaneousDatabaseService;
import gfos.exceptions.UserException;
import gfos.beans.Applicant;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;
import gfos.Pair;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class IndexController implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    MiscellaneousDatabaseService mdbs;
    @Inject
    CurrentUser cu;

    private ArrayList<String> field;  // 1:n
    private ArrayList<String> level;  // 1:n
    private ArrayList<String> time;  // 1:n
    private String maxDistance;

    public ArrayList<Offer> getAllOffers() {
        System.out.println("Field: " + field);

        ArrayList<Offer> offerList =  odbs.fetchAll(
                new FilterObject(
                        field,
                        level,
                        time,
                        toInt(maxDistance)
                ),
                (Applicant) cu.getCurrentUser()
        );

        return offerList;
    }

    private Integer toInt(String str) {
        if (str == null || str.equals("-1")) {
            return null;
        }

        try {
            return Integer.getInteger(str);
        } catch (NumberFormatException nfe) {
            System.out.println("Cannot change string: " + str + " to Integer: " + nfe.getMessage());
            return null;
        }
    }

    public String offerDetailPage(int offerId) {
        return "/02-offer/offer.xhtml?id=" + offerId;
    }

    public String checkLogIn() {
        if (cu.getCurrentUser() == null) {
            return "/00-loginRegistration/login.xhtml";
        } else {
            return "";
        }
    }

    public String reload() {
        return "";
    }

    public boolean isEmployee() {
        return cu.getCurrentUser() instanceof Employee;
    }

    public ArrayList<Pair<Integer, String>> getAllFields() {
        return mdbs.getAllFields();
    }

    public ArrayList<Pair<Integer, String>> getAllLevels() {
        return mdbs.getAllLevels();
    }

    public ArrayList<Pair<Integer, String>> getAllTimes() {
        return mdbs.getAllTimes();
    }

    public ArrayList<String> getField() {
        return field;
    }

    public void setField(ArrayList<String> field) {
        this.field = field;
    }

    public ArrayList<String> getLevel() {
        return level;
    }

    public void setLevel(ArrayList<String> level) {
        this.level = level;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(ArrayList<String> time) {
        this.time = time;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }
}
