package gfos.detailView;

import gfos.pojos.Applicant;
import gfos.pojos.Employee;
import gfos.pojos.Offer;
import gfos.database.ApplicantDatabaseService;
import gfos.database.OfferDatabaseService;
import gfos.beans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

// Controller zum Anzeigen eines Angebots

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

    // bearbeiten des Status als Favorit (siehe IndexController für weitere Erklärung)
    public void editFavorite() {
        int id = detailOffer.getId();
        if (isFavorite()) {
            removeFromFavorites(id);
        } else {
            setAsFavorite(id);
        }
    }

    // Methode zum verstecken eines Feldes für optionale Werte
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

    // veröffentlichen
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
