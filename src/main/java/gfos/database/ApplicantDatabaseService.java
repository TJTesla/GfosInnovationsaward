package gfos.database;

import gfos.pojos.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Klasse für Datenbank Befehle mit Zusammenhang zu Bewerbern

@Named
@ApplicationScoped
public class ApplicantDatabaseService extends DatabaseService implements UserDatabaseInterface {

    public ApplicantDatabaseService() throws SQLException {
        super();
    }

    // Speichern eines Bewerbers in der Datenbank
    public int createOne(Applicant a) {
        try {
            // Salt and hash password
            a.setSalt(PasswordManager.generateSalt());
            a.setPassword(PasswordManager.getHash(a.getPassword(), a.getSalt()));

            stmt = con.prepareStatement("" +
                    "INSERT INTO applicant(id, username, password, salt, firstname, lastname, email, gender, pb, lat, lon, bday) VALUES " +
                    "(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getPassword());
            stmt.setString(3, a.getSalt());
            stmt.setString(4, a.getFirstname());
            stmt.setString(5, a.getLastname());
            stmt.setString(6, a.getEmail());
            stmt.setInt(7, a.getGender());
            stmt.setString(8, a.getPb());
            stmt.setDouble(9, a.getLat());
            stmt.setDouble(10, a.getLon());
            stmt.setString(11, a.getBday());
            int affectedRows = stmt.executeUpdate();

            // Gibt Wert für die grade erzugte ID des neu gespeicherten Applicants zurück
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();

            a.setId(rs.getInt("LAST_INSERT_ID()"));
            if (a.getTitles().size() > 0) {
                // Titel müssen gesondert gespeichert werden (da n:m Beziehung)
                affectedRows += insertTitles(a);
            }

            return a.getId();
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an applicant: " + sqlException.getMessage());
            return -1;
        }
    }

    private int insertTitles(Applicant applicant) throws SQLException {
        stmt = con.prepareStatement("SELECT * FROM title");
        rs = stmt.executeQuery();

        // Speichern aller Verfügbaren Titel in HashMap
        // Key -> Bezeichnung (bspw. Prof.)
        // Value -> Dazugehörige Id in Datenbank
        HashMap<String, Integer> possibleTitles = new HashMap<>();
        while (rs.next()) {
            possibleTitles.put(rs.getString("term"), rs.getInt("id"));
        }

        // Erstellen eines String für Datenbankabfrage
        // Beispielhaftes Ergebnis: Applicant.id=10
        // INSERT [...] VALUES (10, 2),(10,4)
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO titleRelation(applicantId, titleId) VALUES ");
        for (String title : applicant.getTitles()) {
            sqlBuilder.append("(").append(applicant.getId()).append(", ").append(possibleTitles.get(title)).append("),");
        }
        String sql = sqlBuilder.toString();
        // Löschen des letzten Kommas
        sql = sql.substring(0, sql.length()-1);

        stmt = con.prepareStatement(sql);

        return stmt.executeUpdate();
    }

    // Alle Bewerber zurückbekommen
    // Nicht benutzt, da keine Funktionalität -> Dennoch nicht gelöscht
    public ArrayList<Applicant> fetchAll() {
        ArrayList<Applicant> list = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Applicant temp = new Applicant(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getInt("gender"),
                        getTitles(rs.getInt("id")),
                        rs.getString("pb"),
                        rs.getDouble("lat"),
                        rs.getDouble("lon"),
                        rs.getString("bday")
                );

                list.add(temp);
            }
        } catch (SQLException sqlException) {
            System.out.println("There was an error while trying to fetch all Applicants");
        }

        return list;
    }

    // Applicant Objekt mit dazugehöriger ID finden
    public Applicant getById(int id) {
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant WHERE id=?;");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return parseApplicant();
        } catch (SQLException sqlException) {
            System.out.println("Could not get applicant with id " + id + ": " + sqlException.getMessage());
            return null;
        }
    }

    // Liste an Titeln von Applicant mit gegebener ID bekommen
    private ArrayList<String> getTitles(int userId) {
        // Benutzung während Auslesen von Werten des Attributs rs
        // Diese Methode würde dies überschreiben -> Zweites Objekt vom Typ ResultSet nötig
        ResultSet secondRs;
        ArrayList<String> titles = new ArrayList<>();
        try {
            stmt = con.prepareStatement("" +
                    "SELECT term " +
                    "FROM title JOIN titleRelation ON title.id = titleRelation.titleId " +
                    "WHERE titleRelation.applicantId=?;"
            );
            stmt.setInt(1, userId);
            secondRs = stmt.executeQuery();

            while (secondRs.next()) {
                titles.add(secondRs.getString("term"));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch the titles for user with ID " + userId + ": " + sqlException.getMessage());
        }
        return titles;
    }

    // Methode zum überprüfen von korrektem einloggen
    // Wenn kein Ergebnis -> null
    // Ansonsten Objekt von Subtyp von Klasse User zu dem Anmeldedaten passen
    public User loginAttempt(String user, String password) {
        try {
            Applicant a = (Applicant)findUser("applicant", user, password);
            if (a != null) {
                return a;
            }

            return (Employee)findUser("employee", user, password);
        } catch (SQLException sqlException) {
            System.out.println("Could not check login: " + sqlException.getMessage());
            return null;
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("Could not generate hash: " + exception.getMessage());
            return null;
        }
    }

    // Methode zum Ändern des Passworts
    public boolean changePwd(User u, String newPwd) {
        if (!(u instanceof Applicant || u instanceof Employee)) {
            return false;
        }
        String salt = PasswordManager.generateSalt();
        String hashPwd = PasswordManager.getHash(newPwd, salt);

        try {
            if (u instanceof Applicant) {
                stmt = con.prepareStatement("UPDATE applicant SET password=?, salt=? WHERE id=?");
            } else { // instanceof Employee
                stmt = con.prepareStatement("UPDATE employees SET password=?, salt=? WHERE name=?");
            }
            stmt.setString(1, hashPwd);
            stmt.setString(2, salt);
            if (u instanceof Applicant) {
                stmt.setInt(3, ((Applicant)u).getId());
            } else { // instanceof Employee
                stmt.setString(3, u.getName());
            }

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not change pwd of user " + u.getName() + ": "+ sqlException.getMessage());
            return false;
        }
    }

    // salt Wert für einen bestimmten Nutzer finden
    private String getSalt(String table, String name) throws SQLException {
        String activeQuery = "";
        if (table.equals("applicant")) {
            activeQuery = "SELECT salt FROM applicant WHERE username=?";
        } else if (table.equals("employee")) {
            activeQuery = "SELECT salt FROM employees WHERE name=?";
        }

        stmt = con.prepareStatement(activeQuery);
        stmt.setString(1, name);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        return rs.getString("salt");
    }

    // Private Methode zum Überprüfen ob korrekt angemeldet wurde
    // Einmal aufgerufen zum Finden eines Bewerbers, falls nichts gefunden wurde noch einmal für Angestellte
    private User findUser(String table, String name, String password) throws SQLException, NoSuchAlgorithmException {
        // Übergebens Passwort hashen (mit Salt aus Datenbank für Nutzernamen)
        String hashPwd = PasswordManager.getHash(password, getSalt(table, name));

        String activeQuery = "";
        if (table.equals("applicant")) {
            activeQuery = "SELECT * FROM applicant WHERE username=? AND password=?";
        } else if (table.equals("employee")) {
            activeQuery = "SELECT * FROM employees WHERE name=? AND password=? AND registered=TRUE";
        }

        stmt = con.prepareStatement(activeQuery);
        stmt.setString(1, name);
        stmt.setString(2, hashPwd);
        rs = stmt.executeQuery();

        if  (!rs.next()) {
            return null;
        }

        // Methoden zum erzeugen eines Objektes aus ResultSet Werten
        if (table.equals("applicant")) {
            return parseApplicant();
        } else if (table.equals("employee")) {
            return EmployeeDatabaseService.createEmployee(rs);
        }

        return null;
    }

    // Überprüfen ob angegebener Name schon in Datenbank als Name vorkommt
    public boolean nameExists(String name) {
        try {
            stmt = con.prepareStatement("SELECT username FROM applicant WHERE username=?;");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            stmt = con.prepareStatement("SELECT name FROM employees WHERE name=?;");
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Error while checking for name: " + name + ": " + sqlException.getMessage());
            return false;
        }
    }

    // Überprüft ob Angebot mit ID oId ein Favorit von Bewerber mit ID aId ist
    public boolean isFavorite(int aId, int oId) {
        try {
            stmt = con.prepareStatement("SELECT offerId FROM favorites WHERE applicantId=? AND offerId=?");
            stmt.setInt(1, aId);
            stmt.setInt(2, oId);

            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Could not check for favorite for offer " + oId + " and applicant " + aId + ": " + sqlException.getMessage());
            return false;
        }
    }

    // Rückgabe von HashSet mit ID von Favoriten Angeboten für übergebenen Bewerber
    public HashSet<Integer> getFavorites(int id) {
        HashSet<Integer> result = new HashSet<>();
        try {
            stmt = con.prepareStatement("SELECT offerId FROM favorites WHERE applicantId=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getInt("offerId"));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not collect favorites from user: " + id + ": " + sqlException.getMessage());
        }
        return result;
    }

    // Zu Favoriten hinzufügen
    public void addToFavorites(int userId, int offerId) {
        try {
            stmt = con.prepareStatement("INSERT INTO favorites(applicantId, offerId) VALUES (?, ?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, offerId);
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not add offer " + offerId + " to favorites of applicant " + userId + ": " + sqlException.getMessage());
        }
    }

    // Von Favoriten entfernen
    public void removeFromFavorites(int userId, int offerId) {
        try {
            stmt = con.prepareStatement("DELETE FROM favorites WHERE applicantId=? AND offerId=?");
            stmt.setInt(1, userId);
            stmt.setInt(2, offerId);
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not remove offer " + offerId + " from favorites of applicant " + userId + ": " + sqlException.getMessage());
        }
    }

    // Überprüfen ob angegebene E-Mail schon benutzt wird
    public boolean emailExists(String email) {
        try {
            stmt = con.prepareStatement("SELECT email FROM applicant WHERE email=?;");
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Error while checking for email: " + email + ": " + sqlException.getMessage());
            return false;
        }
    }

    public String getSalutation(int id) {
        switch (id) {
            case 1:
                return "Herr";
            case 2:
                return "Frau";
            case 9:
                return "Divers";
            case 0:
                return "k. A.";
            default:
                return null;
        }
    }

    // Rückgabe von Titeln in String in Liste von Bewerber
    public ArrayList<String> titles(int id) {
        ArrayList<String> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT term FROM title JOIN titleRelation ON title.id = titleRelation.titleId AND titleRelation.applicantId=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add( rs.getString("term") );
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch titles for user " + id + ": " + sqlException.getMessage());
        }
        return result;
    }

    // Aktualisieren der Werte von Bewerber
    public boolean update(Applicant a) {
        try {
            stmt = con.prepareStatement("UPDATE applicant SET username=?, firstname=?, lastname=?, email=?, pb=?, lat=?, lon=? WHERE id=?");
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getFirstname());
            stmt.setString(3, a.getLastname());
            stmt.setString(4, a.getEmail());
            stmt.setString(5, a.getPb());
            stmt.setDouble(6, a.getLat());
            stmt.setDouble(7, a.getLon());
            stmt.setInt(8, a.getId());

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not update applicant " + a.getId() + ": " + sqlException.getMessage());
            return false;
        }
    }

    // Löschen eines Bewerbers und allen dazugehörigen Daten
    public void delete(Applicant a) {
        try {
            // Alle Favoriten löschen
            stmt = con.prepareStatement("DELETE FROM favorites WHERE applicantId=?");
            stmt.setInt(1, a.getId());
            stmt.executeUpdate();

            // Applications from applicant + Resumes (also in files)
            deleteApplications(a.getId());

            // Titel löschen
            stmt = con.prepareStatement("DELETE FROM titleRelation WHERE applicantId=?");
            stmt.setInt(1, a.getId());
            stmt.executeUpdate();

            // Delete applicant
            stmt = con.prepareStatement("DELETE FROM applicant WHERE id=?");
            stmt.setInt(1, a.getId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not delete applicant " + a.getId() + ": " + sqlException.getMessage());
        }
    }

    // Hilfsmethode zum Löschen aller Bewerbungen eines Nutzers
    private void deleteApplications(int applicantId) throws SQLException {
        // Speichern aller IDs von Lebensläufen der Bewerbungen
        stmt = con.prepareStatement("SELECT resumeId FROM application WHERE userId=?");
        stmt.setInt(1, applicantId);
        rs = stmt.executeQuery();

        ArrayList<Integer> rIds = new ArrayList<>();
        while (rs.next()) {
            rIds.add(rs.getInt("resumeId"));
        }

        // Löschen des Profilbilds
        stmt = con.prepareStatement("SELECT pb FROM applicant WHERE id=?");
        stmt.setInt(1, applicantId);
        rs = stmt.executeQuery();
        rs.next();
        String pbPath = rs.getString("pb");
        ResourceIO.deleteFile(pbPath);

        // Alle Bewerbungen löschen
        stmt = con.prepareStatement("DELETE FROM application WHERE userId=?");
        stmt.setInt(1, applicantId);
        stmt.executeUpdate();

        // Falls es Lebensläufe gibt
        if (rIds.size() > 0) {
            // Generieren eines String im Format '(<id1>,<id2>,<id3>,...)'
            String braceIds = OfferDatabaseService.getBraceSyntax(rIds);
            stmt = con.prepareStatement("SELECT path FROM resumes WHERE id IN " + braceIds);
            rs = stmt.executeQuery();

            // Speichern aller Pfade für die Lebensläufe
            ArrayList<String> paths = new ArrayList<>();
            while (rs.next()) {
                paths.add(rs.getString("path"));
            }

            // Löschen der Lebensläufe aus der Datenbank
            stmt = con.prepareStatement("DELETE FROM resumes WHERE id IN " + braceIds);
            stmt.executeUpdate();

            // Löschen der Lebensläufe aus dem Dateiensystem
            for (String path : paths) {
                ResourceIO.deleteFile(path);
            }
            // Für den Nutzer angelegten Ordner löschen, sobald keine Lebensläufe mehr in diesem vorliegen
            if (paths.size() > 0) {
                ResourceIO.deleteUserDir(paths.get(0));
            }
        }

    }

    // Aus Attribut rs (ResultSet) ein Applicant-Obejtk erstellen
    private Applicant parseApplicant() throws SQLException {
        return new Applicant(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("salt"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getInt("gender"),
                getTitles(rs.getInt("id")),
                rs.getString("pb"),
                rs.getDouble("lat"),
                rs.getDouble("lon"),
                rs.getString("bday")
        );
    }
}
