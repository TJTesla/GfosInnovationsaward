package gfos.database;

import gfos.pojos.User;

// Eigentlich unnöiges Interface für die Datenbank-Klassen für Angestellte und Bewerber

public interface UserDatabaseInterface {
    public User getById(int id);
}
