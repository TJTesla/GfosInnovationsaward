package gfos.detailView;

import gfos.pojos.Application;
import gfos.database.ApplicationDatabaseService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

// Controller für weitere Bewerbungen
// Kaum bis gar nicht benutzt

@Named
@ViewScoped
public class ExternalApplicationDetail implements Serializable {
    @Inject
    ApplicationDatabaseService adbs;

    private Application application;

    private int offerId = -1;
    private int applicantId = -1;
    private boolean initialized = false;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
        if (applicantId != -1 && !initialized) {
            application = adbs.getById(offerId, applicantId);
            initialized = true;
        }
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
        if (offerId != -1 && !initialized) {
            application = adbs.getById(offerId, applicantId);
            initialized = true;
        }
    }
}
