package gfos.database;

import gfos.beans.User;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class UserDatabaseService extends DatabaseService {

    public UserDatabaseService() throws SQLException {
        super();
    }


    public boolean createOne(User a) {
        try {
            stmt = con.prepareStatement("" +
                    "INSERT INTO applicant(id, username, password, firstname, lastname, gender) VALUES " +
                    "(null, ?, SHA2(?, 256), ?, ?, ?);"
            );
            stmt.setString(1, a.getUsername());
            stmt.setString(2, a.getPassword());
            stmt.setString(3, a.getFirstname());
            stmt.setString(4, a.getLastname());
            stmt.setInt(5, a.getGender());

            int affectedRows = stmt.executeUpdate();

            return affectedRows != 0;
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an applicant: " + sqlException.getMessage());
            return false;
        }
    }

    public boolean exists(User a) {
        try {
            stmt = con.prepareStatement("SELECT username FROM applicant WHERE username=?");
            stmt.setString(1, a.getUsername());
            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Error while finding already existing user: " + sqlException.getMessage());
            return false;
        }
    }

    public ArrayList<User> fetchAll() {
        ArrayList<User> list = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant");
            rs = stmt.executeQuery();

            while (rs.next()) {
                User temp = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("gender")
                );

                list.add(temp);
            }
        } catch (SQLException sqlException) {
            System.out.println("There was an error while trying to fetch all Applicants");
        }

        return list;
    }

    public User loginAttempt(String user, String password) {
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant WHERE username=? AND password=SHA2(?, 256)");
            stmt.setString(1, user);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getInt("gender")
            );

        } catch (SQLException sqlException) {
            System.out.println("Could not check login: " + sqlException.getMessage());
            return null;
        }
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
