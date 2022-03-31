package gfos.tests;

import gfos.beans.FilterObject;
import gfos.beans.Offer;
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
        // Everything
        print(odbs.fetchAll(
                new FilterObject(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        ));

        // EVERYTHING
        ArrayList<Integer> field = new ArrayList<>();
        field.add(5);
        field.add(3);
        print(odbs.fetchAll(
                new FilterObject(
                        field,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        ));

        // Only Sr Dev
        ArrayList<Integer> time = new ArrayList<Integer>();
        time.add(2);
        print(odbs.fetchAll(
                new FilterObject(
                        null,
                        null,
                        time,
                        null,
                        null,
                        null
                ),
                null
        ));

        // Sr Dev
        ArrayList<Integer> level = new ArrayList<Integer>();
        level.add(6);
        print(odbs.fetchAll(
                new FilterObject(
                        null,
                        level,
                        time,
                        null,
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
