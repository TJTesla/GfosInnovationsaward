package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Offer {
    private int id;
    private String title;
    private String description;
    private int provider;
    private int tag;
    private int category;

    public Offer(int id, String title, String description, int provider, int tag, int category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.provider = provider;
        this.tag = tag;
        this.category = category;
    }

    public Offer() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.provider = 0;
        this.tag = 0;
        this.category = 0;
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

    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
