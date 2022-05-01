package gfos.pojos;

// Struktur zum gebündelten Speichern der Daten für einen Lebenslauf
// Beinhaltet nur Variablen; 2 Konstruktoren (Einer ohne Parameter, einer mit komplettem); Getter und Setter für alle Variablen

public class Resume {
    private int id;
    private String path;
    private String name;

    public Resume(int id, String path, String name) {
        this.id = id;
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
