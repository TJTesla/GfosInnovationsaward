package gfos.converter;

import gfos.beans.Applicant;
import gfos.database.ApplicationDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

public abstract class ApplicationConverter implements Converter {
    @Inject
    ApplicationDatabaseService adbs;
    @Inject
    CurrentUser cu;

    protected boolean object;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        try {
            int id = Integer.parseInt(s);
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
