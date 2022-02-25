package gfos.controller;

import gfos.Regexes;
import gfos.beans.Applicant;
import gfos.beans.ResourceIO;
import gfos.database.ApplicantDatabaseService;
import gfos.database.MiscellaneousDatabaseService;
import gfos.exceptions.UploadException;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.file.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

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

    private boolean registerError = false;
    private HashMap<String, String> errorMsgs = new HashMap<>();

    private boolean showRegistration = false;

    public String register() {
        checkRegistration();
        if (registerError) {
            return "";
        }

        Applicant a = new Applicant(-1, username, password, firstname, lastname, email, Integer.parseInt(salutation), arrToList(title), "");

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
            errorMsgs.put("salutation", "Es muss eine Anrede ausgewählt werden");
        }
        // Kein Vorname
        if (firstname.isEmpty()) {
            registerError = true;
            errorMsgs.put("firstname", "Es muss ein Vorname angegeben werden");
        }
        // Kein Nachname
        if (lastname.isEmpty()) {
            registerError = true;
            errorMsgs.put("lastname", "Es muss ein Nachname angegeben werden");
        }
        // Kein benutzername
        if (username.isEmpty()) {
            registerError = true;
            errorMsgs.put("username", "Es muss ein Benutzername angegeben werden");
        }
        // Benutzername schon belegt
        if (adbs.nameExists(username)) {
            registerError = true;
            errorMsgs.put("username", "Dieser Benutzername wird bereits verwendet");
        }
        // Keine E-Mail
        if (email.isEmpty()) {
            registerError = true;
            errorMsgs.put("email", "Es muss eine E-Mail Addresse angegeben werden");
        }
        // E-mail wird schon benutzt
        if (adbs.emailExists(email)) {
            registerError = true;
            errorMsgs.put("email", "Diese E-Mail Adresse wird bereits verwendet");
        }
        // E-Mail nicht im richtigen Format
        if (!Pattern.compile(Regexes.email).matcher(email).find() && !email.isEmpty()) {
            registerError = true;
            errorMsgs.put("email", "Die angegebene E-Mail ist keine korrekte E-Mail Addresse");
        }
        // E-Mails sind nicht gleich
        if (!email.equals(emailRepeat)) {
            registerError = true;
            errorMsgs.put("emailRepeat", "Beide angegebenen E-Mails müssen gleich sein");
        }
        // Kein Passwort
        if (password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Es muss ein Passwort angegeben werden");
        }
        // Passwort regex
        if (!Pattern.compile(Regexes.password).matcher(password).find() && !email.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Das Passwort muss mindestens einen großen, kleinen Buchstaben, ein Sonderzeichen und eine Zahl beinhalten");
        }
        // Passwörter sind nicht gleich
        if (!password.equals(passwordRepeat)) {
            registerError = true;
            errorMsgs.put("passwordRepeat", "Beide angegebenen Passwörter müssen gleich sein");
        }
    }

    public ArrayList<String> getAllTitles() {
        return mdbs.getAllTitles();
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
}
