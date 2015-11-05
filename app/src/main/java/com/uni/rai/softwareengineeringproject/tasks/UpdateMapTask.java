package com.uni.rai.softwareengineeringproject.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.appengine.api.datastore.GeoPt;

import java.io.IOException;

/**
 * Created by rai on 21/10/15.
 */


public class UpdateMapTask extends AsyncTask<GeoPt, Void, SalesDataShortCollection> {
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
    protected SalesDataShortCollection doInBackground(GeoPt... params) {
        GeoPt location = params[0];
        float longitude;
        float latitude;

        if (location == null) {
            // This is used to easily simulate a location in the emulator
            // when developing. For real devices deployment,
            // this temporary code should be removed and the function
            // should just return null.
            longitude = (float) 0.0;
            latitude = (float) 0.0;
            // return null;
        } else {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        }

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
            myApiService = builder.build();
        }
        SalesDataShortCollection result = null;

        try {
            result = myApiService.getPointsInRange((float) 100.0, latitude, longitude, 50).execute();
        } catch (IOException e) {
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