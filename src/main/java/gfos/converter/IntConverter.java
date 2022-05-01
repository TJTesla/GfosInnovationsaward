package gfos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

// Umwandeln einer ID als URL-parameter (übergeben als String) in einen int

@Named
@RequestScoped
public class IntConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("Could not convert \"" + s + "\" to int: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return String.valueOf(o);
    }
}
