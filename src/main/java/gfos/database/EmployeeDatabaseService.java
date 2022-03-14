package gfos.database;

import gfos.beans.Employee;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.SQLException;

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

            return new Employee(
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getBoolean("registered")
            );
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch employee with name: " + name + ": " + sqlException.getMessage());
            return null;
        }
    }
}
