package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Company {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phoneno;
    private String website;
    private String description;

    public Company(int id, String name, String password, String email, String phoneno, String website, String description) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneno = phoneno;
        this.website = website;
        this.description = description;
    }

    public Company() {
        this.id = 0;
        this.name = "";
        this.password = "";
        this.email = "";
        this.phoneno = "";
        this.website = "";
        this.description = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
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
}
