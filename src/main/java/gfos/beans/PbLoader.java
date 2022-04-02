package gfos.beans;

import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Named
@RequestScoped
public class PbLoader {
    @Inject
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;

    public StreamedContent load() {
        if (!(cu.getCurrentUser() instanceof Applicant)) {
            return null;
        }
        return loadAlg( (Applicant)(cu.getCurrentUser()) );
    }

    public StreamedContent load(Applicant a) {
        return loadAlg(a);
    }

    private StreamedContent loadAlg(Applicant a) {
        String file = a.getPb();
        String fileType = file.substring(file.length()-3).toLowerCase();

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            return DefaultStreamedContent.builder()
                    .name(a.getName() + "profile_pic." + fileType)
                    .contentType("image/" + fileType)
                    .stream(() -> bis)
                    .build();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Could not find pb for user " + a.getId() + ": " + fnfe.getMessage());
            return null;
        }
    }
}
