package gfos.longerBeans;

import gfos.beans.Applicant;
import gfos.beans.Company;
import gfos.beans.Employee;
import gfos.beans.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CurrentUser implements Serializable {
    private Object currentUser;

    @PostConstruct
    public void init() {
        currentUser = null;
    }

    public Object getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setCurrentUser(Employee employee) {
        this.currentUser = employee;
    }

    public void logout() {
        this.currentUser = null;
    }
}
