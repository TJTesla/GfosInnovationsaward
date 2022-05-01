package gfos.database;

import gfos.pojos.FilterObject;
import gfos.pojos.Applicant;
import gfos.pojos.Offer;
import gfos.beans.GeoCalculator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Klasse für Datenbank-Befehle mit Zusammenhang mit Angeboten

@Named
@ApplicationScoped
public class OfferDatabaseService extends DatabaseService {
    public OfferDatabaseService() throws SQLException {
        super();
    }

    // Erstellung eines Angebots
    public int createOne(Offer o) {
        try {
            // Da auch Speicherung als Entwurf hierüber funktioniert und dafür nicht alle Angaben getätigt werden müssen
            // Optionale Attribute
            String fieldString = o.getField() == -1 ? "null" : "?";
            String levelString = o.getLevel() == -1 ? "null" : "?";
            String timeString = o.getTime() == -1 ? "null" : "?";

            stmt = con.prepareStatement("" +
                    "INSERT INTO offer(id, title, tasks, qualifications, extras, field, level, time, lat, lon, draft, city) VALUES " +
                    "(null, ?, ?, ?, ?, " + fieldString + ", " + levelString + ", " + timeString + ", ?, ?, ?, ?);"
            );
            setStmtParameters(stmt, o);

            stmt.executeUpdate();

            stmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
            rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("LAST_INSERT_ID()");
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an offer: " + sqlException.getMessage());
            return -1;
        }
    }

    // Setzen der Parameter für SQL Anfrage
    // Umgeht SQL-Injection (Siehe Dokumentation)
    private static int setStmtParameters(PreparedStatement statement, Offer o) {
        try {
            statement.setString(1, o.getTitle());
            statement.setString(2, o.getTasks());
            statement.setString(3, o.getQualifications());
            statement.setString(4, o.getExtras());
            // Nicht klar, wie viele Werte angegeben werden
            // Optionale Parameter; add Variable zählt wie viele eingetragen sind -> Addiert zu Standard-Position
            // => Genauesa Hochzählen für Positionsparametert für Datenbank set Methoden
            int add = 0;
            if (o.getField() != -1) {
                statement.setInt(5, o.getField());
                add += 1;
            }
            if (o.getLevel() != -1) {
                statement.setInt(5+add, o.getLevel());
                add += 1;
            }
            if (o.getTime() != -1) {
                statement.setInt(5+add, o.getTime());
                add += 1;
            }
            statement.setDouble(5+add, o.getLat());
            statement.setDouble(6+add, o.getLon());
            statement.setBoolean(7+add, o.getDraft());
            statement.setString(8+add, o.getCity());

            return add;
        } catch (SQLException sqlException) {
            System.out.println("Couldn't set parameters for insertion or update of company: " + sqlException.getMessage());
            return 0;
        }
    }

    // Rückgabe von allen Angeboten
    public ArrayList<Offer> fetchAll() {
        ArrayList<Offer> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM offer");
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(createOffer(rs));
            }
        } catch (SQLException sqlException) {
            System.out.println("There was an error while fetching all offers: " + sqlException.getMessage());
        }
        return result;
    }

    // Erstellung einer Anfrage für Angebote durch einen Filter
    private String getQuery(FilterObject f, Boolean drafts, Applicant a) {
        // SELECT Teil
        String query = "SELECT DISTINCT * FROM offer";

        // FROM Teil
        if (f.getOnlyApplied() != null && f.getOnlyApplied().equals(true) && a != null) {
            // Wenn nur Angebote gefunden werden sollen, für die sich bereits beworden wurde
            query += " JOIN application ON offer.id=application.offerId AND application.userId=" + a.getId();
        }
        if (f.getFavorites() != null && f.getFavorites().equals(true) && a != null
                && f.getOnlyApplied() != null && f.getOnlyApplied().equals(false)) {
            // Wenn nur Angebote gefunden werden sollen, die Favorite sind und für die sioch noch nicht beworben wurde
            query += ",(SELECT DISTINCT offer.id FROM offer JOIN favorites ON offer.id=favorites.offerId AND favorites.applicantId=" + a.getId() +  ",\n" +
                    "                       (SELECT offer.id FROM offer JOIN application ON offer.id = application.offerId AND application.userID=" + a.getId() + ") AS applied\n" +
                    "WHERE offer.id <> applied.id AND offer.draft = false) AS doubles";
        } else {
            if (f.getFavorites() != null && f.getFavorites().equals(true) && a != null) {
                // Wenn nur Angebote gefunden werden sollen, die Favoriten sind
                query += " JOIN favorites ON offer.id=favorites.offerId AND favorites.applicantId=" + a.getId();
            }
            if (f.getOnlyApplied() != null && f.getOnlyApplied().equals(false) && a != null) {
                // Wenn nur Agebote gefunden werden sollen, für die sich noch nicht beworben wurde
                query += " LEFT JOIN (SELECT offer.id FROM offer JOIN application ON offer.id = application.offerId AND application.userID=" + a.getId() + ") AS applied\n" +
                        "        ON offer.id <> applied.id";
            }
        }

        // WHERE Teil
        // Für verschiedene Filter (bspw. 'field IN (3,5)' oder 'offer.draft = FALSE')

        boolean alreadyExtended = false;
        if (f.getField() != null && f.getField().size() != 0) {
            query += " WHERE field IN " + getBraceSyntax(f.getField());
            alreadyExtended = true;
        }
        if (f.getLevel() != null && f.getLevel().size() != 0) {
            // Nicht klar, welche oder ob etwas angehangen wird:
            // Überprüfung falls etwas angehangen wird:
            // Wenn nicht 'AND' ansonsten 'WHERE'
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " level IN " + getBraceSyntax(f.getLevel());
            alreadyExtended = true;
        }
        if (f.getTime() != null && f.getTime().size() != 0) {
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " time IN " + getBraceSyntax(f.getTime());
            alreadyExtended = true;
        }
        if (f.getFavorites() != null && f.getFavorites().equals(true) && a != null
                && f.getOnlyApplied() != null && f.getOnlyApplied().equals(false)) {
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " offer.id = doubles.id";
            alreadyExtended = true;
        } else if (f.getOnlyApplied() != null && f.getOnlyApplied().equals(false) && a != null) {
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " offer.draft = false AND applied.id IS NOT NULL";
            alreadyExtended = true;
        }
        if (drafts != null) {
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " offer.draft = " + drafts;
        }

        System.out.println(query);

        return query;
    }

    // Gibt zurück, ob Bewerber userId sich schon für Angebot offerId beworben hat
    public boolean alreadyApplied(int userId, int offerId) {
        try {
            stmt = con.prepareStatement(
                    "SELECT * " +
                            "FROM offer " +
                            "JOIN application a " +
                            "ON offer.id = a.offerId AND a.userId=? " +
                            "WHERE offer.id=?");
            stmt.setInt(1, userId);
            stmt.setInt(2, offerId);
            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Could not find, whether applicant " + userId + " applied to offer " + offerId + ": " + sqlException.getMessage());
            return false;
        }
    }

    // Erzeugt String aus Liste:
    // Bsp.: {1, 3, 5, 7} -> '(1,3,5,7)'
    // benötigt für Datenbank Abfrage, wo mehrere Fälle zuutreffen dürfen (bspw. Berufsfeld)
    public static String getBraceSyntax(ArrayList<Integer> list) {
        StringBuilder result = new StringBuilder("(");

        for (Integer item : list) {
            result.append(item).append(",");
        }
        result.deleteCharAt(result.length()-1);  // Delete last comma
        result.append(")");

        return result.toString();
    }

    // Gibt Offer mit übergebener ID zurück
    public Offer getById(int id) {
        try {
            stmt = con.prepareStatement("SELECT * FROM offer WHERE id=?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return createOffer(rs);
        } catch (SQLException sqlException) {
            System.out.println("There was an error fetching offer for id " + id + ": " + sqlException.getMessage());
            return null;
        }
    }

    // Publike Methoden zum Anfragen von Listen von Angeboten
    public ArrayList<Offer> getAllFinalOffers(FilterObject f, Applicant a) {
        return this.getDraftType(f, a, false);
    }
    public ArrayList<Offer> getAllDrafts(FilterObject f, Applicant a) {
        return this.getDraftType(f, a, true);
    }

    // Private Hilfsmethode
    private ArrayList<Offer> getDraftType(FilterObject f, Applicant a, boolean draft) {
        ArrayList<Offer> result = new ArrayList<>();
        try {
            // Erstellt Query mit Parametern
            stmt = con.prepareStatement(getQuery(f, draft, a));
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(createOffer(rs));
            }

            // Falls Filter nach Distanz ausgewählt -> Danach filtern
            if (a != null && f.getMaxDistance() != null) {
                result.removeIf(o ->
                        GeoCalculator.distance(
                                new double[]{o.getLat(), o.getLon()},
                                new double[]{a.getLat(), a.getLon()}
                        ) > f.getMaxDistance()
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch all offers: " + sqlException.getMessage());
        }
        return result;
    }

    // Aus parameter rs ein offer Objekt erstellen
    public static Offer createOffer(ResultSet resultSet) throws SQLException {
        return new Offer(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("tasks"),
                resultSet.getString("qualifications"),
                resultSet.getString("extras"),
                resultSet.getInt("field"),
                resultSet.getInt("level"),
                resultSet.getInt("time"),
                resultSet.getDouble("lat"),
                resultSet.getDouble("lon"),
                resultSet.getBoolean("draft"),
                resultSet.getString("city")
        );
    }

    // Methoden um von ID für Eigenschaften zu Namen zu kommen
    public String getField(int id) {
        return getType("field", id);
    }
    public String getLevel(int id) {
        return getType("level", id);
    }
    public String getTime(int id) {
        return getType("time", id);
    }

    // Algorithmus zum Zurückgeben von Strings für IDs von Berufsfeld, Einstiegslevel etc.
    public String getType(String table, int id) {
        // Einziger Unterschied
        String query = "";
        switch (table) {
            case "field":
                query = "SELECT tag FROM fields WHERE id=?";
                break;
            case "level":
                query = "SELECT term FROM level WHERE id=?";
                break;
            case "time":
                query = "SELECT term FROM time WHERE id=?";
                break;
        }

        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            if (table.equals("field")) {
                return rs.getString("tag");
            } else if (table.equals("level") || table.equals("time")) {
                return rs.getString("term");
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not transform index into name from table: " + table + ": " + sqlException.getMessage());
        }
        return null;
    }

    // Veröffentlichen von Angebot
    public void publish(Offer o) {
        try {
            stmt = con.prepareStatement("UPDATE offer SET draft=FALSE WHERE id=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not publish offer " + o.getId() + ": " + sqlException.getMessage());
        }
    }

    // Aktualisieren von Angebot
    public void update(Offer o) {
        try {
            // Gleicher Vorgang wie bei Erzeugung (Siehe 2. Methode der Klasse)
            String fieldString = o.getField() == -1 ? "" : " field=?,";
            String levelString = o.getLevel() == -1 ? "" : " level=?,";
            String timeString = o.getTime() == -1 ? "" : " time=?,";

            stmt = con.prepareStatement(
                    "UPDATE offer " +
                            "SET title=?, tasks=?, qualifications=?, extras=?," + fieldString + levelString + timeString + " lat=?, lon=?, draft=?, city=? " +
                            "WHERE id=?"
            );
            int offset = setStmtParameters(stmt, o);
            stmt.setInt(9+offset, o.getId());

            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not update offer " + o.getId() + ": " + sqlException.getMessage());
        }
    }

    // Löschen von Angebot
    public void delete(Offer o) {
        try {
            // Alle bewerbungen für das Angebot löschen
            stmt = con.prepareStatement("DELETE FROM application WHERE offerId=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();

            // Alle Favoriten-Einträge mit dem Angebot löschen
            stmt = con.prepareStatement("DELETE FROM favorites WHERE offerId=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();

            // Angebot löschen
            stmt = con.prepareStatement("DELETE FROM offer WHERE id=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not delete offer " + o.getId() + ": " + sqlException.getMessage());
        }
    }
}
