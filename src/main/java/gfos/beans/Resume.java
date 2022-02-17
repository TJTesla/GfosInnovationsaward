package gfos.beans;

public class Resume {
    private int id;
    private String path;
    private String name;

    public Resume(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public Resume() {
        this.id = 0;
        this.path = "";
        this.name = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
