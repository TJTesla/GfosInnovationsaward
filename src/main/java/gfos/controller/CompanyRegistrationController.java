package gfos.controller;

import gfos.Regexes;
import gfos.beans.Company;
import gfos.beans.ResourceIO;
import gfos.database.ApplicantDatabaseService;
import gfos.database.CompanyDatabaseService;
import gfos.exceptions.UploadException;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.file.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

@Named
@ViewScoped
public class CompanyRegistrationController implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CompanyDatabaseService cdbs;
    @Inject
    CurrentUser cu;

    private String name;
    private String password;
    private String passwordRepeat;
    private String website;
    private String description;
    private UploadedFile logoFile;
    private String email;
    private String emailRepeat;
    private String phoneno;

    private boolean registerError = false;
    private HashMap<String, String> errorMsgs = new HashMap<>();

    public String register() {
        checkRegistration();
        if (registerError) {
            return "";
        }

        Company c = new Company(-1, name, password, email, phoneno, website, description, "");

        if (logoFile != null) {
            saveLogo(c);
        }

        cdbs.createOne(c);

        cu.setCurrentUser(c);

        return "/index.xhtml?faces-redirect=true";
    }

    private void saveLogo(Company c) {
        try {
            String pbDir = ResourceIO.uploadPb(logoFile, c);
            c.setPb(pbDir);
        } catch (IOException ioException) {
            System.out.println("There was an internal error while saving pb file: "+ ioException.getMessage());
        } catch (UploadException uploadException) {
            System.out.println("There was an error while saving pb file: " + uploadException.getMessage());
        }
    }

    public String getErrorMessage(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
    }

    private void checkRegistration() {
        // Name leer
        if (name.isEmpty()) {
            registerError = true;
            errorMsgs.put("name", "Es muss ein Name angegeben werden");
        }
        // Name wird schon benutzt
        if (adbs.nameExists(name)) {
            registerError = true;
            errorMsgs.put("name", "Der Name \"" + name + "\" existiert bereits.");
        }
        // Passwort leer
        if (password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Es muss ein Passwort angegeben werden");
        }
        // Passwort regex
        if (!Pattern.compile(Regexes.password).matcher(password).find() && !password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Das Passwort muss mindestens einen Großbuchstaben, einen Kleinbuchstaben, ein Sonderzeichen und eine Zahl beinhalten.");
        }
        // Passwort wiederholung
        if (!password.equals(passwordRepeat)) {
            registerError = true;
            errorMsgs.put("password", "Passwörter stimmen nicht überein.");
        }
        // Webseite leer
        if (website.isEmpty()) {
            registerError = true;
            errorMsgs.put("website", "Es muss eine Webseite angegeben werden.");
        }
        // Webseite regex
        if (!Pattern.compile(Regexes.website).matcher(website).find() && !website.isEmpty()) {
            registerError = true;
            errorMsgs.put("website", "Die angegebene Webseite ist keine korrekte URL.");
        }
        // Email leer
        if (email.isEmpty()) {
            registerError = true;
            errorMsgs.put("email", "Es muss eine E-Mail Adresse angegeben werden.");
        }
        // Email regex
        if (!Pattern.compile(Regexes.email).matcher(email).find() && !email.isEmpty()) {
            registerError = true;
            errorMsgs.put("email", "Die angegebene E-Mail ist keine korrekte E-Mail Adresse.");
        }
        // Email wird schon benutzt
        if (adbs.emailExists(email)) {
            registerError = true;
            errorMsgs.put("email", "Die E-Mail wird bereits verwendet.");
        }
        // Email wiederholung
        if (!email.equals(emailRepeat)) {
            registerError = true;
            errorMsgs.put("emailRepeat", "E-Mail Adressen stimmen nicht überein.");
        }
        // Telefonnr regex
        if (!Pattern.compile(Regexes.phoneno).matcher(phoneno).find() && !phoneno.isEmpty()) {
            registerError = true;
            errorMsgs.put("phoneno", "Die angegebene Telefonnummer ist keine korrekte Nummer.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UploadedFile getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(UploadedFile logoFile) {
        this.logoFile = logoFile;
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

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public boolean isRegisterError() {
        return registerError;
    }

    public void setRegisterError(boolean registerError) {
        this.registerError = registerError;
    }
}
