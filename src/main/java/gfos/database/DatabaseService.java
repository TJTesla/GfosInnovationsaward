package gfos.database;

import gfos.pojos.Env;

import javax.annotation.PreDestroy;
import java.sql.*;

public abstract class DatabaseService {
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

    @PreDestroy
    public void deconstruct() {
        try {
            con.close();
            stmt.close();
            rs.close();
        } catch (SQLException sqlException) {
            System.out.println("Could not destroy UserDatabaseService: " + sqlException.getMessage());
        }
    }
}
