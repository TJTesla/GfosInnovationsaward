package gfos.pojos;

import java.util.ArrayList;

// Struktur zum gebündelten Speichern der Daten für einen Bewerber
// Beinhaltet nur Variablen; 2 Konstruktoren (Einer ohne Parameter, einer mit komplettem); Getter und Setter für alle Variablen

public class Applicant extends User {
    private int id;
    private String email;
    private String firstname;
    private String lastname;
    private int gender;
    private ArrayList<String> titles;
    private String pb;
    private double lat, lon;
    private String bday;

    public Applicant(int id, String username, String password, String salt, String firstname, String lastname, String email, int gender, ArrayList<String> titles, String pb, double lat, double lon, String bday) {
        super(username, password, salt);
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.titles = titles;
        this.pb = pb;
        this.lat = lat;
        this.lon = lon;
        this.bday = bday;
    }

    public Applicant() {
        super( "", "", "");
        this.id = 0;
        this.email = "";
        this.firstname = "";
        this.lastname = "";
        this.gender = 0;
        this.titles = new ArrayList<>();
        this.pb = "";
        this.lat = 0.0;
        this.lon = 0.0;
        this.bday = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }
}

