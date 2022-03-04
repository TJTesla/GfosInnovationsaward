package gfos.converter;

import gfos.database.ApplicationDatabaseService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

public class ApplicationConverter implements Converter {
    @Inject
    ApplicationDatabaseService adbs;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        try {
            // int id = Integer.parseInt(s);
            System.out.println(s);
            return null;
            // return adbs.getById()
        } catch (Exception e) {
            System.out.println("Could not convert String " + s + " to Application object: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return null;
    }
}
