package gfos.longerBeans;

import gfos.beans.Applicant;
import gfos.beans.Company;
import gfos.beans.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CurrentUser implements Serializable {
    private User currentUser;

    @PostConstruct
    public void init() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }
}
