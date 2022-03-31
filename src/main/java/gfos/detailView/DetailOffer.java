package gfos.detailView;

import gfos.beans.Applicant;
import gfos.beans.Employee;
import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DetailOffer implements Serializable {
    @Inject
    OfferDatabaseService odbs;
    @Inject
    CurrentUser cu;

    private Offer detailOffer = new Offer();

    public String fieldString() {
        return odbs.getField(detailOffer.getField());
    }

    public String levelString() {
        return odbs.getLevel(detailOffer.getLevel());
    }

    public String timeString() {
        return odbs.getTime(detailOffer.getTime());
    }

    public boolean loggedInAsApplicant() {
        return cu.getCurrentUser() instanceof Applicant;
    }

    public boolean loggedInAsEmployee() {
        return cu.getCurrentUser() instanceof Employee;
    }

    public Offer getDetailOffer() {
        return detailOffer;
    }

    public void setDetailOffer(Offer detailOffer) {
        this.detailOffer = detailOffer;
    }
}
