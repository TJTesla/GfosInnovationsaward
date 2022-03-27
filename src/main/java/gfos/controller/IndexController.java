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
import java.util.HashMap;

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

    private HashMap<Integer, Boolean> field;
    private HashMap<Integer, Boolean> level;
    private HashMap<Integer, Boolean> time;

    private ArrayList<String> internalField;  // 1:n
    private ArrayList<String> internalLevel;  // 1:n
    private ArrayList<String> internalTime;  // 1:n
    private String maxDistance;

    @PostConstruct
    private void init() {
        /*
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
        */
    }

    public ArrayList<Offer> getAllOffers() {
        /*System.out.println("Field: " + field.size());
        System.out.println("Level: " + level.size());
        System.out.println("Time: " + time.size());
        System.out.println("Max Distance: " + maxDistance);*/

        System.out.println("search");

        if (field == null) {
            System.out.println("NULL");
        } else {
            System.out.println(field.size());
        }

        return odbs.getAllFinalOffers(
                new FilterObject(
                        createFieldFilterArray(field),
                        createLevelFilterArray(level),
                        createTimeFilterArray(time),
                        toInt(maxDistance)
                ),
                (Applicant)cu.getCurrentUser()
        );
    }

    private String findName(Integer index, ArrayList<Pair<Integer, String>> list) {
        for (Pair<Integer, String> p : list) {
            if (p.first().equals(index)) {
                return p.second();
            }
        }

        return null;
    }

    private ArrayList<String> createFieldFilterArray(HashMap<Integer, Boolean> arr) {
        return createFilterArray(arr, "field");
    }

    private ArrayList<String> createLevelFilterArray(HashMap<Integer, Boolean> arr) {
        return createFilterArray(arr, "level");
    }

    private ArrayList<String> createTimeFilterArray(HashMap<Integer, Boolean> arr) {
        return createFilterArray(arr, "time");
    }

    private ArrayList<String> createFilterArray(HashMap<Integer, Boolean> arr, String type) {
        if (arr == null) {
            return new ArrayList<>();
        }
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Pair<Integer, String>> all = new ArrayList<>();
        switch (type) {
            case "field":
                all = getAllFields();
                break;
            case "level":
                all = getAllLevels();
                break;
            case "time":
                all = getAllTimes();
                break;
        }

        for (int i = 0; i < arr.size(); i++) {
            Boolean b = arr.get(i);
            if (b) {
                result.add(findName(i, all));
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
