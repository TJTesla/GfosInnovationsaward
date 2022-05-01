package gfos.controller;


import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

// Eigentlich eine Test-Klasse für BooleanCheckBoxes
// Unklar ob noch benutzt -> Noch nicht gelöscht

@Named
@RequestScoped
public class TestController implements Serializable {
    private boolean box = true;

    public void check() {
        if (box) {
            System.out.println("Checked");
        } else {
            System.out.println("Unchecked");
        }
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }
}
