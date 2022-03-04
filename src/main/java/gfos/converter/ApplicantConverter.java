package gfos.converter;

import gfos.database.ApplicantDatabaseService;
import gfos.database.DatabaseService;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ApplicantConverter extends UserConverter implements Converter {
    @Inject
    ApplicantDatabaseService adbs;

    @Override
    DatabaseService getDbs() {
        return adbs;
    }
}
