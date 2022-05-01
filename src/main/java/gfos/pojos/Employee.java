package gfos.pojos;

// Struktur zum gebündelten Speichern der Daten für einen Angestellten
// Beinhaltet nur Variablen; 2 Konstruktoren (Einer ohne Parameter, einer mit komplettem); Getter und Setter für alle Variablen

public class Employee extends User {
    private boolean registered;

    public Employee() {
        super("", "", "");
        this.name = "";
        this.password = "";
        this.registered = false;
    }

    public Employee(String name, String password, String salt, boolean registered) {
        super(name, password, salt);
        this.registered = registered;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
