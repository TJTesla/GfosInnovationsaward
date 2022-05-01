package gfos.pojos;

// Struktur zum gebündelten Speichern der Daten für einen Super User
// Beinhaltet nur Variablen; 2 Konstruktoren (Einer ohne Parameter, einer mit komplettem); Getter und Setter für alle Variablen

public class SuperUser extends User {
    public SuperUser() {
        super("sudo", "", "");
    }
}
