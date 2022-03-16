package gfos.database;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Employee;
import gfos.beans.Offer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
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
            stmt = con.prepareStatement("INSERT INTO employees(name, password, registered) VALUE (?, ?, false)");
            stmt.setString(1, e.getName());
            stmt.setString(2, e.getPassword());

            int changed = stmt.executeUpdate();
            if (changed == 0) {
                throw new SQLException();
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not create employee with name: " + e.getName() + ": " + sqlException.getMessage());
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
        return new Employee(
                rs.getString("name"),
                rs.getString("password"),
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
            stmt = con.prepareStatement("UPDATE employees SET registered=?, password=SHA2(?, 256) WHERE name=?");
            stmt.setBoolean(1, e.isRegistered());
            stmt.setString(2, e.getPassword());
            stmt.setString(3, e.getName());

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not register employee: " + e.getName() + ": " + sqlException.getMessage());
            return false;
        }
    }

    public boolean employeeCreated(String name, String key) {
        try {
            stmt = con.prepareStatement("SELECT * FROM employees WHERE name=? AND password=SHA2(?, 256) AND registered=false");
            stmt.setString(1, name);
            stmt.setString(2, key);

            rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Problem occured while finding employee with name: " + name + ": " + sqlException.getMessage());
            return false;
        }
    }
}
