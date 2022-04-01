package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

//@Named
//@RequestScoped
public class Offer {
    private int id;
    private String title;
    private String tasks;
    private String qualifications;
    private String extras;
    private int field, level, time;
    private double lat;
    private double lon;
    private boolean draft;
    private String city;

    public Offer(int id, String title, String tasks, String qualifications, String extras, int field, int level, int time, double lat, double lon, boolean draft, String city) {
        this.id = id;
        this.title = title;
        this.tasks = tasks;
        this.qualifications = qualifications;
        this.extras = extras;
        this.field = field;
        this.level = level;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.draft = draft;
        this.city = city;
    }

    public Offer() {
        this.id = 0;
        this.title = "";
        this.tasks = "";
        this.qualifications = "";
        this.extras = "";
        this.field = 0;
        this.level = 0;
        this.time = 0;
        this.lat = 0.0;
        this.lon = 0.0;
        this.draft = true;
        this.city = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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

    public boolean getDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
