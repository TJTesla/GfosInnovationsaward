package gfos.controller;

import gfos.beans.*;
import gfos.database.ApplicationDatabaseService;
import gfos.exceptions.UploadException;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.file.UploadedFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;

@Named
public class ApplicationFormController {
    @Inject
    CurrentUser cu;
    @Inject
    ApplicationDatabaseService adbs;

    private Offer offer;
    UploadedFile cv;
    private String text;

    public String apply() {
        try {
            Applicant currentApplicant = (Applicant)(cu.getCurrentUser());
            Resume r = ResourceIO.uploadResume(cv, offer, currentApplicant);
            Application a = new Application(
                    currentApplicant.getId(),
                    offer.getId(),
                    text,
                    0,
                    0
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
        if (cu.getCurrentUser() == null || cu.getCurrentUser() instanceof Employee) {
            return "/login.xhtml";
        }
        return "";
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
}

