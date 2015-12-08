package com.example.rai.myapplication.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rai on 03/11/15.
 */
public class SQLDatabaseQueryHelper {

    private static final double EARTH_RADIUS = 6378.1;
    private static final java.lang.String CLASS_NAME = "com.mysql.jdbc.GoogleDriver";
    private static final String URL = "jdbc:google:mysql://software-engineering-1102:final-instance2/sales_data?user=root";
    public SQLDatabaseQueryHelper(){
    }
    public static List<List<Double>> getPlaces(final double latitude, final double longitude,
                                               final double distanceInKm, final int resultCount) throws SQLException, ClassNotFoundException {
        int KM_CONSTANT = 6371;
//        double latitude = latitud;//50.8252;
//        double longitude = longitud;//-0.13824;
        String url = null;
        Class.forName(CLASS_NAME);
        url = URL;
        //create rectangel for query
        double lon1 = (longitude - distanceInKm / Math.abs(Math.cos(latitude * Math.PI / 180.0) * 69));
        double lon2 = (longitude + distanceInKm / Math.abs(Math.cos(latitude * Math.PI / 180.0) * 69));
        double lat1 = (latitude - (distanceInKm / 69));
        double lat2 = (latitude + (distanceInKm / 69));
        String query =
//                "SELECT"+
//        "*, ("+
//                "6371 * acos ("+
//                        "cos ( radians("+latitude+") )"+
//                                "* cos( radians( latitude ) )"+
//                                "* cos( radians( longitude ) - radians("+longitude+") )"+
//                                "+ sin ( radians("+latitude+") )"+
//                                "* sin( radians( latitude ) )"+
//                ")"+
//        ") AS distance "+
//        "FROM HeatmapData" +
//        "HAVING distance <"+ distanceInKm+" " +
//        "ORDER BY distance "+
//        "LIMIT 0 , "+resultCount;
                "select * from HeatmapData where latitude > " + lat1 +
                " and latitude < " + lat2 + " and longitude > " + lon1 + " and longitude < " + lon2 +
                " ";
        Connection conn = DriverManager.getConnection(url);
        //test with mock queries
        ResultSet rs = conn.createStatement().executeQuery(query);
        //define return array
        List<List<Double>> places = new ArrayList<>();
        while (rs.next()) {
            List<Double> s = new ArrayList<>();
            s.add(rs.getDouble("latitude"));
            s.add(rs.getDouble("longitude"));
            s.add(rs.getDouble("averagePrice"));
            places.add(s);
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