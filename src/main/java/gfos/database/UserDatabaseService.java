package gfos.database;

import gfos.Env;
import gfos.RequestBeans.User;

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

    public ArrayList<User> fetchAll() {
        ArrayList<User> list = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM applicant");
            rs = stmt.executeQuery();

            while (rs.next()) {
                User temp = new User(
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
}
