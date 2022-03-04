package gfos.converter;

import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class OfferConverter implements Converter {
    @Inject
    OfferDatabaseService odbs;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        try {
            int id = Integer.parseInt(s);
            return odbs.getById(id);
        } catch (Exception e) {
            throw new ConverterException("Could not convert with Offer id: " + s);
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Offer) {
            int id = ((Offer) o).getId();
            return String.valueOf(id);
        } else {
            throw new ConverterException("The given type could't be converted to Offer type");
        }
    }
}
