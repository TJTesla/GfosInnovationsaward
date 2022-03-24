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

import javax.annotation.PostConstruct;
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

    private ArrayList<Boolean> field;
    private ArrayList<Boolean> level;
    private ArrayList<Boolean> time;

    private ArrayList<String> internalField;  // 1:n
    private ArrayList<String> internalLevel;  // 1:n
    private ArrayList<String> internalTime;  // 1:n
    private String maxDistance;

    @PostConstruct
    private void init() {
        field = new ArrayList<>();
        field.add(false);
        for (int i = 0; i < getAllFields().size(); i++) {
            field.add(false);
        }

        level = new ArrayList<>();
        level.add(false);
        for (int i = 0; i < getAllLevels().size(); i++) {
            level.add(false);
        }

        time = new ArrayList<>();
        time.add(false);
        for (int i = 0; i < getAllTimes().size(); i++) {
            time.add(false);
        }
    }

    public ArrayList<Offer> getAllOffers() {
        /*System.out.println("Field: " + field.size());
        System.out.println("Level: " + level.size());
        System.out.println("Time: " + time.size());
        System.out.println("Max Distance: " + maxDistance);*/

        System.out.println("search");

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

    private static ArrayList<String> createFilterArray(ArrayList<Boolean> arr) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i)) {
                result.add(String.valueOf(i));
            }
        }

        return result;
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

    public ArrayList<Boolean> getField() {
        return field;
    }

    public void setField(ArrayList<Boolean> field) {
        this.field = field;
    }

    public ArrayList<Boolean> getLevel() {
        return level;
    }

    public void setLevel(ArrayList<Boolean> level) {
        this.level = level;
    }

    public ArrayList<Boolean> getTime() {
        return time;
    }

    public void setTime(ArrayList<Boolean> time) {
        this.time = time;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }
}
