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
            Resume r = ResourceIO.uploadResume(cv, offer, cu.getCurrentUser());
            Application a = new Application(
                    cu.getCurrentUser().getId(),
                    offer.getId(),
                    text,
                    0,
                    r.getId()
            );

            adbs.apply(a, r);

        } catch (IOException ioException) {
            System.out.println("Could not save file: " + ioException.getMessage());
        } catch (UploadException uploadException) {
            System.out.println(uploadException.getMessage());
        }

        return ""; // Succespage.xhtml?faces-redirect=true
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

