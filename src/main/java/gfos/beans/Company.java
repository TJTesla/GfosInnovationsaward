package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

//@Named
//@RequestScoped
public class Company extends User {
    private String phoneno;
    private String website;
    private String description;

    public Company(int id, String name, String password, String email, String phoneno, String website, String description, String pb) {
        super(id, name, password, email, pb);
        this.phoneno = phoneno;
        this.website = website;
        this.description = description;
    }

    public Company() {
        super(0, "", "", "", "");
        this.phoneno = "";
        this.website = "";
        this.description = "";
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
