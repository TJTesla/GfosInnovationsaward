package gfos.database;

import gfos.beans.Application;
import gfos.beans.Resume;

import javax.servlet.jsp.jstl.sql.SQLExecutionTag;
import java.sql.SQLException;

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
        stmt = con.prepareStatement("INSERT INTO resumes(id, path, name) VALUES (null, ?, ?); SELECT LAST_INSERT_ID();");
        stmt.setString(1, r.getPath());
        stmt.setString(2, r.getName());

        rs = stmt.executeQuery();
        return rs.getInt("id");
    }

    private boolean resumeExists() {
        return false;
    }
}
