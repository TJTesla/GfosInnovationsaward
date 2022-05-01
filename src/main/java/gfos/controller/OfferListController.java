package gfos.controller;

import gfos.pojos.Offer;
import gfos.database.OfferDatabaseService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

// Wahrscheinlich ungenutzte Klasse aber da keine Sicherheit und keine zeit zum testen da ist
// Wurde sie noch nicht gelöscht

@Named
@ViewScoped
public class OfferListController implements Serializable {
    @Inject
    OfferDatabaseService odbs;

    public ArrayList<Offer> getAllOffers() {
        return null;
    }
}
