package gfos.controller;

import gfos.beans.*;
import gfos.database.ApplicationDatabaseService;
import gfos.exceptions.UploadException;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.file.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;

@Named
@ViewScoped
public class ApplicationFormController implements Serializable {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicationDatabaseService adbs;

    private Offer offer;
    private UploadedFile cv;
    private String text;

    public String apply(boolean draft) {
        try {
            Applicant currentApplicant = (Applicant)(cu.getCurrentUser());
            Resume r = ResourceIO.uploadResume(cv, offer, currentApplicant);
            Application a = new Application(
                    currentApplicant.getId(),
                    offer.getId(),
                    text,
                    0,
                    0,
                    draft
            );

            adbs.apply(a, r);

        } catch (IOException ioException) {
            System.out.println("Could not save file: " + ioException.getMessage());
        } catch (UploadException uploadException) {
            System.out.println(uploadException.getMessage());
        }

        return ""; // Succespage.xhtml?faces-redirect=true
    }

    public String checkUserRights() {
        if (cu.getCurrentUser() instanceof Applicant) {
            return "";
        }
        return "/login.xhtml";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public UploadedFile getCv() {
        return cv;
    }

    public void setCv(UploadedFile cv) {
        this.cv = cv;
    }
}

