package gfos.database;

import gfos.beans.FilterObject;
import gfos.beans.Applicant;
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

    public int createOne(Offer o) {
        try {
            stmt = con.prepareStatement("" +
                    "INSERT INTO offer(id, title, tasks, qualifications, extras, field, level, time, lat, lon, draft, city) VALUES " +
                    "(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            setStmtParameters(stmt, o);

            int affectedRows = stmt.executeUpdate();

            stmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
            rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("LAST_INSERT_ID()");
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an applicant: " + sqlException.getMessage());
            return -1;
        }
    }

    private void setStmtParameters(PreparedStatement statement, Offer o) {
        try {
            statement.setString(1, o.getTitle());
            statement.setString(2, o.getTasks());
            statement.setString(3, o.getQualifications());
            statement.setString(4, o.getExtras());
            statement.setInt(5, o.getField());
            statement.setInt(6, o.getLevel());
            statement.setInt(7, o.getTime());
            statement.setDouble(8, o.getLat());
            statement.setDouble(9, o.getLon());
            statement.setBoolean(10, o.getDraft());
            statement.setString(11, o.getCity());
        } catch (SQLException sqlException) {
            System.out.println("Couldn't set parameters for insertion or update of company: " + sqlException.getMessage());
        }
    }

    public ArrayList<Offer> fetchAll(FilterObject filter, Applicant a) {
        ArrayList<Offer> result = new ArrayList<>();
        try {
            String query = getQuery(filter, false, a);
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(createOffer(rs));
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

    private String getQuery(FilterObject f, Boolean drafts, Applicant a) {
        String query = "SELECT * FROM offer";
        if (f.getOnlyApplied() != null && f.getOnlyApplied().equals(true) && a != null) {
            query += " JOIN application ON offer.id=application.offerId AND application.userId=" + a.getId();
        }
        if (f.getOnlyApplied() != null && f.getOnlyApplied().equals(false) && a != null) {
            query += ", (SELECT offer.id FROM offer JOIN application ON offer.id = application.offerId AND application.userID=" + a.getId() + ") AS applied";
        }
        if (f.getFavorites() != null && f.getFavorites().equals(true) && a != null) {
            query += " JOIN favorites ON offer.id=favorites.offerId AND favorites.applicantId=" + a.getId();
        }

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
            alreadyExtended = true;
        }
        if (f.getOnlyApplied() != null && f.getOnlyApplied().equals(false) && a != null) {
            if (alreadyExtended) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " offer.id <> applied.id";
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

    public static String getBraceSyntax(ArrayList<Integer> list) {
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
            stmt = con.prepareStatement(getQuery(f, draft, a));
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

    public String getField(int id) {
        return getType("field", id);
    }

    public String getLevel(int id) {
        return getType("level", id);
    }

    public String getTime(int id) {
        return getType("time", id);
    }

    public String getType(String table, int id) {
        String query = "";
        if (table.equals("field")) {
            query = "SELECT tag FROM fields WHERE id=?";
        } else if (table.equals("level")) {
            query = "SELECT term FROM level WHERE id=?";
        } else if (table.equals("time")) {
            query = "SELECT term FROM time WHERE id=?";
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

    public void publish(Offer o) {
        try {
            stmt = con.prepareStatement("UPDATE offer SET draft=FALSE WHERE id=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not publish offer " + o.getId() + ": " + sqlException.getMessage());
        }
    }

    public void delete(Offer o) {
        try {
            stmt = con.prepareStatement("DELETE FROM application WHERE offerId=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();

            stmt = con.prepareStatement("DELETE FROM favorites WHERE offerId=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();

            stmt = con.prepareStatement("DELETE FROM offer WHERE id=?");
            stmt.setInt(1, o.getId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not delete offer " + o.getId() + ": " + sqlException.getMessage());
        }
    }
}
