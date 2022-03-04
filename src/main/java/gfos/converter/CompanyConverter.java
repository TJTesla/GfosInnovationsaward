package gfos.converter;

import gfos.database.CompanyDatabaseService;
import gfos.database.DatabaseService;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class CompanyConverter extends UserConverter implements Converter {
    @Inject
    CompanyDatabaseService cdbs;

    @Override
    DatabaseService getDbs() {
        return cdbs;
    }
}
