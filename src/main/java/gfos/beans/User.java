package gfos.beans;

public abstract class User {
    protected int id;
    protected String name;
    protected String password;
    protected String email;
    protected String pb;

    public User(int id, String name, String password, String email, String pb) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.pb = pb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }
}
