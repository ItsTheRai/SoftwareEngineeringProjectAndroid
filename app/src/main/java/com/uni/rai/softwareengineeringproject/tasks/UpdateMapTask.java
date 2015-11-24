 package com.uni.rai.softwareengineeringproject.tasks;

        import android.content.Context;
        import android.location.Location;
        import android.os.AsyncTask;

//        import com.example.rai.myapplication.backend.LatLongWrapper;
        import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataCollection;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
//        import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
        import com.example.rai.myapplication.backend.salesInformationApi.model.NumberCollectionCollection;
        import com.example.rai.myapplication.backend.salesInformationApi.model.WeightedLatLngCollection;
        import com.google.android.gms.maps.model.VisibleRegion;
        import com.google.api.client.extensions.android.http.AndroidHttp;
        import com.google.api.client.extensions.android.json.AndroidJsonFactory;
        import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
        import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
        import com.google.maps.android.heatmaps.WeightedLatLng;

        import java.io.IOException;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.List;

 /**
 * Created by rai on 21/10/15.
 */


public class UpdateMapTask extends AsyncTask<Object, Void, List<List<Double>>> {
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
    protected List<List<Double>> doInBackground(Object... params) {
        Location location = (Location)params[0];
        double distanceInKm =  (double)params[1]; ///////////////////////////////////////////
        double longitude;
        double latitude;
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
        NumberCollectionCollection result = null;
//        LatLongWrapper s=null;
        try {
            result = myApiService.getPointsInRange( latitude, longitude, distanceInKm, NUMBER_OF_RESULTS).execute();     }
        catch (IOException e) {
            e.printStackTrace();
        }
//        if(result)
        return result.getItems();
    }

//    @Override
//    protected void onPostExecute(List<UserLocation> result) {
//        for (Quote q : result) {
//            Toast.makeText(context, q.getWho() + " : " + q.getWhat(), Toast.LENGTH_LONG).show();
//        }
}

