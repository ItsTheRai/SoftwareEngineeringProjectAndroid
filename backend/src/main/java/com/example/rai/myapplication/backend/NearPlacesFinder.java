package com.example.rai.myapplication.backend;

import com.example.rai.myapplication.backend.model.SalesData;
import com.example.rai.myapplication.backend.model.SalesDataShort;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rai on 03/11/15.
 */
public class NearPlacesFinder {

    private NearPlacesFinder(){
    }

    /**
     * Returns the Places index in the datastore.
     * @return The index to use to search places in the datastore.
     */
    public static Index getIndex() {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName("Places")
                .build();
        return SearchServiceFactory.getSearchService().getIndex(indexSpec);
    }

    /**
     * Builds a new Place document to insert in the Places index.
     * @param placeId      the identifier of the place in the database.
//     * @param placeAddress the address of the place.
     * @param location     the GPS location of the place, as a GeoPt.
     * @return the Place document created.
     */
    public static Document buildDocument(
            final Long placeId, final String postcode,
            final int priceInPouds, final GeoPt location) {
        GeoPoint geoPoint = new GeoPoint((double)location.getLatitude(),
                (double)location.getLongitude());

        Document.Builder builder = Document.newBuilder()
                .addField(Field.newBuilder().setName("place_location")
                        .setGeoPoint(geoPoint))

            .addField(Field.newBuilder().setName("id")
                        .setText(placeId.toString()))

                .addField(Field.newBuilder().setName("postcode").setText(postcode))

                .addField(Field.newBuilder().setName(String.valueOf(priceInPouds))
                        .setText(String.valueOf(priceInPouds)));

        return builder.build();
    }

    /**
     * Returns the nearest places to the location of the user.
     * @param location the location of the user.
     * @param distanceInMiles the maximum distance to the user.
     * @param resultCount the maximum number of places returned.
     * @return List of up to resultCount places in the datastore ordered by
     *      the distance to the location parameter and less than
     *      distanceInMeters meters to the location parameter.
     */
    public static List<SalesData> getPlaces(final GeoPt location,
                                            final long distanceInMiles, final int resultCount) {

        // Optional: use memcache

        String geoPoint = "geopoint(" + location.getLatitude() + ", " + location
                .getLongitude()
                + ")";
        String locExpr = "distance(place_location, " + geoPoint + ")";

        // Build the SortOptions with 2 sort keys
        SortOptions sortOptions = SortOptions.newBuilder()
                .addSortExpression(SortExpression.newBuilder()
                        .setExpression(locExpr)
                        .setDirection(SortExpression.SortDirection.ASCENDING)
                        .setDefaultValueNumeric(distanceInMiles + 1))
                .setLimit(resultCount)
                .build();
        // Build the QueryOptions
        QueryOptions options = QueryOptions.newBuilder()
                .setSortOptions(sortOptions)
                .build();
        // Query string
        String searchQuery = "distance(place_location, " + geoPoint + ") < "
                + distanceInMiles;//distance mile metres

        Query query = Query.newBuilder().setOptions(options).build(searchQuery);

        Results<ScoredDocument> results = getIndex().search(query);

        List<SalesData> places = new ArrayList<>();

        for (ScoredDocument document : results) {
            if (places.size() >= resultCount) {
                break;
            }

            GeoPoint p = document.getOnlyField("place_location").getGeoPoint();

            SalesData place = new SalesData();

            place.setId(Long.valueOf(document.getOnlyField("id")
                    .getText()));

            place.setPrice(Integer.parseInt(document.getOnlyField("price").getText()));  //parse string data to an int

            place.setLocation(new GeoPt((float) p.getLatitude(),
                    (float) p.getLongitude()));
            places.add(place);
        }
        return places;
    }
}
