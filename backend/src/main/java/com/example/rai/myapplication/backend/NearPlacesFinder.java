package com.example.rai.myapplication.backend;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
//import com.google.android.gms.maps.model.*;
//import com.google.maps.android.heatmaps.WeightedLatLng;
//import com.google.maps.android.heatmaps.WeightedLatLng;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import oracle.spatial.geometry.JGeometry;


/**
 * Created by rai on 03/11/15.
 */
public class NearPlacesFinder {

    private static final double EARTH_RADIUS = 6378.1;
    private static final int RESULTS_LIMIT = 1000;
    private static String cursor;
    public NearPlacesFinder(){
    }

    /**
     * Returns the Places index in the datastore.
     * @return The index to use to search places in the datastore.
     */
    public static Index getIndex() {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName("PresentationIndex")
                .build();
        return SearchServiceFactory.getSearchService().getIndex(indexSpec);
    }

    /**
     * Builds a new Place document to insert in the Places index.
     * @param placeId      the identifier of the place in the database.
//     * @param placeAddress the address of the place.
     * @param priceInPouds
     *@param location     the GPS location of the place, as a GeoPt.  @return the Place document created.
     */
    public static Document buildDocument(
            final Long placeId, final double priceInPouds,final GeoPoint location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),
                location.getLongitude());

        Document.Builder builder = Document.newBuilder()
                .addField(Field.newBuilder().setName("place_location")
                        .setGeoPoint(geoPoint))

                .addField(Field.newBuilder().setName("id")
                        .setText(placeId.toString()))
                .addField(Field.newBuilder().setName("price")
                                .setText(String.valueOf(priceInPouds))
                )
         ;
        return builder.build();
    }

    /**
     * Returns the nearest places to the location of the user.
//     * @param location the location of the user.
     * @param distanceInMeters the maximum distance to the user.
     * @param resultCount the maximum number of places returned.
     * @return List of up to resultCount places in the datastore ordered by
     *      the distance to the location parameter and less than
     *      distanceInMeters meters to the location parameter.
     */
    public static List<List<Double>> getPlaces(final double latitud, final double longitud,
                                               final double distanceInMeters, final int resultCount) throws SQLException, ClassNotFoundException {
        //query string used
//        String url = null
//        Class.forName("com.mysql.jdbc.GoogleDriver");
//        url = "jdbc:google:mysql://software-engineering-1102:final-instance2/sales_data?user=root";
        int KM_CONSTANT = 6371;
        double latitude = latitud;//50.8252;
        double longitude = longitud;//-0.13824;
        String url = null;
//        if (SystemProperty.environment.value() ==
//                SystemProperty.Environment.Value.Production) {
        // Connecting from App Engine.
        // Load the class that provides the "jdbc:google:mysql://"
        // prefix.
        Class.forName("com.mysql.jdbc.GoogleDriver");
        url =
                "jdbc:google:mysql://software-engineering-1102:final-instance2/sales_data?user=root";


//        String query = "SELECT\n" +
//                "  Unique_ref, (\n" +
//                "    "+KM_CONSTANT+"* acos (" +
//                "      cos ( radians("+location.getLatitude()+") )" +
//                "      * cos( radians( Latitude ) )" +
//                "      * cos( radians( Longitude ) - radians("+location.getLongitude()+") )" +
//                "      + sin ( radians("+location.getLatitude()+") )" +
//                "      * sin( radians( Latitude ) )" +
//                "    )" +
//                "  ) AS distance" +
//                "FROM Data" +
//                "HAVING distance < "+distanceInMeters/1000+
//                "ORDER BY distance" +
//                "LIMIT 0 , 50;";

//        SELECT id, KM_CONSTANT* acos (cos ( radians(location.getLatitude()) )* cos( radians( lat ) )* cos( radians( lng ) - radians(location.getLongitude()) )+ sin ( radians(78.3232) )* sin( radians( lat ) ))) AS distance FROM markers HAVING distance < +distanceInMeters/1000 ORDER BY distance LIMIT 0 , 1000;


        //create rectangel for query
        double lon1 = (longitude - distanceInMeters / 1600 / Math.abs(Math.cos(latitude * Math.PI / 180.0) * 69));
        double lon2 = (longitude + distanceInMeters / 1600 / Math.abs(Math.cos(latitude * Math.PI / 180.0) * 69));
        double lat1 = (latitude - (distanceInMeters / 1600 / 69));
        double lat2 = (latitude + (distanceInMeters / 1600 / 69));
//        String mockquerySetPolygon ="set @poly= 'POLYGON((-0.1392 50.8200,-0.1390 50.8200,-0.1390 50.8300,-0.1392 50.8300,-0.1392 50.8200))';";
//      String mockquerySetPolygonOutside = "SET @poly =\n" +
//                "     'Polygon(("+lo1+" "+la1+"," +
//                "                 "+lo2+" "+la1+"," +
//                "                 "+lo2+" "+la2+"," +
//                "                 "+lo1+" "+la2+"," +
//                "                 "+lo1+" "+la1+"))';";
//        String mockqueryDB = "select X(Coords),Y(coords),avg(Price),Unique_ref from SalesData " +
//                "where MBRWithin(Coords,GeomFromText(@poly)) group by Postcode limit "+resultCount+";";

//        String mockquerySetPolygonInside = "SET @poly =\n" +
//                "     'Polygon(("+lon1+" "+lat1+"," +
//                "                 "+lon2+" "+lat1+"," +
//                "                 "+lon2+" "+lat2+"," +
//                "                 "+lon1+" "+lat2+"," +
//                "                 "+lon1+" "+lat1+"))';";
        String query = "select * from HeatmapData where latitude > " + lat1 +
                " and latitude < " + lat2 + " and longitude > " + lon1 + " and longitude < " + lon2 +
                " limit 5000;";
        Connection conn = DriverManager.getConnection(url);
//      conn.setNetworkTimeout(Executors.newFixedThreadPool(),);
        //test with mock queries
        ResultSet rs = conn.createStatement().executeQuery(query);
//            ResultSet rs = conn.createStatement().executeQuery(mockqueryDB);//coose qery to use
//            rs.getCursorName();
//        }
        //define return array
        List<List<Double>> places = new ArrayList<>();
//if(false) {
        while (rs.next()) {
//            double lat= ;
//            double lon = ;
//            double price = ;
            List<Double> s = new ArrayList<>();
            s.add(rs.getDouble("latitude"));
            s.add(rs.getDouble("longitude"));
            s.add(rs.getDouble("averagePrice"));
            places.add(s);
//        user.setId(rs.getString("postcode"));
            //put them in an object
//        float x = (float)rs.getDouble("longitude");
//        float y = (float)rs.getDouble("latitude");
//        GeoPt point = new GeoPt(y, x);

        }return places;
    }

    /**
     * Computes the geodesic between two GPS coordinates.
     * @param latitude1 the latitude of the first point.
     * @param longitude1 the longitude of the first point.
     * @param latitude2 the latitude of the second point.
     * @param longitude2 the longitude of the second point.
     * @return the geodesic distance between the two points, in kilometers.
     */
    public static double getDistanceInKm(
            final double latitude1, final double longitude1,
            final double latitude2, final double longitude2) {

        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        double long1 = Math.toRadians(longitude1);
        double long2 = Math.toRadians(longitude2);

        return EARTH_RADIUS * Math
                .acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
                        * Math.cos(lat2) * Math.cos(Math.abs(long1 - long2)));
    }
}
