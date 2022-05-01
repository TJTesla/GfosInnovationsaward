package gfos.database;

import gfos.pojos.Pair;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;

// Klasse mit Datenbank-Befehlen ohne richtigen Zusammenhang

@Named
@ApplicationScoped
public class MiscellaneousDatabaseService extends DatabaseService {

    public MiscellaneousDatabaseService() throws SQLException {
        super();
    }

    // Methodenm zum holen der Einträge für das Filtern von Angeboten
    public ArrayList<Pair<Integer, String>> getAllTitles() {
        return getStringArr("SELECT id, term FROM title ORDER BY id ASC", "term");
    }

    public ArrayList<Pair<Integer, String>> getAllFields() {
        return getStringArr("SELECT id, tag FROM fields", "tag");
    }

    public ArrayList<Pair<Integer, String>> getAllLevels() {
        return getStringArr("SELECT id, term FROM level", "term");
    }

    public ArrayList<Pair<Integer, String>> getAllTimes() {
        return getStringArr("SELECT id, term FROM time", "term");
    }

    // Private Methode die immer aufgerufen wird
    private ArrayList<Pair<Integer, String>> getStringArr(String query, String column) {
        ArrayList<Pair<Integer, String>> arr = new ArrayList<>();

        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                arr.add(new Pair<>( rs.getInt("id"), rs.getString(column)) );
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch the titles: " + sqlException.getMessage());
        }

        return arr;
    }
}
