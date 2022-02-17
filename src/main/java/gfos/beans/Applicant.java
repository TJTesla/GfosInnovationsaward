package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;


//TODO: Test whether classes in gfos.beans package need to be CDI beans or POJOs would be sufficient
//@Named
//@RequestScoped
public class Applicant extends User {
    private String firstname;
    private String lastname;
    private int gender;
    private ArrayList<String> titles;

    public Applicant(int id, String username, String password, String firstname, String lastname, String email, int gender, ArrayList<String> titles, String pb) {
        super(id, username, password, email, pb);
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.titles = titles;
    }

    public Applicant() {
        super(0, "", "", "", "");
        this.firstname = "";
        this.lastname = "";
        this.gender = 0;
        this.titles = new ArrayList<>();
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }
}

