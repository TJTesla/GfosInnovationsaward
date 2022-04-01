package gfos.controller;

import gfos.beans.Offer;
import gfos.longerBeans.CurrentUser;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@ViewScoped
public class ApplicationListController {
    @Inject
    CurrentUser cu;

    public ArrayList<Offer> appliedOffers() {

    }
}
