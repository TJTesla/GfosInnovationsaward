package gfos.beans;

public class Employee extends User {
    private boolean registered;

    public Employee() {
        super("", "");
        this.name = "";
        this.password = "";
        this.registered = false;
    }

    public Employee(String name, String password, boolean registered) {
        super(name, password);
        this.registered = registered;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
