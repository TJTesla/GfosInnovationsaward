package gfos.database;

import gfos.beans.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Named
@ApplicationScoped
public class EmployeeDatabaseService extends DatabaseService {
    public EmployeeDatabaseService() throws SQLException {
        super();
    }

    public void createOne(Employee e) {
        try {
            // Salt and hash
            e.setSalt(PasswordManager.generateSalt());
            e.setPassword(PasswordManager.getHash(e.getPassword(), e.getSalt()));

            stmt = con.prepareStatement("INSERT INTO employees(name, password, salt, registered) VALUE (?, ?, ?, false)");
            stmt.setString(1, e.getName());
            stmt.setString(2, e.getPassword());
            stmt.setString(3, e.getSalt());

            int changed = stmt.executeUpdate();
            if (changed == 0) {
                throw new SQLException();
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not create employee with name: " + e.getName() + ": " + sqlException.getMessage());
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("Could not generate hash for passwort of employee: " + exception.getMessage());
        }
    }

    public Employee getByName(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM employees WHERE name=?");
            stmt.setString(1, name);

            rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return createEmployee(rs);
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch employee with name: " + name + ": " + sqlException.getMessage());
            return null;
        }
    }

    public static Employee createEmployee(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new Employee(
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("salt"),
                rs.getBoolean("registered")
        );
    }

    public ArrayList<Application> getAllApplications() {
        ArrayList<Application> result = new ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT * FROM application");

            while (rs.next()) {
                result.add(ApplicationDatabaseService.createApplication(rs));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch all applications: " + sqlException.getMessage());
        }
        return result;
    }

    public boolean registerEmployee(Employee e) {
        try {
            String salt = PasswordManager.generateSalt();
            String hashPwd = PasswordManager.getHash(e.getPassword(), salt);

            stmt = con.prepareStatement("UPDATE employees SET registered=true, password=?, salt=? WHERE name=?");
            stmt.setString(1, hashPwd);
            stmt.setString(2, salt);
            stmt.setString(3, e.getName());

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not register employee: " + e.getName() + ": " + sqlException.getMessage());
            return false;
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("Could not generate hash: " + exception.getMessage());
            return false;
        }
    }

    public String getSalt(String name) throws SQLException {
        stmt = con.prepareStatement("SELECT salt FROM employees WHERE name=?");
        stmt.setString(1, name);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        return rs.getString("salt");
    }

    public boolean employeeCreated(String name, String key) {
        try {
            String hashKey = PasswordManager.getHash(key, getSalt(name));

            stmt = con.prepareStatement("SELECT * FROM employees WHERE name=? AND password=? AND registered=false");
            stmt.setString(1, name);
            stmt.setString(2, hashKey);

            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Problem occured while finding employee with name: " + name + ": " + sqlException.getMessage());
            return false;
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("Could not crate hash: " + exception.getMessage());
            return false;
        }
    }
}
