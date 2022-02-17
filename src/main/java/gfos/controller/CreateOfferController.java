package gfos.controller;

import gfos.beans.Company;
import gfos.longerBeans.CurrentUser;

import javax.inject.Inject;

public class CreateOfferController {
    private String title;
    private String description;
    private String tag;
    private String category;

    private String street;
    private String postalCode;
    private String city;

    @Inject
    CurrentUser cu;

    public String save() {
        return "";
    }

    public String checkAdminRights() {
        if (cu.getCurrentUser() instanceof Company){
            return "";
        } else {
            return "index.xhtml"; // *Main page* for normal user OR maybe login page?
        }
    }
}
