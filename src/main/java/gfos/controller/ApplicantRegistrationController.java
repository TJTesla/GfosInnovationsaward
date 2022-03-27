package gfos.controller;

import gfos.Regexes;
import gfos.beans.Applicant;
import gfos.beans.ResourceIO;
import gfos.database.ApplicantDatabaseService;
import gfos.database.MiscellaneousDatabaseService;
import gfos.exceptions.UploadException;
import gfos.longerBeans.CurrentUser;
import gfos.longerBeans.GeoCalculator;
import gfos.Pair;
import org.primefaces.model.file.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.time.LocalDate;

@Named
@ViewScoped
public class ApplicantRegistrationController implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    MiscellaneousDatabaseService mdbs;
    @Inject
    CurrentUser cu;

    private String salutation;
    private String[] title;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String emailRepeat;
    private String password;
    private String passwordRepeat;
    private UploadedFile pbFile;
    private String street, zip, city;
    private String birthdate;

    private boolean registerError = false;
    private HashMap<String, String> errorMsgs = new HashMap<>();

    private boolean showRegistration = false;

    public String register() {
        checkRegistration();
        if (registerError) {
            return "";
        }

        String address = street + " " + zip + " " + city;
        double[] coordinates = GeoCalculator.getCoordinates(address);

        Applicant a = new Applicant(
                -1,
                username,
                password,
                "",
                firstname,
                lastname,
                email,
                Integer.parseInt(salutation),
                arrToList(title),
                "",
                coordinates[0],  // Latitude
                coordinates[1],  // Longitude
                birthdate
        );

        if (pbFile != null) {
            savePb(a);
        }

        adbs.createOne(a);

        cu.setCurrentUser(a);

        return "/index.xhtml?faces-redirect=true";
    }

    private void savePb(Applicant a) {
        try {
            String pbDir = ResourceIO.uploadPb(pbFile, a);
            a.setPb(pbDir);
        } catch (IOException ioException) {
            System.out.println("There was an internal error while saving pb file: "+ ioException.getMessage());
        } catch (UploadException uploadException) {
            System.out.println("There was an error while saving pb file: " + uploadException.getMessage());
        }
    }

    private boolean checkDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e){
            return false;
        }
        return true;
    }

    private boolean checkAge(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthdate = LocalDate.parse(date, formatter);
        LocalDate now = LocalDate.now();
        LocalDate max = now.minus(67, ChronoUnit.YEARS);
        LocalDate min = now.minus(16, ChronoUnit.YEARS);
        return (birthdate.isAfter(max) && birthdate.isBefore(min)) || birthdate.isEqual(min);
    }


    @Inject
    LoginController login;

    public void switchView() {
        showRegistration = !showRegistration;
        login.setLoginError(false);
    }

    public String getErrorMessage(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
    }

    private ArrayList<String> arrToList(String[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    private void checkRegistration() {
        // Keine Anrede
        if (salutation.equals("-1")) {
            registerError = true;
            errorMsgs.put("salutation", "Es muss eine Anrede ausgewählt werden.");
        }
        // Kein Vorname
        if (firstname.isEmpty()) {
            registerError = true;
            errorMsgs.put("firstname", "Vorname ist erforderlich.");
        }
        // Kein Nachname
        if (lastname.isEmpty()) {
            registerError = true;
            errorMsgs.put("lastname", "Nachname ist erforderlich.");
        }
        // Kein Geburtstag
        if (birthdate.isEmpty()) {
            registerError = true;
            errorMsgs.put("birthdate", "Es muss ein Geburtstag angeben werden.");
        }
        // Geburtstag nicht im richtigen Format
        if(!checkDateFormat(birthdate)) {
            registerError = false;
            errorMsgs.put("birthdate", "Das angegebene Datum ist nicht korrekt.");
        } else if (!checkAge(birthdate)) {
            registerError = false;
            errorMsgs.put("birthdate", "Das angegebene Datum ist zeitlich nicht korrekt.");
        }
        // Kein benutzername
        if (username.isEmpty()) {
            registerError = true;
            errorMsgs.put("username", "Es muss ein Benutzername angegeben werden.");
        }
        // Benutzername schon belegt
        if (adbs.nameExists(username) || username.equals("root")) {
            registerError = true;
            errorMsgs.put("username", "Dieser Benutzername wird bereits verwendet.");
        }
        // Keine E-Mail
        if (email.isEmpty()) {
            registerError = true;
            errorMsgs.put("email", "Es muss eine E-Mail Adresse angegeben werden.");
        }
        // E-Mail wird schon benutzt
        if (adbs.emailExists(email)) {
            registerError = true;
            errorMsgs.put("email", "Diese E-Mail Adresse wird bereits verwendet.");
        }
        // E-Mail nicht im richtigen Format
        if (!Pattern.compile(Regexes.email).matcher(email).find() && !email.isEmpty()) {
            registerError = true;
            errorMsgs.put("email", "Die angegebene E-Mail ist keine korrekte E-Mail Adresse.");
        }
        // E-Mails sind nicht gleich
        if (!email.equals(emailRepeat)) {
            registerError = true;
            errorMsgs.put("emailRepeat", "E-Mail Adressen stimmen nicht überein.");
        }
        // Kein Passwort
        if (password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Es muss ein Passwort angegeben werden");
        }
        // Passwort regex
        if (!Pattern.compile(Regexes.password).matcher(password).find() && !password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Das Passwort muss mindestens einen Großbuchstaben, einen Kleinbuchstaben, ein Sonderzeichen und eine Zahl beinhalten.");
        }
        // Passwörter sind nicht gleich
        if (!password.equals(passwordRepeat)) {
            registerError = true;
            errorMsgs.put("passwordRepeat", "Passwörter stimmen nicht überein.");
        }
    }

    public ArrayList<String> getAllTitles() {
        ArrayList<String> arr = new ArrayList<>();

        ArrayList<Pair<Integer, String>> entries = mdbs.getAllTitles();
        for (Pair<Integer, String> p : entries) {
            arr.add(p.second());
        }

        return arr;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() { return birthdate; }

    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailRepeat() {
        return emailRepeat;
    }

    public void setEmailRepeat(String emailRepeat) {
        this.emailRepeat = emailRepeat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public UploadedFile getPbFile() {
        return pbFile;
    }

    public void setPbFile(UploadedFile pbFile) {
        this.pbFile = pbFile;
    }

    public boolean isRegisterError() {
        return registerError;
    }

    public void setRegisterError(boolean registerError) {
        this.registerError = registerError;
    }

    public boolean isShowRegistration() {
        return showRegistration;
    }

    public void setShowRegistration(boolean showRegistration) {
        this.showRegistration = showRegistration;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
