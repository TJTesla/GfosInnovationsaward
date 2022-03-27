package gfos.database;

import gfos.FilterObject;
import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Offer;
import gfos.longerBeans.GeoCalculator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class OfferDatabaseService extends DatabaseService {
    public OfferDatabaseService() throws SQLException {
        super();
    }

    public boolean createOne(Offer o) {
        try {
            stmt = con.prepareStatement("" +
                    "INSERT INTO offer(id, title, description, field, level, time, lat, lon, draft) VALUES " +
                    "(null, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
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
            statement.setInt(4, o.getField());
            statement.setInt(5, o.getLevel());
            statement.setInt(6, o.getTime());
            statement.setDouble(7, o.getLat());
            statement.setDouble(8, o.getLon());
            statement.setBoolean(9, o.getDraft());
        } catch (SQLException sqlException) {
            System.out.println("Couldn't set parameters for insertion or update of company: " + sqlException.getMessage());
        }
    }

    public ArrayList<Offer> fetchAll(FilterObject filter, Applicant a) {
        ArrayList<Offer> result = new ArrayList<>();
        try {
            String query = getQuery(filter, false);
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new Offer(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("field"),
                        rs.getInt("level"),
                        rs.getInt("time"),
                        rs.getDouble("lat"),
                        rs.getDouble("lon"),
                        rs.getBoolean("draft")
                ));
            }

            // TODO: Uncomment and filter by distance
            /*result.removeIf(o ->
                    GeoCalculator.distance(
                            new double[]{o.getLat(), o.getLon()},
                            new double[]{a.getLat(), a.getLon()}
                    ) > filter.getMaxDistance()
            );*/
        } catch (SQLException sqlException) {
            System.out.println("There was an error while fetching all companies: " + sqlException.getMessage());
        }
        return result;
    }

    private String getQuery(FilterObject f, Boolean drafts) {
        String query = "SELECT * FROM offer";

        boolean alreadyExtended = false;
        if (f.getField() != null && f.getField().size() != 0) {
            query += " WHERE field IN " + getBraceSyntax(f.getField());
            alreadyExtended = true;
        }
        if (f.getLevel() != null && f.getLevel().size() != 0) {
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
        }
        if (drafts != null) {
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " draft = " + drafts;
        }

        System.out.println(query);

        return query;
    }

    private String getBraceSyntax(ArrayList<Integer> list) {
        StringBuilder result = new StringBuilder("(");

        for (Integer item : list) {
            result.append(item).append(",");
        }
        result.deleteCharAt(result.length()-1);  // Delete last comma
        result.append(")");

        return result.toString();
    }

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

    public ArrayList<Offer> getAllFinalOffers(FilterObject f, Applicant a) {
        return this.getDraftType(f, a, false);
    }

    public ArrayList<Offer> getAllDrafts(FilterObject f, Applicant a) {
        return this.getDraftType(f, a, true);
    }

    private ArrayList<Offer> getDraftType(FilterObject f, Applicant a, boolean draft) {
        ArrayList<Offer> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement(getQuery(f, draft));
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(createOffer(rs));
            }

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

    public static Offer createOffer(ResultSet resultSet) throws SQLException {
        return new Offer(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getInt("field"),
                resultSet.getInt("level"),
                resultSet.getInt("time"),
                resultSet.getDouble("lat"),
                resultSet.getDouble("lon"),
                resultSet.getBoolean("draft")
        );
    }

    public String getTag(int id) {
        try {
            stmt = con.prepareStatement("SELECT tag FROM fields WHERE id=?");
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
