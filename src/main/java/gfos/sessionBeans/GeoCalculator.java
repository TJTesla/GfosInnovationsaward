package gfos.sessionBeans;

import com.google.gson.*;
import gfos.Env;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class GeoCalculator {
    // Use the Geoapify API to geocode the address to latitude and longitude
    public static double[] getCoordinates(String location) {
        location = location.replace(" ", "%20");

        HttpResponse<JsonNode> response = Unirest
                .get("https://api.geoapify.com/v1/geocode/search?text=" + location + "&lang=de&limit=1&type=street&format=json&apiKey=" + Env.geoapifyKey)
                .asJson();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response.getBody().getObject().toString());

        JsonObject street = element.getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();

        double[] result = new double[2];
        result[0] = street.get("lat").getAsBigDecimal().doubleValue();
        result[1] = street.get("lon").getAsBigDecimal().doubleValue();

        return result;
    }

    // Use Haversine formula to calculate distance between two points on earth
    // Taken from: https://stackoverflow.com/a/27943
    public static double distance(double[] loc1, double[] loc2)  {
        int r = 6371; // Radius of earth in km
        double dLat = deg2rad(loc2[0] - loc1[0]); // Delta for latitude
        double dLon = deg2rad(loc2[1] - loc1[1]); // Delta for longitude

        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(loc1[0])) * Math.cos(deg2rad(loc2[0])) *
                Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return r * c;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
