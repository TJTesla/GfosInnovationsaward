package gfos.controller;

import gfos.beans.Employee;
import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;
import gfos.longerBeans.GeoCalculator;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;

@Named
@ViewScoped
public class CreateOfferController implements Serializable {
    private String title;
    private String tasks, qualifications, extras;
    private String field;
    private String level;
    private String time;

    private String street;
    private String postalCode;
    private String city;

    @Inject
    CurrentUser cu;
    @Inject
    OfferDatabaseService odbs;

    private boolean creationError = false;
    private HashMap<String, String> errorMsgs = new HashMap<>();

    public String save(boolean draft) {
        checkCreation();
        if (creationError) {
            return "";
        }

        String location = street + " " + postalCode + " " + city;
        double[] coordinates = GeoCalculator.getCoordinates(location);

        odbs.createOne(new Offer(
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

        return "/tjtesla-tests/companyIndex.xhtml?faces-redirect=true";
    }

    private void checkCreation() {
        // Titel leer
        if (title.isEmpty()) {
            creationError = true;
            errorMsgs.put("title", "Es muss ein Titel eingegeben werden");
        }
        // Aufgaben leer
        if (tasks.isEmpty()) {
            creationError = true;
            errorMsgs.put("tasks", "Es müssen Aufgaben angegeben werden");
        }
        // Qualifikationen leer
        if (qualifications.isEmpty()) {
            creationError = true;
            errorMsgs.put("qualifications", "Es müssen Qualifikationen angegeben werden");
        }
        // Keine Adresse
        if (street.isEmpty() || postalCode.isEmpty() || city.isEmpty()) {
            creationError = true;
            errorMsgs.put("address", "Es müssen alle Bestandteile der Adresse eingegeben werden");
        }
        // Keine Filter ausgewählt
        if (field.equals("-1") || level.equals("-1") || time.equals("-1")) {
            creationError = true;
            errorMsgs.put("filter", "Es muss für alle Filter ein Wert eingegeben werden");
        }
    }

    public String getErrorMsg(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
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
