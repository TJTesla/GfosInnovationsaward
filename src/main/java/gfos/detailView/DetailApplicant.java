package gfos.detailView;

import gfos.beans.Applicant;
import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

@Named
@ViewScoped
public class DetailApplicant implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;
    // <p:graphicImage value="#{detailApplicant.pb}" styleClass="image-profile" />

    private Applicant detailApplicant = new Applicant();

    public void init() {
        setDetailApplicant((Applicant) (cu.getCurrentUser()));
    }

    public String salutationString() {
        return adbs.getSalutation(detailApplicant.getGender());
    }

    public Applicant getDetailApplicant() {
        return detailApplicant;
    }

    public void setDetailApplicant(Applicant detailApplicant) {
        this.detailApplicant = detailApplicant;
    }

    private String emailRepeat;

    private boolean changingError = false;
    public String updateProfile() {
        System.out.println("UPDATE");
        changingError = false;
        if (emailRepeat != null &&  !emailRepeat.equals(detailApplicant.getEmail())) {
            changingError = true;
            return "";
        }

        adbs.update(detailApplicant);
        return "/01-user/userProfile.xhtml";
    }

    public String getEmailRepeat() {
        return emailRepeat;
    }

    public void setEmailRepeat(String emailRepeat) {
        this.emailRepeat = emailRepeat;
    }

    public boolean isChangingError() {
        return changingError;
    }

    public void setChangingError(boolean changingError) {
        this.changingError = changingError;
    }
}
