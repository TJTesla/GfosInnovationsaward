package gfos.pojos;

public class Employee extends User {
    private boolean registered;

    public Employee() {
        super("", "", "");
        this.name = "";
        this.password = "";
        this.registered = false;
    }

    public Employee(String name, String password, String salt, boolean registered) {
        super(name, password, salt);
        this.registered = registered;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
