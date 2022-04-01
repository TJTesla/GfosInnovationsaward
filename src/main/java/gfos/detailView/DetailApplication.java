package gfos.detailView;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Employee;
import gfos.beans.Offer;
import gfos.database.ApplicationDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DetailApplication implements Serializable {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicationDatabaseService adbs;
    @Inject
    OfferDatabaseService odbs;

    private Application application;
    private Offer offer;
    private String offerId;

    public String checkRights() {
        if (cu.getCurrentUser() != null &&
                cu.getCurrentUser() instanceof Employee
        ) {
            return "/00-loginRegistration/login.xhtml";
        }
        return "";
    }

    private static int toInt(String str) {
        if (str == null) {
            return -1;
        }

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            System.out.println("Could not parse string: " + str + " to integer: " + e.getMessage());
            return -1;
        }
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
        offer = odbs.getById(this.application.getOfferId());
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
