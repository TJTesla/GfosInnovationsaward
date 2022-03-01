package gfos.tests;

import gfos.FilterObject;
import gfos.beans.Offer;
import gfos.database.OfferDatabaseService;

import javax.inject.Inject;
import java.lang.reflect.Array;
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
        // Everything
        print(odbs.fetchAll(
                new FilterObject(
                        null,
                        null,
                        null,
                        null
                ),
                null
        ));

        // EVERYTHING
        ArrayList<String> field = new ArrayList<String>();
        field.add("5");
        field.add("3");
        print(odbs.fetchAll(
                new FilterObject(
                        field,
                        null,
                        null,
                        null
                ),
                null
        ));

        // Only Sr Dev
        ArrayList<String> time = new ArrayList<String>();
        time.add("2");
        print(odbs.fetchAll(
                new FilterObject(
                        null,
                        null,
                        time,
                        null
                ),
                null
        ));

        // Only Build a pc
        ArrayList<String> level = new ArrayList<String>();
        level.add("1");
        print(odbs.fetchAll(
                new FilterObject(
                        null,
                        level,
                        null,
                        null
                ),
                null
        ));
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
