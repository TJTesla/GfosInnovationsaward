package gfos.database;

import gfos.Env;

import javax.inject.Named;
import java.sql.*;

@Named
public class DatabaseService {
    protected Connection con;
    protected PreparedStatement stmt;
    protected ResultSet rs;

    public DatabaseService() throws SQLException {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ApplicationManagement?useSSL=false&allowPublicKeyRetrieval=true", Env.user, Env.password);
            stmt = null;
            rs = null;
        } catch (SQLException sqlException) {
            System.out.println("SQL Error:" + sqlException.getMessage());
            throw sqlException;
        }
    }

    public Object getById(int id) {
        return null;
    }
}
