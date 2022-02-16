package gfos.database;

import gfos.beans.Applicant;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Named
@ApplicationScoped
public class ApplicantDatabaseService extends DatabaseService implements UserDatabaseInterface {

    public ApplicantDatabaseService() throws SQLException {
        super();
    }


    public boolean createOne(Applicant a) {
        try {
            stmt = con.prepareStatement("" +
                    "INSERT INTO applicant(id, username, password, firstname, email, lastname, gender) VALUES " +
                    "(null, ?, SHA2(?, 256), ?, ?, ?, ?);"
            );
            stmt.setString(1, a.getUsername());
            stmt.setString(2, a.getPassword());
            stmt.setString(3, a.getFirstname());
            stmt.setString(4, a.getLastname());
            stmt.setString(5, a.getEmail());
            stmt.setInt(6, a.getGender());

            int affectedRows = stmt.executeUpdate();

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

    public boolean exists(Applicant a) {
        try {
            stmt = con.prepareStatement("SELECT username FROM applicant WHERE username=?");
            stmt.setString(1, a.getUsername());
            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Error while finding already existing user: " + sqlException.getMessage());
            return true;
        }
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
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getInt("gender"),
                        getTitles(rs.getInt("id"))
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

    public Applicant loginAttempt(String user, String password) {
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant WHERE username=? AND password=SHA2(?, 256)");
            stmt.setString(1, user);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            return parseApplicant();

        } catch (SQLException sqlException) {
            System.out.println("Could not check login: " + sqlException.getMessage());
            return null;
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
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getInt("gender"),
                getTitles(rs.getInt("id"))
        );
    }


    @PreDestroy
    private void deconstruct() {
        try {
            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException sqlException) {
            System.out.println("Could not destroy UserDatabaseService: " + sqlException.getMessage());
        }
    }
}
