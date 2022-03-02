package gfos.database;

import gfos.beans.Application;
import gfos.beans.Resume;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.servlet.jsp.jstl.sql.SQLExecutionTag;
import java.sql.ResultSet;
import java.sql.SQLException;

@Named
@ApplicationScoped
public class ApplicationDatabaseService extends DatabaseService {
    public ApplicationDatabaseService() throws SQLException {
        super();
    }

    public void apply(Application a, Resume r) {
        try {
            stmt = con.prepareStatement("INSERT INTO application(userId, offerId, text, status, resumeId) VALUES (?, ?, ?, 0, ?)");
            stmt.setInt(1, a.getUserId());
            stmt.setInt(2, a.getOfferId());
            stmt.setString(3, a.getText());
            stmt.setInt(4, this.createResume(r));

            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not create Application and/or Resume: " + sqlException.getMessage());
        }
    }

    private int createResume(Resume r) throws SQLException {
        stmt = con.prepareStatement("INSERT INTO resumes(id, path, name) VALUES (null, ?, ?);");
        stmt.setString(1, r.getPath());
        stmt.setString(2, r.getName());
        stmt.executeUpdate();

        rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        return rs.getInt("LAST_INSERT_ID()");
    }

    private boolean resumeExists() {
        return false;
    }

    public Application getById(int oId, int aId) {
        try {
            stmt = con.prepareStatement("SELECT * FROM application WHERE offerId=? AND userId=?;");
            stmt.setInt(1, oId);
            stmt.setInt(2, aId);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return createApplication(rs);
        } catch (SQLException sqlException) {
            System.out.println("Could not get application with offer Id: " + oId + " and userId: " + aId + ": " + sqlException.getMessage());
            return null;
        }
    }

    private static Application createApplication(ResultSet rs) throws SQLException {
        return new Application(
                rs.getInt("userId"),
                rs.getInt("offerId"),
                rs.getString("text"),
                rs.getInt("status"),
                rs.getInt("resumeId")
        );
    }
}
