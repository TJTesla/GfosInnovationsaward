package gfos.converter;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.database.ApplicationDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ApplicationConverter implements Converter {
    @Inject
    ApplicationDatabaseService adbs;
    @Inject
    CurrentUser cu;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        try {
            int id = Integer.parseInt(s);
            return adbs.getById(id, ((Applicant)cu.getCurrentUser()).getId() );
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
