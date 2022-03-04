package gfos.converter;

import gfos.beans.Applicant;
import gfos.database.ApplicantDatabaseService;
import gfos.database.ApplicationDatabaseService;
import gfos.database.CompanyDatabaseService;
import gfos.database.DatabaseService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

public abstract class UserConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        try {
            int id = Integer.parseInt(s);

            DatabaseService dbs = getDbs();
            assert dbs instanceof ApplicantDatabaseService || dbs instanceof CompanyDatabaseService;
            return dbs.getById(id);
        } catch (Exception e) {
            System.out.println("Could not convert '" + s + "' into Applicant");
            return null;
        }
    }

    abstract DatabaseService getDbs();

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
