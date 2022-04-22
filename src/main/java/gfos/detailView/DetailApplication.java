package gfos.detailView;

import gfos.database.ApplicantDatabaseService;
import gfos.database.ApplicationDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.beans.CurrentUser;
import gfos.pojos.*;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class DetailApplication implements Serializable {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicationDatabaseService adbs;
    @Inject
    ApplicantDatabaseService udbs;
    @Inject
    OfferDatabaseService odbs;

    private int aId = -1;
    private int oId = -1;
    private boolean initialized = false;

    public DetailApplication() {
    }

    public DetailApplication(Application a, ApplicationDatabaseService aDB, ApplicantDatabaseService uDB, OfferDatabaseService oDB) {
        this.setApplicationWithParams(a, uDB, oDB);

        this.adbs = aDB;
        this.udbs = uDB;
        this.odbs = oDB;
    }

    private Application application;
    private Applicant applicant;
    private Offer offer;

    private String status;

    public String statusString() {
        return adbs.getStatusString(application.getStatus());
    }

    public String statusString(Application a) {
        return adbs.getStatusString(a.getStatus());
    }

    public String salutationString() {
        return udbs.getSalutation(applicant.getGender());
    }

    public ArrayList<String> titles() {
        return udbs.titles(applicant.getId());
    }

    public StreamedContent resume() {
        Resume resume = adbs.getResume(application.getResumeId());
        if (resume == null) {
            return null;
        }

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resume.getPath()));

            return DefaultStreamedContent.builder()
                    .name(resume.getName())
                    .contentType("application/pdf")
                    .stream(() -> bis)
                    .build();
        } catch (FileNotFoundException fnfE) {
            System.out.println("Could not download resume " + resume.getId() + ": " + fnfE.getMessage());
            return null;
        }
    }

    public String retract() {
        if (isDraft() || application.getStatus() != 0) {
            return null;
        }

        adbs.changeVisibility(this.application);
        return "";
    }

    public String delete() {
        adbs.delete(this.application);
        return "/03-applicationList/applicationDrafts?faces-redirect=true";
    }

    public boolean isDraft() {
        return this.application.getDraft();
    }

    public String checkEmployee() {
        if (cu.getCurrentUser() != null &&
                cu.getCurrentUser() instanceof Employee
        ) {
            return "";
        }
        return "/00-loginRegistration/login.xhtml";
    }

    public String checkApplicant() {
        if (cu.getCurrentUser() instanceof Applicant) {
            return "";
        }
        return "/00-loginRegistration/login.xhtml";
    }

    public String changeStatus() {
        application.setStatus( toInt(status) );
        adbs.changeStatus(application);
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
        if (application == null) {
            return;
        }
        this.application = application;
        applicant = udbs.getById(this.application.getUserId());
        offer = odbs.getById(this.application.getOfferId());
        status = String.valueOf(application.getStatus());
    }

    public void setApplicationWithParams(Application application, ApplicantDatabaseService aDB, OfferDatabaseService oDB) {
        if (application == null) {
            return;
        }
        this.application = application;
        applicant = aDB.getById(this.application.getUserId());
        offer = oDB.getById(this.application.getOfferId());
        status = String.valueOf(application.getStatus());
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;

        if (oId != -1 && !initialized) {
            this.setApplication(adbs.getById(oId, aId));
            initialized = true;
        }
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;

        if (aId != -1 && !initialized) {
            this.setApplication(adbs.getById(oId, aId));
            initialized = true;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
