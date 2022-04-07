package gfos.detailView;

import gfos.beans.Applicant;
import gfos.beans.Application;
import gfos.beans.Employee;
import gfos.beans.Offer;
import gfos.database.ApplicantDatabaseService;
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
    ApplicantDatabaseService adbs;
    @Inject
    CurrentUser cu;

    private Offer detailOffer = new Offer();

    public boolean alreadyApplied() {
        if (!loggedInAsApplicant()) {
            return false;
        }

        return odbs.alreadyApplied( ((Applicant)cu.getCurrentUser()).getId(), detailOffer.getId());
    }

    public boolean isFavorite() {
        return adbs.isFavorite( ((Applicant)cu.getCurrentUser()).getId(), detailOffer.getId());
    }

    public String delete() {
        odbs.delete(detailOffer);
        return "/index.xhtml?faces-redirect=true";
    }

    public void editFavorite() {
        int id = detailOffer.getId();
        if (isFavorite()) {
            removeFromFavorites(id);
        } else {
            setAsFavorite(id);
        }
    }

    public boolean hasExtraInfos() {
        return detailOffer.getExtras() != null && !detailOffer.getExtras().isEmpty();
    }

    private void setAsFavorite(int id) {
        adbs.addToFavorites( ((Applicant)cu.getCurrentUser()).getId(), id);
    }

    private void removeFromFavorites(int id) {
        adbs.removeFromFavorites( ((Applicant)cu.getCurrentUser()).getId(), id);
    }

    public boolean isFinalOffer() {
        return !detailOffer.getDraft();
    }

    public void publish() {
        detailOffer.setDraft(false);
        odbs.publish(detailOffer);
    }

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
