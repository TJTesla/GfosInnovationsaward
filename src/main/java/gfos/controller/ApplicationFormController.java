package gfos.controller;

import gfos.beans.Offer;
import gfos.sessionBeans.CurrentUser;
import org.primefaces.model.file.UploadedFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;

@Named
public class ApplicationFormController {
    @Inject
    CurrentUser cu;

    private Offer offer;
    UploadedFile cv;

    public String apply() {
        // Don't allow other file types than pdf to be uploaded
        // Prevent upload of possibly malware in form of .exe files etc.
        // Don't allow paths as name for file
        // To save would need to mkdir -> Could get too much
        if (cv == null || isPDF(cv.getFileName()) || cv.getFileName().contains("/")) {
            return "";
        }
        // Save cv (in DB and filesystem)
        String cvPath = "resumes/" + cu.getCurrentUser().getId() + "/" + cv.getFileName();
        // Create File object in current working subdirectory
        File file = new File(System.getProperty("user.dir"), cvPath);
        file.mkdirs();

        try {
            InputStream is = cv.getInputStream();
            OutputStream os = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        } catch(IOException ioException) {
            System.out.println("Could not save file: " + ioException.getMessage());
            return "";
        }

        // Create and save application


        return ""; // Succespage.xhtml?faces-redirect=true
    }

    private boolean isPDF(String fileName) {
        String fileType = fileName.substring(fileName.length()-4).toLowerCase();
        return fileType.equals(".pdf");
    }
}

