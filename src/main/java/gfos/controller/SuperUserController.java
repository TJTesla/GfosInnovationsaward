package gfos.controller;

import gfos.beans.Employee;
import gfos.beans.PasswordManager;
import gfos.beans.SuperUser;
import gfos.database.ApplicantDatabaseService;
import gfos.database.EmployeeDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

@Named
@ViewScoped
public class SuperUserController implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    EmployeeDatabaseService edbs;
    @Inject
    CurrentUser cu;

    private String name;
    private String key;

    private boolean renderKey = false;

    boolean registrationError = false;

    HashMap<String, String> errorMsgs = new HashMap<>();

    public String createEmployee() {
        renderKey = false;
        errorMsgs.clear();
        registrationError = false;
        check();
        if (registrationError) {
            return "";
        }

        key = PasswordManager.generateKey();

        edbs.createOne(new Employee(
                name,
                key,
                "",
                false
        ));
        renderKey = true;
        return "";
    }

    public String getLastKey() {
        return this.key;
    }

    public String getErrorMessage(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
    }

    private void check() {
        if (name.isEmpty()) {
            registrationError = true;
            errorMsgs.put("name", "Ein Name ist erforderlich");
        }

        if (adbs.nameExists(name) || name.equals("root")) {
            registrationError = true;
            errorMsgs.put("name", "Dieser Name wird bereits verwendet");
        }
    }

    public void delete(Employee e) {
        edbs.deleteEmployee(e);
    }

    public String activationStatus(Employee e) {
        if (e.isRegistered()) {
            return "Aktiviert";
        } else {
            return "Nicht aktiviert";
        }
    }

    public String isSuperUser() {
        if (cu.getCurrentUser() instanceof SuperUser) {
            return "";
        }

        return "/00-loginRegistration/login.xhtml";
    }

    public ArrayList<Employee> getAllEmployees() {
        return edbs.getAllEmployees();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRenderKey() {
        return renderKey;
    }

    public void setRenderKey(boolean renderKey) {
        this.renderKey = renderKey;
    }

}
