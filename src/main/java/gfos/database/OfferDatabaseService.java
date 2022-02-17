package gfos.database;

import gfos.beans.Company;
import gfos.beans.Offer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class OfferDatabaseService extends DatabaseService {
    public OfferDatabaseService() throws SQLException {
        super();
    }

    public boolean createOne(Offer o) {
        try {
            stmt = con.prepareStatement("" +
                    "INSERT INTO offer(id, title, description, provider, tag, category, lat, lon) VALUES " +
                    "(null, ?, ?, ?, ?, ?, ?, ?);"
            );
            setStmtParameters(stmt, o);

            int affectedRows = stmt.executeUpdate();

            return affectedRows != 0;
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an applicant: " + sqlException.getMessage());
            return false;
        }
    }

    private void setStmtParameters(PreparedStatement statement, Offer o) {
        try {
            statement.setString(1, o.getTitle());
            statement.setString(2, o.getDescription());
            statement.setInt(3, o.getProvider());
            statement.setInt(4, o.getTag());
            statement.setInt(5, o.getCategory());
            statement.setDouble(6, o.getLat());
            statement.setDouble(7, o.getLon());
        } catch (SQLException sqlException) {
            System.out.println("Couldn't set parameters for insertion or update of company: " + sqlException.getMessage());
        }
    }

    public ArrayList<Offer> fetchAll() {
        ArrayList<Offer> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM offer;");
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new Offer(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("provider"),
                        rs.getInt("tag"),
                        rs.getInt("category"),
                        rs.getDouble("lat"),
                        rs.getDouble("lon")
                ));
            }
        } catch (SQLException sqlException) {
            System.out.println("There was an error while fetching all companies: " + sqlException.getMessage());
        }
        return result;
    }

    public Offer getById(int id) {
        try {
            stmt = con.prepareStatement("SELECT * FROM offer WHERE id=?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return new Offer(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("provider"),
                    rs.getInt("tag"),
                    rs.getInt("category"),
                    rs.getDouble("lat"),
                    rs.getDouble("lon")
            );
        } catch (SQLException sqlException) {
            System.out.println("There was an error fetching offer for id " + id + ": " + sqlException.getMessage());
            return null;
        }
    }

    public String getTag(int id) {
        try {
            stmt = con.prepareStatement("SELECT tag FROM tags WHERE id=?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            rs.next();

            return rs.getString("tag");
        } catch (SQLException sqlException) {
            System.out.println("Couldn't get tag: " + sqlException.getMessage());
            return "";
        }
    }
}