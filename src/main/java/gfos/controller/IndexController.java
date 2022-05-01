package gfos.controller;

import gfos.pojos.FilterObject;
import gfos.pojos.Employee;
import gfos.database.MiscellaneousDatabaseService;
import gfos.pojos.Applicant;
import gfos.pojos.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.beans.CurrentUser;
import gfos.pojos.Pair;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// Controller für die Index Seite
// Verantwortlich für das Anzeigen von Angeboten
// und Weiterleiten der Filter an die Datenbankverantwortlichen Klassen

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

    // Variablen zum Speichern der Filter-Angaben
    private HashMap<Integer, Boolean> field = new HashMap<>();
    private HashMap<Integer, Boolean> level = new HashMap<>();
    private HashMap<Integer, Boolean> time = new HashMap<>();
    private String appliedChoose;
    private boolean favorites;

    private String maxDistance;
    private HashSet<Integer> favoritesSet = new HashSet<>();

    private Applicant optionalApplicant() {
        if (cu.getCurrentUser() instanceof Applicant) {
            return (Applicant)(cu.getCurrentUser());
        }
        return null;
    }

    // Hauptmethode
    // Aufgerufen bei jeder Seitenladung
    public ArrayList<Offer> getAllOffers() {
        // Falls angemeldet als Angestellter einfach alle Angebote (auch Entwürfe) anzeigen
        if (cu.getCurrentUser() instanceof Employee) {
            return odbs.fetchAll();
        }

        // Zum Markieren der Favoriten, falls angemeldet als Bewerber
        // Speichern der ID der favorisierten Angebote in HashSet -> Schnelles Abfragen möglich
        if (cu.getCurrentUser() instanceof Applicant) {
            favoritesSet = adbs.getFavorites( ((Applicant)cu.getCurrentUser()).getId() );
        }

        return odbs.getAllFinalOffers(
                new FilterObject(
                        createFilterArray(field),
                        createFilterArray(level),
                        createFilterArray(time),
                        toInt(maxDistance),
                        toApplyFilter(appliedChoose),
                        favorites
                ),
                optionalApplicant()
        );
    }

    // Für andere HTML-Seite -> Anzeigen der Entwürfe (Angebote)
    public ArrayList<Offer> getAllOfferDrafts() {
        return odbs.getAllDrafts(
                new FilterObject(
                        createFilterArray(field),
                        createFilterArray(level),
                        createFilterArray(time),
                        toInt(maxDistance),
                        toApplyFilter(appliedChoose),
                        favorites
                ),
                null
        );
    }

    // Verschiedene Integer Werte für Boolean Werte für Auswahl ob bspw. nur Angebote für die schon beworben wurde
    // Umwandlung nötig
    private Boolean toApplyFilter(String appliedChoose) {
        if (appliedChoose == null) {
            return null;
        }

        switch (appliedChoose) {
            case "-1":
                return null;
            case "1":
                return true;
            case "2":
                return false;
        }
        return null;
    }

    public String delete(Offer o) {
        odbs.delete(o);
        return "";
    }

    // Wegen JSF -> Speichern der Filter von Berufsfeld, Einstiegslevel etc. in HashMap
    // Umwandlung zu ArrayList nötig (Nur Speichern der id benötigt)
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
        if (str == null || str.equals("-1") || str.isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            System.out.println("Cannot change string: " + str + " to Integer: " + nfe.getMessage());
            return -1;
        }
    }

    public boolean isDraft(Offer o) {
        return o.getDraft();
    }

    // Einfacheres Abfragen durch HashSet
    public boolean isFavorite(int id) {
        return favoritesSet.contains(id);
    }

    // Aufgerufene Methode
    public void editFavorite(int id) {
        if (isFavorite(id)) {
            removeFromFavorites(id);
        } else {
            setAsFavorite(id);
        }
    }

    // Private Methode zum Hinzufügen zu Favoriten
    private void setAsFavorite(int id) {
        favoritesSet.add(id);
        adbs.addToFavorites( ((Applicant)cu.getCurrentUser()).getId(), id);
    }

    // Private Methode zum Entfernen aus Favoriten
    private void removeFromFavorites(int id) {
        favoritesSet.remove(id);
        adbs.removeFromFavorites( ((Applicant)cu.getCurrentUser()).getId(), id);
    }

    // Wahrscheinlich nicht benutzt, aber aus Sicherheit noch nicht gelöscht
    public String offerDetailPage(int offerId) {
        return "/02-offer/offer.xhtml?id=" + offerId;
    }

    // Wahrscheinlich nicht benutzt, aber aus Sicherheit noch nicht gelöscht
    public String checkLogIn() {
        if (cu.getCurrentUser() == null) {
            return "/00-loginRegistration/login.xhtml";
        } else {
            return "";
        }
    }

    public String isEmployee() {
        if(cu.getCurrentUser() instanceof Employee) {
            return "";
        }
        return "/00-loginRegistration/login.xhtml";
    }

    // ID des Berufsfeldes in einen String umwandeln
    public String fieldString(int fieldId) {
        return odbs.getField(fieldId);
    }

    public String levelString(int levelId) {
        return odbs.getLevel(levelId);
    }

    public String timeString(int timeId) {
        return odbs.getTime(timeId);
    }

    public boolean loggedInAsApplicant() {
        return cu.getCurrentUser() instanceof Applicant;
    }

    public boolean loggedInAsEmployee() {
        return cu.getCurrentUser() instanceof Employee;
    }

    public boolean notLoggedIn() {
        return cu.getCurrentUser() == null;
    }

    public String reload() {
        return "";
    }

    // Getter und Setter //

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

    public String getAppliedChoose() {
        return appliedChoose;
    }

    public void setAppliedChoose(String appliedChoose) {
        this.appliedChoose = appliedChoose;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }
}
