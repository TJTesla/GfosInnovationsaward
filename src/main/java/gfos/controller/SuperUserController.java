package gfos.controller;

import gfos.beans.Employee;
import gfos.database.ApplicantDatabaseService;
import gfos.database.EmployeeDatabaseService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;

@Named
@ViewScoped
public class SuperUserController implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    EmployeeDatabaseService edbs;

    private String name;
    private String key;

    boolean registrationError = false;
    HashMap<String, String> errorMsgs = new HashMap<>();

    public String createEmployee() {
        errorMsgs.clear();
        registrationError = false;
        check();
        if (registrationError) {
            return "";
        }

        edbs.createOne(new Employee(
                name,
                key,
                "",
                false
        ));
        return "";
    }

    private void check() {
        if (name.isEmpty()) {
            registrationError = true;
            errorMsgs.put("name", "Ein Name ist erforderlich");
        }

        if (adbs.nameExists(name)) {
            registrationError = true;
            errorMsgs.put("name", "Dieser Name wird bereits verwendet");
        }

        if (key.isEmpty()) {
            registrationError = true;
            errorMsgs.put("key", "Es muss ein Schlüssel eingegeben werden");
        }
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
}
