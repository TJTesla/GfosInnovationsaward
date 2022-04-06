package gfos.detailView;

import gfos.beans.Applicant;
import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;
import gfos.longerBeans.GeoCalculator;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
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
        formerName = detailApplicant.getName();
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

    private String formerName;

    private String emailRepeat;
    private String street, zip, city;

    private boolean changingError = false;

    public String updateProfile() {
        System.out.println("UPDATE");
        changingError = false;
        if (!emailRepeat.isEmpty() &&  !emailRepeat.equals(detailApplicant.getEmail())) {
            changingError = true;
            return "";
        }
        if (!formerName.equals(detailApplicant.getName())) {
            if (adbs.nameExists(detailApplicant.getName())) {
                changingError = true;
                return "";
            }
        }

        double[] newCoords = new double[2];
        if (!(street.isEmpty() && zip.isEmpty() && city.isEmpty())) {
            if (street.isEmpty() || zip.isEmpty() || city.isEmpty()) {
                changingError = true;
                return "";
            }
            String location = street + " " + zip + " " + city;

            newCoords= GeoCalculator.getCoordinates(location);
        }

        detailApplicant.setLat(newCoords[0]);
        detailApplicant.setLon(newCoords[1]);
        adbs.update(detailApplicant);
        cu.setCurrentUser(detailApplicant);
        return "/01-user/userProfile.xhtml?faces-redirect=true";
    }

    public String delete() {
        adbs.delete(detailApplicant);
        cu.setCurrentUser(null);
        return "/00-loginRegistration/login.xhtml";
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

    public ApplicantDatabaseService getAdbs() {
        return adbs;
    }

    public void setAdbs(ApplicantDatabaseService adbs) {
        this.adbs = adbs;
    }

    public CurrentUser getCu() {
        return cu;
    }

    public void setCu(CurrentUser cu) {
        this.cu = cu;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
