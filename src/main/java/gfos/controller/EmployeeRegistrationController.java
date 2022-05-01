package gfos.controller;

import gfos.pojos.Regexes;
import gfos.pojos.Employee;
import gfos.database.ApplicantDatabaseService;
import gfos.database.EmployeeDatabaseService;
import gfos.beans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

// Controller zum Steuern der Registrierung eines Employees

@Named
@ViewScoped
public class EmployeeRegistrationController implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    EmployeeDatabaseService edbs;
    @Inject
    CurrentUser cu;

    private String name;
    private String password;
    private String passwordRepeat;
    private String key;

    private boolean registerError = false;
    private HashMap<String, String> errorMsgs = new HashMap<>();

    public String register() {
        errorMsgs.clear();
        checkRegistration();
        if (registerError) {
            return "";
        }

        Employee e = new Employee(name, password, "", true);

        edbs.registerEmployee(e);

        cu.setCurrentUser(e);

        return "/index.xhtml?faces-redirect=true";
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
        if (!edbs.employeeCreated(name, key) || name.equals("root")) {
            registerError = true;
            errorMsgs.put("name", "Es wurde kein noch nicht aktivierter eingetragener Angestellter für den Namen \"" + name + "\" und dem angegebenen Passwort gefunden.");
        }
        // Passwort leer
        if (password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Es muss ein Passwort angegeben werden.");
        }
        // Passwort regex
        if (!Pattern.compile(Regexes.password).matcher(password).find() && !password.isEmpty()) {
            registerError = true;
            errorMsgs.put("password", "Das Passwort muss mindestens einen Großbuchstaben, einen Kleinbuchstaben, ein Sonderzeichen und eine Zahl beinhalten.");
        }
        // Passwort wiederholung
        if (!password.equals(passwordRepeat)) {
            registerError = true;
            errorMsgs.put("passwordRepeat", "Passwörter stimmen nicht überein.");
        }
    }

    // Getter und Setter //

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRegisterError() {
        return registerError;
    }

    public void setRegisterError(boolean registerError) {
        this.registerError = registerError;
    }
}
