package gfos.detailView;

import gfos.pojos.Employee;
import gfos.pojos.Regexes;
import gfos.pojos.Applicant;
import gfos.pojos.ResourceIO;
import gfos.database.ApplicantDatabaseService;
import gfos.exceptions.UploadException;
import gfos.beans.CurrentUser;
import gfos.beans.GeoCalculator;
import org.primefaces.model.file.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.*;

@Named
@ViewScoped
public class DetailApplicant implements Serializable {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;
    // <p:graphicImage value="#{detailApplicant.pb}" styleClass="image-profile" />

    public String isApplicant() {
        if (cu.getCurrentUser() instanceof Applicant) {
            return "";
        }
        return "/00-loginRegistration/login.xhtml";
    }

    private Applicant detailApplicant = new Applicant();

    public void init() {
        setDetailApplicant((Applicant) (cu.getCurrentUser()));
        formerName = detailApplicant.getName();
        formerEmail = detailApplicant.getEmail();
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

    private String formerName, formerEmail;

    private String emailRepeat;
    private String street, zip, city;
    private UploadedFile cv;

    private HashMap<String, String> errorMsgs =new HashMap<>();

    private boolean changingError = false;

    public String getErrorMessage(String name) {
        return errorMsgs.get(name) == null ? "" : errorMsgs.get(name);
    }

    public String updateProfile() {
        errorMsgs.clear();
        System.out.println("UPDATE");
        changingError = false;
        //Kein Vorname
        if (detailApplicant.getFirstname().isEmpty()) {
            changingError = true;
            errorMsgs.put("firstname", "Vorname ist erforderlich.");
        }
        //Kein Nachname
        if (detailApplicant.getLastname().isEmpty()) {
            changingError = true;
            errorMsgs.put("lastname", "Nachname ist erforderlich.");
        }
        //Keine Email
        if (detailApplicant.getEmail().isEmpty()) {
            changingError = true;
            errorMsgs.put("email", "Es muss eine E-Mail Adresse angegeben werden.");
        }
        //E-Mail existiert schon
        if (!formerEmail.equals(detailApplicant.getEmail()) && adbs.emailExists(detailApplicant.getEmail())) {
            changingError = true;
            errorMsgs.put("email", "Diese E-Mail Adresse wird bereits verwendet.");
        }
        //Keine korrekte E-Mail
        if (!Pattern.compile(Regexes.email).matcher(detailApplicant.getEmail()).find() && !detailApplicant.getEmail().isEmpty()) {
            changingError = true;
            errorMsgs.put("email", "Diese E-Mail ist keine korrekte E-Mail Adresse.");
        }
        //E-Mails nicht gleich
        if (!emailRepeat.isEmpty() &&  !emailRepeat.equals(detailApplicant.getEmail())) {
            changingError = true;
            errorMsgs.put("emailRepeat", "E-Mail Adressen stimmen nicht überein.");
        }
        //Benutzername wird schon verwendet
        if (!formerName.equals(detailApplicant.getName())) {
            if (adbs.nameExists(detailApplicant.getName()) || detailApplicant.getName().equals("sudo") ) {
                changingError = true;
                errorMsgs.put("name", "Dieser Benutzername wird bereits verwendet.");
            }
        }

        double[] newCoords = new double[2];
        //Nicht alle Teile der Adresse angegeben
        if (!(street.isEmpty() && zip.isEmpty() && city.isEmpty())) {
            if (street.isEmpty() || zip.isEmpty() || city.isEmpty()) {
                changingError = true;
                errorMsgs.put("location", "Es müssen alle Bestandteile der Adresse angegeben werden.");
                return"";
            }
            String location = street + " " + zip + " " + city;

            newCoords= GeoCalculator.getCoordinates(location);
        }

        if(changingError) {
            return "";
        }

        updatePb(detailApplicant);

        detailApplicant.setLat(newCoords[0]);
        detailApplicant.setLon(newCoords[1]);
        adbs.update(detailApplicant);
        cu.setCurrentUser(detailApplicant);

        return "/01-user/userProfile.xhtml?faces-redirect=true";
    }

    private void updatePb(Applicant a) {
        try {
            String pbDir = ResourceIO.uploadPb(cv, a);
            a.setPb(pbDir);
        } catch (IOException ioException) {
            System.out.println("There was an internal error while saving pb file: "+ ioException.getMessage());
        } catch (UploadException uploadException) {
            System.out.println("There was an error while saving pb file: " + uploadException.getMessage());
        }
    }

    public String delete() {
        adbs.delete(detailApplicant);
        cu.setCurrentUser(null);
        return "/00-loginRegistration/login.xhtml?faces-redirect=true";
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

    public UploadedFile getCv() {
        return cv;
    }

    public void setCv(UploadedFile cv) {
        this.cv = cv;
    }
}
