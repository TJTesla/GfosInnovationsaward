package gfos.controller;

import gfos.database.ApplicationDatabaseService;
import gfos.exceptions.UploadException;
import gfos.beans.CurrentUser;
import gfos.pojos.*;
import org.primefaces.model.file.UploadedFile;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.util.HashMap;

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
    private HashMap<String, String> errMsgs = new HashMap<>();
    boolean applicationError;

    public String getErrorMsg(String name) {
        return errMsgs.get(name) == null ? "" : errMsgs.get(name);
    }

    public String apply(boolean draft) {
        applicationError = false;
        if (adbs.getById(offer.getId(), ((Applicant)cu.getCurrentUser()).getId()) != null) {
            return editAction(draft);
        }
        System.out.println("APPLY");
        if (cv == null) {
            System.out.println("No CV uploaded");
            errMsgs.put("cv", "Es muss ein Lebenslauf hochgeladen werden.");
            applicationError = true;
        } else if (cv.getSize() > 50000) {
            errMsgs.put("cv", "Die Maximalgröße sind 5MB");
        }
        if (text.isEmpty()) {
            errMsgs.put("text", "Es muss ein Motivationsschreiben vorliegen.");
            applicationError = true;
        }
        if (applicationError) {
            return "";
        }
        int applicationId = -1;
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

            applicationId = adbs.apply(a, r);
            if (applicationId == -1) {
                return "";
            }
        } catch (IOException ioException) {
            System.out.println("Could not save file: " + ioException.getMessage());
        } catch (UploadException uploadException) {
            System.out.println(uploadException.getMessage());
        }

        return "/03-application/applDetailUser.xhtml?id=" + applicationId + "&faces-redirect=true"; // Succespage.xhtml?faces-redirect=true
    }

    private String editAction(boolean draft) {
        Resume r = null;
        if (cv != null) {
            try {
                r = ResourceIO.uploadResume(cv, offer, cu.getCurrentUser());
            } catch (Exception e) {
                System.out.println("Could not update resume: " + e.getMessage());
            }
        }

        int applicationId = adbs.update( ((Applicant)cu.getCurrentUser()).getId(), offer.getId(), text, r, draft);

        return "/03-application/applDetailUser.xhtml?id=" + applicationId + "&faces-redirect=true";
    }

    public String checkUserRights() {
        if (cu.getCurrentUser() instanceof Applicant) {
            return "";
        }
        return "/00-loginRegistration/login.xhtml";
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

