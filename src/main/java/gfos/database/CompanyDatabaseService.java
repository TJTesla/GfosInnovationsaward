package gfos.database;

import gfos.beans.Company;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class CompanyDatabaseService extends DatabaseService implements UserDatabaseInterface {
    public CompanyDatabaseService() throws SQLException {
        super();
    }

    public boolean createOne(Company a) {
        try {
            stmt = con.prepareStatement("" +
                    "INSERT INTO company(id, name, password, email, phoneno, website, description, pb) VALUES " +
                    "(null, ?, SHA2(?, 256), ?, ?, ?, ?, ?);"
            );
            setStmtParameters(stmt, a);

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows != 0;
        } catch (SQLException sqlException) {
            System.out.println("There was an error while creating an applicant: " + sqlException.getMessage());
            return false;
        }
    }
    
    public ArrayList<Company> fetchAll() {
        ArrayList<Company> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM company;");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                result.add(new Company(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phoneno"),
                        rs.getString("website"),
                        rs.getString("description"),
                        rs.getString("pb")
                ));
            }
        } catch (SQLException sqlException) {
            System.out.println("There was an error while fetching all companies: " + sqlException.getMessage());
        }
        return result;
    }
    
    public Company getById(int id) {
        try {
            stmt = con.prepareStatement("SELECT * FROM company WHERE id=?;");
            rs = stmt.executeQuery();
            rs.next();
            
            return new Company(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phoneno"),
                    rs.getString("website"),
                    rs.getString("description"),
                    rs.getString("pb")
            );
        } catch (SQLException sqlException) {
            System.out.println("Couldn't get a company by ID: " + sqlException.getMessage());
            return null;
        }
    }
    
    public int updateCompany(Company c) {
        try {
            stmt = con.prepareStatement("UPDATE company " +
                    "SET name=?, password=SHA2(?, 256), email=?, phoneno=?, website=?, description=? " +
                    "WHERE id=?;");
            setStmtParameters(stmt, c);
            stmt.setInt(7, c.getId());

            return stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Couldn't update company: " + sqlException.getMessage());
            return 0;
        }
    }
    
    private void setStmtParameters(PreparedStatement statement, Company c) {
        try {
            statement.setString(1, c.getName());
            statement.setString(2, c.getPassword());
            statement.setString(3, c.getEmail());
            statement.setString(4, c.getPhoneno());
            statement.setString(5, c.getWebsite());
            statement.setString(6, c.getDescription());
            statement.setString(7, c.getPb());
        } catch (SQLException sqlException) {
            System.out.println("Couldn't set parameters for insertion or update of company: " + sqlException.getMessage());
        }
    }
    
    public int deleteCompany(int id) {
        try {
            stmt = con.prepareStatement("DELETE FROM company WHERE id=?");
            stmt.setInt(1, id);
            
            return stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not delete company: " + sqlException.getMessage());
            return 0;
        }
    }
}
