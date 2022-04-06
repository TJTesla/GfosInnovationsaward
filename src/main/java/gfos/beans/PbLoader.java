package gfos.beans;

import gfos.database.ApplicantDatabaseService;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
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

    public StreamedContent load(int id) {
        return loadAlg(adbs.getById(id));
    }

    public StreamedContent loadWithContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        String id = context.getExternalContext().getRequestParameterMap().get("id");
        return load( Integer.parseInt(id) );
    }

    private StreamedContent loadAlg(Applicant a) {
        String file = a.getPb();
        if (file == null || file.isEmpty()) {
            file = "uploads/profilepics/applicants/default.jpg";
        }

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
