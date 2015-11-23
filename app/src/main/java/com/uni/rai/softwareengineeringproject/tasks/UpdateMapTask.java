 package com.uni.rai.softwareengineeringproject.tasks;

        import android.content.Context;
        import android.location.Location;
        import android.os.AsyncTask;

        import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataCollection;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
        import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
        import com.google.android.gms.maps.model.VisibleRegion;
        import com.google.api.client.extensions.android.http.AndroidHttp;
        import com.google.api.client.extensions.android.json.AndroidJsonFactory;
        import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
        import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

        import java.io.IOException;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;

 /**
 * Created by rai on 21/10/15.
 */


public class UpdateMapTask extends AsyncTask<Object, Void, SalesDataShortCollection> {
    //    private static long range =1000000000;
    private static final int NUMBER_OF_RESULTS = 10000;
    private static SalesInformationApi myApiService = null;
    private Context context;

    public UpdateMapTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
//        placesListLabel.setText(R.string.retrievingPlaces);
//        MainActivity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    //not type safe but gets the job done
    protected SalesDataShortCollection doInBackground(Object... params) {
        Location location = (Location)params[0];
        double distanceInKm =  (double)params[1]; ///////////////////////////////////////////
//        double rangeInKm=0.5;
//        double lon1 =  (location.getLongitude()-rangeInKm/1000/Math.abs(Math.cos(location.getLatitude()*Math.PI/180.0) * 69));
//        double lon2 =  (location.getLongitude()+rangeInKm/1000/Math.abs(Math.cos(location.getLatitude()*Math.PI/180.0) * 69));
//        double lat1 =  (location.getLatitude()-(rangeInKm/1000/69));
//        double lat2 =  (location.getLatitude()+(rangeInKm/1000/69));
//        String url = null;
//        try {
//            Class.forName("com.mysql.jdbc.GoogleDriver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        url = "jdbc:google:mysql://software-engineering-1102:final-instance2/sales_data?user=root";
//        int KM_CONSTANT = 6371;
//
//        //create rectangel for query
//
//        String poligonString = "'POLYGON(("+lon1+" "+lat1+", "+lon1+" "+lat2+", "+
//                lon2+" "+lat2+", "+lon2+" "+lat1+", "+lon1+" "+lat1+"))'";
//
//        String querySetPolygon = "SET @poly =\n" +
//                "     'Polygon(("+lon1+" "+lat1+"," +
//                "                 "+lon2+" "+lat1+"," +
//                "                 "+lon2+" "+lat2+"," +
//                "                 "+lon1+" "+lat2+"," +
//                "                 "+lon1+" "+lat1+"))';";//"SET @bbox = 'POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))';"+
////                " select Unique_ref, AsText(Coords) FROM SalesData WHERE Intersects(Coords'POLYGON((-1.54324 49.987987, -1.0564 49.987987, -1.0564 50.987987, -1.0564 49.987987, -1.54324 49.987987))' ) );"
//        String queryDB = "SELECT Unique_ref, AsText(Coords) FROM SalesData WHERE MBRWithin(Coords, GeomFromText(@poly))" +
//                ";";
//
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(url);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            conn.createStatement().executeQuery(querySetPolygon);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            ResultSet rs = conn.createStatement().executeQuery(queryDB);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        /////////////////////////////////////////////////////////////////////




        //VisibleRegion region = (VisibleRegion)params[2];

        double longitude;
        double latitude;

//        if (location == null) {
            // This is used to easily simulate a location in the emulator
            // when developing. For real devices deployment,
            // this temporary code should be removed and the function
            // should just return null.
//            longitude =  0.0f;
//            latitude =  0.0f;
            // return null;
//        } else {
            latitude =  location.getLatitude();
            longitude =  location.getLongitude();
//        }

        if(myApiService == null) { // Only do this once
            SalesInformationApi.Builder builder = new SalesInformationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    .setRootUrl("https://software-engineering-1102.appspot.com/_ah/api/");    //for servlet
//            .setRootUrl("http://192.168.0.4:8080/_ah/api/")//for local wifi
//                    - turn off compression when running against local devappserver
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                                                           @Override
//                                                           public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                                                               abstractGoogleClientRequest.setDisableGZipContent(true);
//                                                           }
//                                                       }
//                    );
//
            myApiService = builder.build();
        }
        SalesDataShortCollection result = null;
        try {
            result = myApiService.getPointsInRange( latitude, longitude,
                    distanceInKm, NUMBER_OF_RESULTS).execute();     } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    @Override
//    protected void onPostExecute(List<UserLocation> result) {
//        for (Quote q : result) {
//            Toast.makeText(context, q.getWho() + " : " + q.getWhat(), Toast.LENGTH_LONG).show();
//        }
}

