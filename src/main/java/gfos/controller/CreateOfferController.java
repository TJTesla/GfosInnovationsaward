package gfos.controller;

import gfos.pojos.Applicant;
import gfos.pojos.Pair;
import gfos.pojos.Employee;
import gfos.pojos.Offer;
import gfos.database.MiscellaneousDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.beans.CurrentUser;
import gfos.beans.GeoCalculator;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

// Controller zum Erstellen eines Angebots

@Named
@ViewScoped
public class CreateOfferController implements Serializable {
    // Alle Felder
    private int id = -1;
    private boolean hasCoordinates = false;

    private String title;
    private String tasks, qualifications, extras;
    private String field;
    private String level;
    private String time;

    private String street;
    private String postalCode;
    private String city;

    public void setFromDraft(Offer o) {
        if (o == null) {
            return;
        }
        this.id = o.getId();
        this.hasCoordinates = o.getLat() != -1;

        this.title = o.getTitle();
        this.tasks = o.getTasks();
        this.qualifications = o.getQualifications();
        this.extras = o.getExtras();
        this.field = String.valueOf(o.getField());
        this.level = String.valueOf(o.getLevel());
        this.time = String.valueOf(o.getTime());
        this.city = o.getCity();
    }

    public Offer getFromDraft() {
        return new Offer();
    }

    @Inject
    CurrentUser cu;
    @Inject
    OfferDatabaseService odbs;
    @Inject
    MiscellaneousDatabaseService mdbs;

    private boolean creationError = false;
    private HashMap<String, String> errorMsgs = new HashMap<>();

    public String isEmployee() {
        if (cu.getCurrentUser() instanceof Employee) {
            return "";
        }
        return "/00-loginRegistration/login.xhtml";
    }

    public String save(boolean draft) {
        creationError = false;
        errorMsgs.clear();
        checkCreation(draft);
        if (creationError) {
            return "";
        }

        if (id != -1) {
            return update(draft);
        }

        double[] coordinates = { -1, -1 };
        if (!(street.isEmpty() || postalCode.isEmpty() || city.isEmpty())) {
            String location = street + " " + postalCode + " " + city;
            coordinates = GeoCalculator.getCoordinates(location);
        }

        int oId = odbs.createOne(new Offer(
                -1,
                title,
                tasks,
                qualifications,
                extras,
                toInt(field),
                toInt(level),
                toInt(time),
                coordinates[0],
                coordinates[1],
                draft,
                city
        ));

        return "/02-offer/offer.xhtml?faces-redirect=true&id=" + oId;
    }

    private String update(boolean draft) {
        double[] coordinates = { -1, -1 };
        if (!hasCoordinates) {
            if (!(street.isEmpty() || postalCode.isEmpty() || city.isEmpty())) {
                String location = street + " " + postalCode + " " + city;
                coordinates = GeoCalculator.getCoordinates(location);
            }
        }

        Offer o = new Offer(
                id,
                title,
                tasks,
                qualifications,
                extras,
                toInt(field),
                toInt(level),
                toInt(time),
                coordinates[0],
                coordinates[1],
                draft,
                city
        );

        odbs.update(o);

        return "/02-offer/offer.xhtml?faces-redirect=true&id=" + o.getId();
    }

    private void checkCreation(boolean draft) {
        if (draft) {
            // Titel leer
            if (title.isEmpty()) {
                creationError = true;
                errorMsgs.put("title", "Es muss ein Titel eingegeben werden.");
            }
            return;
        }
        // Aufgaben leer
        if (tasks.isEmpty()) {
            creationError = true;
            errorMsgs.put("tasks", "Es müssen Aufgaben angegeben werden.");
        }
        // Qualifikationen leer
        if (qualifications.isEmpty()) {
            creationError = true;
            errorMsgs.put("qualifications", "Es müssen Qualifikationen angegeben werden.");
        }
        // Nur wichtig, falls noch keine hat
        if (!hasCoordinates) {
            // Keine Adresse
            if (street.isEmpty() || postalCode.isEmpty() || city.isEmpty()) {
                creationError = true;
                errorMsgs.put("address", "Es müssen alle Bestandteile der Adresse eingegeben werden.");
            }
        }
        // Keine Filter ausgewählt
        if (field.equals("-1") || level.equals("-1") || time.equals("-1")) {
            creationError = true;
            errorMsgs.put("filter", "Es muss für alle Filter ein Wert eingegeben werden.");
        }
    }

    public String getErrorMsg(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
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

    private static int toInt(String str) {
        if (str == null) {
            return -1;
        }

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            System.out.println("Could not parse string: " + str + " to integer: " + e.getMessage());
            return -1;
        }
    }

    public String checkAdminRights() {
        if (cu.getCurrentUser() instanceof Employee){
            return "";
        } else {
            return "/index.xhtml"; // *Main page* for normal user OR maybe login page?
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isCreationError() {
        return creationError;
    }

    public void setCreationError(boolean creationError) {
        this.creationError = creationError;
    }
}
