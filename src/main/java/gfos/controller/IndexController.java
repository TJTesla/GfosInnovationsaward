package gfos.controller;

import gfos.FilterObject;
import gfos.beans.Employee;
import gfos.beans.User;
import gfos.database.MiscellaneousDatabaseService;
import gfos.exceptions.UserException;
import gfos.beans.Applicant;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;
import gfos.Pair;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private HashMap<Integer, Boolean> field = new HashMap<>();
    private HashMap<Integer, Boolean> level = new HashMap<>();
    private HashMap<Integer, Boolean> time = new HashMap<>();

    private String maxDistance;

    public ArrayList<Offer> getAllOffers() {
        System.out.println("search");

        if (field == null) {
            System.out.println("NULL");
        } else {
            for (int i = 0; i < field.size(); i++) {
                System.out.println(i + ": " + field.get(i));
            }
        }

        return odbs.getAllFinalOffers(
                new FilterObject(
                        createFilterArray(field),
                        createFilterArray(level),
                        createFilterArray(time),
                        toInt(maxDistance)
                ),
                (Applicant)cu.getCurrentUser()
        );
    }

    private static ArrayList<Integer> createFilterArray(HashMap<Integer, Boolean> arr) {
        if (arr == null) {
            return new ArrayList<>();
        }
        ArrayList<Integer> result = new ArrayList<>();

        for (Map.Entry<Integer, Boolean> i : arr.entrySet()) {
            if (i.getValue()) {
                result.add(i.getKey());
            }
        }

        return result;
    }

    private int toInt(String str) {
        if (str == null || str.equals("-1")) {
            return -1;
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            System.out.println("Cannot change string: " + str + " to Integer: " + nfe.getMessage());
            return -1;
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

    public boolean loggedIn() {
        User u = cu.getCurrentUser();
        return u instanceof Applicant;
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

    public HashMap<Integer, Boolean> getField() {
        return field;
    }

    public void setField(HashMap<Integer, Boolean> field) {
        this.field = field;
    }

    public HashMap<Integer, Boolean> getLevel() {
        return level;
    }

    public void setLevel(HashMap<Integer, Boolean> level) {
        this.level = level;
    }

    public HashMap<Integer, Boolean> getTime() {
        return time;
    }

    public void setTime(HashMap<Integer, Boolean> time) {
        this.time = time;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }
}
