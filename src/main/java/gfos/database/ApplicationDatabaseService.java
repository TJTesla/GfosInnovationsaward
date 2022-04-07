package gfos.database;

import gfos.pojos.Application;
import gfos.pojos.Resume;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Named
@ApplicationScoped
public class ApplicationDatabaseService extends DatabaseService {
    public ApplicationDatabaseService() throws SQLException {
        super();
    }

    public int apply(Application a, Resume r) {
        try {
            int resumeId = this.createResume(r);

            stmt = con.prepareStatement("INSERT INTO application(userId, offerId, text, status, resumeId, draft) VALUES (?, ?, ?, 0, ?, ?)");
            stmt.setInt(1, a.getUserId());
            stmt.setInt(2, a.getOfferId());
            stmt.setString(3, a.getText());
            stmt.setInt(4, resumeId);
            stmt.setBoolean(5, a.getDraft());

            if (stmt.executeUpdate() != 1) {
                return -1;
            }

            return a.getOfferId();
        } catch (SQLException sqlException) {
            System.out.println("Could not create Application and/or Resume: " + sqlException.getMessage());
        }
        return -1;
    }

    private int createResume(Resume r) throws SQLException {
        stmt = con.prepareStatement("INSERT INTO resumes(id, path, name) VALUES (null, ?, ?);");
        stmt.setString(1, r.getPath());
        stmt.setString(2, r.getName());
        stmt.executeUpdate();

        rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        rs.next();
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

    public Resume getResume(int id) {
        try {
            stmt = con.prepareStatement("SELECT * FROM resumes WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return createResume(rs);
        } catch (SQLException sqlException) {
            System.out.println("Could not create resume with id " + id + ": " + sqlException.getMessage());
            return null;
        }
    }

    public void changeVisibility(Application a) {
        a.setDraft(!a.getDraft());

        try {
            stmt = con.prepareStatement("UPDATE application SET draft=? WHERE userId=? AND offerId=?");
            stmt.setBoolean(1, a.getDraft());
            stmt.setInt(2, a.getUserId());
            stmt.setInt(3, a.getOfferId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not change visibility for application for offer " + a.getOfferId() + " from applicant " + a.getDraft() + ": " + sqlException.getMessage());
        }
    }

    private static Resume createResume(ResultSet rs) throws SQLException {
        return new Resume(
                rs.getInt("id"),
                rs.getString("path"),
                rs.getString("name")
        );
    }

    public String getStatusString(int id) {
        final HashMap<Integer, String> status = new HashMap<>();
        status.put(0, "Noch nicht bearbeitet");
        status.put(1, "Wird zurzeit bearbeitet");
        status.put(10, "Angenommen");
        status.put(11, "Abgelehnt");

        return status.get(id);
    }

    public ArrayList<Application> getAllApplications(int id, boolean draft, int filter) {
        ArrayList<Application> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM application WHERE userId=? AND draft=?";
            if (filter != -1) {
                query += " AND status=?";
            }

            stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setBoolean(2, draft);
            if (filter != -1) {
                stmt.setInt(3, filter);
            }
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(createApplication(rs));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not detch all applications for user " + id + ": " + sqlException.getMessage());
        }
        return result;
    }

    public void delete(Application a) {
        System.out.println("DELETE APPLICATION");
        try {
            stmt = con.prepareStatement("DELETE FROM application WHERE userId=? AND offerId=?");
            stmt.setInt(1, a.getUserId());
            stmt.setInt(2, a.getOfferId());
            stmt.executeUpdate();

            stmt = con.prepareStatement("DELETE FROM resumes WHERE id=?");
            stmt.setInt(1, a.getResumeId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not delete application for offer " + a.getOfferId() + " from user " + a.getUserId() + ": " + sqlException.getMessage());
        }
    }

    public int update(int uId, int oId, String text, Resume r, boolean draft) {
        try {
            int oldResumeId = -1;
            if (r != null) {
                oldResumeId = this.getResumeId(uId, oId);
            }

            String resumeExtension = "";
            if (r != null) {
                resumeExtension = "resumeID=null,";
            }

            stmt = con.prepareStatement("UPDATE application SET text=?," + resumeExtension + " draft=? WHERE userId=? AND offerId=?");
            stmt.setString(1, text);
            stmt.setBoolean(2, draft);
            stmt.setInt(3, uId);
            stmt.setInt(4, oId);

            if (stmt.executeUpdate() != 1) {
                return -1;
            }

            if (r != null) {
                this.deleteResume(oldResumeId);
                r.setId(this.createResume(r));

                stmt = con.prepareStatement("UPDATE application SET resumeId=? WHERE userId=? AND offerId=?");
                stmt.setInt(1, r.getId());
                stmt.setInt(2, uId);
                stmt.setInt(3, oId);

                if (stmt.executeUpdate() != 1) {
                    return -1;
                }
            }

            return oId;
        } catch (SQLException sqlException) {
            System.out.println("Could not updatze application from applicant " + uId + " for offer " + oId + ": " + sqlException.getMessage());
            return -1;
        }
    }

    private int getResumeId(int uId, int oId) {
        try {
            stmt = con.prepareStatement("SELECT resumeId FROM application WHERE userId=? AND offerId=?");
            stmt.setInt(1, uId);
            stmt.setInt(2, oId);
            rs = stmt.executeQuery();

            rs.next();
            return rs.getInt("resumeId");
        } catch (SQLException sqlException) {
            System.out.println("Could not get resume ID for offer " + oId + " and applicant " +  uId + ": " + sqlException.getMessage());
            return -1;
        }
    }

    private boolean deleteResume(int id) {
        try {
            stmt = con.prepareStatement("DELETE FROM resumes WHERE id=?");
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not dlete resume: " + id + ": " + sqlException.getMessage());
            return false;
        }
    }

    public void changeStatus(Application a) {
        // Status of a is already changed
        try {
            stmt = con.prepareStatement("UPDATE application SET status=? WHERE userId=? AND offerId=?");
            stmt.setInt(1, a.getStatus());
            stmt.setInt(2, a.getUserId());
            stmt.setInt(3, a.getOfferId());

            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not change status for application from applicant " + a.getUserId() + " for offer " + a.getOfferId() + ": " + sqlException.getMessage());
        }
    }

    public static Application createApplication(ResultSet rs) throws SQLException {
        return new Application(
                rs.getInt("userId"),
                rs.getInt("offerId"),
                rs.getString("text"),
                rs.getInt("status"),
                rs.getInt("resumeId"),
                rs.getBoolean("draft")
        );
    }
}
