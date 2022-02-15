package gfos.controller;

import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class OfferController {
    private Offer detailOffer = new Offer();

    public Offer getDetailOffer() {
        return detailOffer;
    }

    public void setDetailOffer(Offer detailOffer) {
        this.detailOffer = detailOffer;
    }
}
