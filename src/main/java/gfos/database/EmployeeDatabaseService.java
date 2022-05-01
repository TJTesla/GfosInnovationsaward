package gfos.database;

import gfos.pojos.Applicant;
import gfos.pojos.Application;
import gfos.pojos.Employee;
import gfos.pojos.PasswordManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Klasse mit Datenbank-Befehlen im Zusammenhang mit Angestellten

@Named
@ApplicationScoped
public class EmployeeDatabaseService extends DatabaseService {
    public EmployeeDatabaseService() throws SQLException {
        super();
    }

    // Speicherung von neuem Angestellten
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
        }
    }

    // Nicht benutzt -> Dennoch noch nicht gelöscht
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

    // Erstellung eines Employee Objektes aus rs als Parameter
    public static Employee createEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("salt"),
                rs.getBoolean("registered")
        );
    }

    // Rückgabe von allen Angeboten
    public ArrayList<Application> getAllApplications(int oId) {
        ArrayList<Application> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM application WHERE offerId=? AND draft=FALSE");
            stmt.setInt(1, oId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(ApplicationDatabaseService.createApplication(rs));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch all applications: " + sqlException.getMessage());
        }
        return result;
    }

    // Rückgabe von allen Angestellten
    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> result = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM employees");
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(createEmployee(rs));
            }
        } catch (SQLException sqlException) {
            System.out.println("Could not fetch all employees: " + sqlException.getMessage());
        }
        return result;
    }

    // Löschen von einem Angestellten
    public void deleteEmployee(Employee e) {
        try {
            stmt = con.prepareStatement("DELETE FROM employees WHERE name=?");
            stmt.setString(1, e.getName());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Could not delete employee " + e.getName() + ": " + sqlException.getMessage());
        }
    }

    // Registrieren/Aktivieren eines Angestellten
    public boolean registerEmployee(Employee e) {
        try {
            // Setzt neues Passwort anstatt generierten key
            String salt = PasswordManager.generateSalt();
            String hashPwd = PasswordManager.getHash(e.getPassword(), salt);

            // Aktualisieren der Daten
            stmt = con.prepareStatement("UPDATE employees SET registered=true, password=?, salt=? WHERE name=?");
            stmt.setString(1, hashPwd);
            stmt.setString(2, salt);
            stmt.setString(3, e.getName());

            return stmt.executeUpdate() == 1;
        } catch (SQLException sqlException) {
            System.out.println("Could not register employee: " + e.getName() + ": " + sqlException.getMessage());
            return false;
        }
    }

    // Gibt salt für Zeile zurück mit Namen als Parameter
    public String getSalt(String name) throws SQLException {
        stmt = con.prepareStatement("SELECT salt FROM employees WHERE name=?");
        stmt.setString(1, name);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        return rs.getString("salt");
    }

    // Überprüft ob ein Angestellter erzeugt wurde / Ob eine Aktivierung möglich ist
    // => Name erstellt; Noch nicht aktiviert
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
        }
    }
}
