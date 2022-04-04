package gfos.database;

import gfos.beans.Applicant;
import gfos.beans.Employee;
import gfos.beans.PasswordManager;
import gfos.beans.User;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Named
@ApplicationScoped
public class ApplicantDatabaseService extends DatabaseService implements UserDatabaseInterface {

    public ApplicantDatabaseService() throws SQLException {
        super();
    }


    public boolean createOne(Applicant a) {
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

            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();

            a.setId(rs.getInt("LAST_INSERT_ID()"));
            affectedRows += insertTitles(a);

            return affectedRows != 0;
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an applicant: " + sqlException.getMessage());
            return false;
        }
    }

    private int insertTitles(Applicant applicant) throws SQLException {
        stmt = con.prepareStatement("SELECT * FROM title");
        rs = stmt.executeQuery();

        HashMap<String, Integer> possibleTitles = new HashMap<>();
        while (rs.next()) {
            possibleTitles.put(rs.getString("term"), rs.getInt("id"));
        }

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO titleRelation(applicantId, titleId) VALUES ");
        for (String title : applicant.getTitles()) {
            sqlBuilder.append("(").append(applicant.getId()).append(", ").append(possibleTitles.get(title)).append("),");
        }
        String sql = sqlBuilder.toString();
        sql = sql.substring(0, sql.length()-1);

        stmt = con.prepareStatement(sql);

        return stmt.executeUpdate();
    }

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

    public Applicant getById(int id) {
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant WHERE id=?;");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            return parseApplicant();
        } catch (SQLException sqlException) {
            System.out.println("Could not get applicant with id " + id + ": " + sqlException.getMessage());
            return null;
        }
    }

    private ArrayList<String> getTitles(int userId) {
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

    public boolean changePwd(Applicant a, String newPwd) {
        String salt = PasswordManager.generateSalt();
        String hashPwd = PasswordManager.getHash(newPwd, salt);

        try {
            stmt = con.prepareStatement("UPDATE applicant SET password=?, salt=? WHERE id=?");
            stmt.setString(1, hashPwd);
            stmt.setString(2, salt);
            stmt.setInt(3, a.getId());

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not change pwd of user " + a.getId() + ": "+ sqlException.getMessage());
            return false;
        }
    }

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

    private User findUser(String table, String name, String password) throws SQLException, NoSuchAlgorithmException {
        String hashPwd = PasswordManager.getHash(password, getSalt(table, name));

        String activeQuery = "";
        if (table.equals("applicant")) {
            activeQuery = "SELECT * FROM applicant WHERE username=? AND password=?";
        } else if (table.equals("employee")) {
            activeQuery = "SELECT * FROM employees WHERE name=? AND password=?";
        }

        stmt = con.prepareStatement(activeQuery);
        stmt.setString(1, name);
        stmt.setString(2, hashPwd);
        rs = stmt.executeQuery();

        if (table.equals("applicant")) {
            return parseApplicant();
        } else if (table.equals("employee")) {
            return EmployeeDatabaseService.createEmployee(rs);
        }

        return null;
    }

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

    public boolean update(Applicant a) {
        try {
            stmt = con.prepareStatement("UPDATE applicant SET username=?, firstname=?, lastname=?, email=?, lat=?, lon=? WHERE id=?");
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getFirstname());
            stmt.setString(3, a.getLastname());
            stmt.setString(4, a.getEmail());
            stmt.setDouble(5, a.getLat());
            stmt.setDouble(6, a.getLon());
            stmt.setInt(7, a.getId());

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not update applicant " + a.getId() + ": " + sqlException.getMessage());
            return false;
        }
    }

    private Applicant parseApplicant() throws SQLException {
        if (!rs.next()) {
            return null;
        }

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
