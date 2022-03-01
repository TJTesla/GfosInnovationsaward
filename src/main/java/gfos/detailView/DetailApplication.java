package gfos.detailView;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class DetailApplication {
    @Inject
    CurrentUser cu;
    @Inject
    OfferDatabaseService odbs;

    private Application application;
    private Offer offer;

    @PostConstruct
    public void init() {
        offer = odbs.getById(application.getOfferId());
    }

    public String checkRights() {
        if (cu.getCurrentUser() == null ||
                cu.getCurrentUser() instanceof Applicant ||
                offer.getProvider() != cu.getCurrentUser().getId()
        ) {
            return "/00-loginRegistration/login.xhtml";
        }
        return "";
    }
}
