package gfos.converter;

import gfos.beans.Applicant;
import gfos.database.ApplicationDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ApplicationObjectConverter extends ApplicationConverter {
    @PostConstruct
    private void init() {
        object = true;
    }
}
