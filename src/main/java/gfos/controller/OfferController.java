package gfos.controller;

import gfos.beans.Offer;

import javax.inject.Named;

@Named
public class OfferController {
    private Offer detailOffer;

    public Offer getDetailOffer() {
        return detailOffer;
    }

    public void setDetailOffer(Offer detailOffer) {
        this.detailOffer = detailOffer;
    }
}
