package gfos.database;

import gfos.Env;
import gfos.requestBeans.Applicant;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class UserDatabaseService {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    public UserDatabaseService() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ApplicationManagement?useSSL=false", Env.user, Env.password);
        stmt = null;
        rs = null;
    }

    public boolean createOne(Applicant a) {
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

    public boolean exists(Applicant a) {
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

    public ArrayList<Applicant> fetchAll() {
        ArrayList<Applicant> list = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Applicant temp = new Applicant(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getString("username"),
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
