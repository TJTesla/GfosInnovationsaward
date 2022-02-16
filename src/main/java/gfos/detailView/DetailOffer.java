package gfos.detailView;

import gfos.beans.Offer;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DetailOffer implements Serializable {
    private Offer detailOffer = new Offer();

    public Offer getDetailOffer() {
        return detailOffer;
    }

    public void setDetailOffer(Offer detailOffer) {
        this.detailOffer = detailOffer;
    }
}
