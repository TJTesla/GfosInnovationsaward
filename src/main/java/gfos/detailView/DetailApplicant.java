package gfos.detailView;

import gfos.beans.Applicant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DetailApplicant implements Serializable {
    private Applicant detailApplicant = new Applicant();

    public Applicant getDetailApplicant() {
        return detailApplicant;
    }

    public void setDetailApplicant(Applicant detailApplicant) {
        this.detailApplicant = detailApplicant;
    }
}
