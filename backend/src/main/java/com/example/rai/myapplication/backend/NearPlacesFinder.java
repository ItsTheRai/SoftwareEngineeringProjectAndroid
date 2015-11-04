package com.example.rai.myapplication.backend;

import com.example.rai.myapplication.backend.model.SalesLocationData;
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
        IndexSpec indexSpec = IndexSpec.newBuilder().setName("INDEX_NAME")
                .build();
        return SearchServiceFactory.getSearchService().getIndex(indexSpec);
    }

    /**
     * Builds a new Place document to insert in the Places index.
     * @param placeId      the identifier of the place in the database.
     * @param placeName    the name of the place.
//     * @param placeAddress the address of the place.
     * @param location     the GPS location of the place, as a GeoPt.
     * @return the Place document created.
     */
    public static Document buildDocument(
            final Long placeId, final String placeName,
            final int priceInPouds, final GeoPt location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),
                location.getLongitude());

        Document.Builder builder = Document.newBuilder()
                .addField(Field.newBuilder().setName("id")
                        .setText(placeId.toString()))

                .addField(Field.newBuilder().setName("transactionID").setText(placeName))

                .addField(Field.newBuilder().setName("price")
                        .setText(String.valueOf(priceInPouds)))

                .addField(Field.newBuilder().setName("place_location")
                        .setGeoPoint(geoPoint));

        // geo-location doesn't work under dev_server, so let's add another
        // field to use for retrieving documents
//        if (environment.value() == Development) {
//            builder.addField(Field.newBuilder().setName("value").setNumber(1));
//        }

        return builder.build();
    }

    /**
     * Returns the nearest places to the location of the user.
     * @param location the location of the user.
     * @param distanceInMeters the maximum distance to the user.
     * @param resultCount the maximum number of places returned.
     * @return List of up to resultCount places in the datastore ordered by
     *      the distance to the location parameter and less than
     *      distanceInMeters meters to the location parameter.
     */
    public static List<SalesLocationData> getPlaces(final GeoPt location,
                                            final float distanceInMeters, final int resultCount) {

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
                        .setDefaultValueNumeric(distanceInMeters + 1))
                .setLimit(resultCount)
                .build();
        // Build the QueryOptions
        QueryOptions options = QueryOptions.newBuilder()
                .setSortOptions(sortOptions)
                .build();
        // Query string
        String searchQuery = "distance(place_location, " + geoPoint + ") < "
                + distanceInMeters;

        Query query = Query.newBuilder().setOptions(options).build(searchQuery);

        Results<ScoredDocument> results = getIndex().search(query);

        if (results.getNumberFound() == 0) {
            // geo-location doesn't work under dev_server
//            if (environment.value() == Development) {
//                // return all documents
//                results = getIndex().search("value > 0");
//            }
        }

        List<SalesLocationData> places = new ArrayList<>();
//        SalesLocationDataCollection places = new SalesLocationDataCollection();

        for (ScoredDocument document : results) {
            if (places.size() >= resultCount) {
                break;
            }

            GeoPoint p = document.getOnlyField("place_location").getGeoPoint();

            SalesLocationData place = new SalesLocationData();
            place.setId(Long.valueOf(document.getOnlyField("id")
                    .getText()));
            place.setPriceInPouds(Integer.parseInt(document.getOnlyField("price").getText()));  //parse string data to an int
//            place.set.setName(document.getOnlyField("name").getText());
//            place.setAddress(document.getOnlyField("address").getText());
            //set up location
            place.setLocation(new GeoPt((float) p.getLatitude(),
                    (float) p.getLongitude()));

            // GeoPoints are not implemented on dev server and latitude and
            // longitude are set to zero
            // But since those are doubles let's play safe
            // and use double comparison with epsilon set to EPSILON
//            if (Math.abs(p.getLatitude()) <= EPSILON
//                    && Math.abs(p.getLongitude()) <= EPSILON) {
//                // set a fake distance of 5+ km
//                place.setDistanceInKilometers(FAKE_DISTANCE_FOR_DEV + places
//                        .size());
//            } else {
//                double distance = distanceInMeters / METERS_IN_KILOMETER;
//                try {
//                    distance = getDistanceInKm(
//                            p.getLatitude(), p.getLongitude(),
//                            location.getLatitude(),
//                            location.getLongitude());
//                } catch (Exception e) {
//                    LOG.warning("Exception when calculating a distance: " + e
//                            .getMessage());
//                }
//
//                place.setDistanceInKilometers(distance);
//            }

            places.add(place);
        }
        return places;
    }
}
