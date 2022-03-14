package gfos.beans;

public class Employee {
    private String name;
    private String password;
    private boolean registered;

    public Employee() {
        this.name = "";
        this.password = "";
        this.registered = false;
    }

    public Employee(String name, String password, boolean registered) {
        this.name = name;
        this.password = password;
        this.registered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
