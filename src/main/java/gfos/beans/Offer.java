package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

//@Named
//@RequestScoped
public class Offer {
    private int id;
    private String title;
    private String description;
    private int field, level, time;
    private double lat;
    private double lon;
    private boolean draft;
    private String city;

    public Offer(int id, String title, String description, int field, int level, int time, double lat, double lon, boolean draft, String city) {
        this.id = id;
        this.title = title;
        this.description = description;
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
        this.description = "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
