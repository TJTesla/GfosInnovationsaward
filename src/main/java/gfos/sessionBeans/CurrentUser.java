package gfos.sessionBeans;

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
    private Company currentCompany;

    @PostConstruct
    public void init() {
        currentUser = new User();
    }

    public T getCurrentUser() {
        if (currentUser == null) {
            return this.currentCompany;
        } else if (currentCompany == null) {
            return this.currentUser;
        }

        return null;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
