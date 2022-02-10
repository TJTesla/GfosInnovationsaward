package gfos.database;

import gfos.Env;

import java.sql.*;

public abstract class DatabaseService {
    protected Connection con;
    protected PreparedStatement stmt;
    protected ResultSet rs;

    public DatabaseService() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ApplicationManagement?useSSL=false", Env.user, Env.password);
        stmt = null;
        rs = null;
    }
}
