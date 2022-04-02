package gfos.detailView;

import gfos.beans.*;
import gfos.database.ApplicantDatabaseService;
import gfos.database.ApplicationDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
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

    public DetailApplication() {
    }

    public DetailApplication(Application a, ApplicationDatabaseService aDB, ApplicantDatabaseService uDB, OfferDatabaseService oDB) {
        this.setApplication(a, uDB, oDB);

        this.adbs = aDB;
        this.udbs = uDB;
        this.odbs = oDB;
    }

    private Application application;
    private Applicant applicant;
    private Offer offer;

    public String statusString() {
        return adbs.getStatusString(application.getStatus());
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
        if (application == null) {
            return;
        }
        this.application = application;
        applicant = udbs.getById(this.application.getUserId());
        offer = odbs.getById(this.application.getOfferId());
    }

    public void setApplication(Application application, ApplicantDatabaseService aDB, OfferDatabaseService oDB) {
        if (application == null) {
            return;
        }
        this.application = application;
        applicant = aDB.getById(this.application.getUserId());
        offer = oDB.getById(this.application.getOfferId());
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
}
