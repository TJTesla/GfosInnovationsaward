package gfos.database;

import gfos.pojos.Env;

import javax.annotation.PreDestroy;
import java.sql.*;

// Abstrakte Klasse als Super-Klasse aller Datenbank-Klassen
// Stellt drei Attribute bereit:
// Connection, PreparedStatement und ResultSet

public abstract class DatabaseService {
    protected Connection con;
    protected PreparedStatement stmt;
    protected ResultSet rs;

    // Erstellt verbindung mit Datenbank
    public DatabaseService() throws SQLException {
        try {
            //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ApplicationManagement?useSSL=false&allowPublicKeyRetrieval=true", Env.user, Env.password);
            con = DriverManager.getConnection(
                    "jdbc:mysql://bewerbermanagement.educationhost.cloud:3306/fdrvpqxv_applicationmanagement?useSSL=false&allowPublicKeyRetrieval=true",
                    Env.remoteUser,
                    Env.remotePassword
            );
            stmt = null;
            rs = null;
        } catch (SQLException sqlException) {
            System.out.println("SQL Error:" + sqlException.getMessage());
            throw sqlException;
        }
    }

    public Object getById(int id) {
        return null;
    }

    // Schließt Verbindung mit Datenbank bei Zerstörung des Objekts
    // Alle Datenbank-Klassen sind ApplicationScoped Beans -> Zerstörung erst bei Beendigung des ganzen Programms
    @PreDestroy
    public void deconstruct() {
        try {
            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException sqlException) {
            System.out.println("Could not destroy UserDatabaseService: " + sqlException.getMessage());
        }
    }
}
