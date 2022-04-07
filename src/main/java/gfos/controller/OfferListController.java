package gfos.controller;

import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

@Named
@ViewScoped
public class OfferListController implements Serializable {
    @Inject
    OfferDatabaseService odbs;

    public ArrayList<Offer> getAllOffers() {
        return null;
    }
}
