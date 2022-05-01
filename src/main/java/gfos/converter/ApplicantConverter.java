package gfos.converter;

import gfos.pojos.Applicant;
import gfos.database.ApplicantDatabaseService;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

// Converter zum Umwandeln von ID als URL-Parameter zu Bewerbern

@Named
@RequestScoped
public class ApplicantConverter implements Converter {
    @Inject
    ApplicantDatabaseService adbs;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        try {
            int id = Integer.parseInt(s);

            // Finden des Bewerbers mit der Id
            return adbs.getById(id);
        } catch (Exception e) {
            System.out.println("Could not convert '" + s + "' into Applicant");
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return null;
        }

        try {
            int id = ((Applicant) o).getId();
            return String.valueOf(id);
        } catch (Exception e) {
            System.out.println("Could not create string of integer for Applicant");
            return null;
        }
    }
}
