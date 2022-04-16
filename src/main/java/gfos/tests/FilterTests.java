package gfos.tests;

import gfos.pojos.FilterObject;
import gfos.pojos.Offer;
import gfos.database.OfferDatabaseService;

import java.sql.SQLException;
import java.util.ArrayList;

public class FilterTests {
    static OfferDatabaseService odbs;

    static {
        try {
            odbs = new OfferDatabaseService();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    private static void print(ArrayList<Offer> list) {
        System.out.println("Size:" +  list.size() + ": ");

        for (Offer o : list) {
            System.out.print(o.getTitle() + "; ");
        }
        System.out.println();

        System.out.println("-----------------");
    }
}
