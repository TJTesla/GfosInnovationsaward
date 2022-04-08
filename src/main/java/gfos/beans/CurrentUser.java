package gfos.beans;

import gfos.pojos.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

// Speichert, dem aktuell angemeldeten Nutzer als Onjekt von Typ User
// Ermöglicht einfaches Überprüfen, wer angemeldet ist

@Named
@SessionScoped
public class CurrentUser implements Serializable {
    // Aktuell angemeldeter Nutzer
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
