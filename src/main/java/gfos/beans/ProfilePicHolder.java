package gfos.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

// Temporärer Hallter zum Umgehen der Begrenzung von ViewScoped Beans
// ViewScoped Beans können keinen Parameter zum Laden eines Profilbildes bieten,
// da es während des Runterladens keinen ViewScope gibt

@Named
@RequestScoped
public class ProfilePicHolder {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
