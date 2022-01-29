package gfos.database;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class MiscellaneousDatabaseService extends DatabaseService {

    public MiscellaneousDatabaseService() throws SQLException {
        super();
    }

    public ArrayList<String> getAllTitles() {
        ArrayList<String> arr = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT term FROM title");
            rs = stmt.executeQuery();

            while (rs.next()) {
                arr.add(rs.getString("term"));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch the titles: " + sqlException.getMessage());
        }

        return arr;
    }
}
