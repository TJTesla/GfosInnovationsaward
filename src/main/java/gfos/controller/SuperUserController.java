package gfos.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class SuperUserController {
    private String name;
    private String key;

    public String createEmployee() {
        // Error handling
        // Create employee w/ edbs
        return "";
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
