package gfos.converter;

import gfos.pojos.Applicant;
import gfos.database.ApplicationDatabaseService;
import gfos.beans.CurrentUser;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

// Umwandeln von Id als URL-Parameter zu Bewerbung
// Abstrakte Klasse -> ApplicationObjectConverter und ApplicationtextConverter erben
// Beide Klassen teilen sich alles, bis auf Rückgabe -> Alles bis auf Rückgabe wird in Superklasse implementiert

public abstract class ApplicationConverter implements Converter {
    @Inject
    ApplicationDatabaseService adbs;
    @Inject
    CurrentUser cu;

    // Attribut wid in Konstruktor der Sub-Klasen gesetzt
    protected boolean object;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        try {
            int id = Integer.parseInt(s);
            // Für verschiedene Fälle:
            // Entweder die ganze Bewerbung oder nur der Text Teil (Motivation)
            if (object) {
                return adbs.getById(id, ((Applicant) cu.getCurrentUser()).getId());
            } else {
                return adbs.getById(id, ((Applicant) cu.getCurrentUser()).getId()).getText();
            }
        } catch (Exception e) {
            System.out.println("Could not convert String " + s + " to Application object: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return null;
        }

        return o.toString();
    }
}
