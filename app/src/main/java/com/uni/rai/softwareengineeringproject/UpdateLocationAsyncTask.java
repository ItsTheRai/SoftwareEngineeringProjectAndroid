package com.uni.rai.softwareengineeringproject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.rai.myapplication.backend.userLocationApi.UserLocationApi;
import com.example.rai.myapplication.backend.userLocationApi.model.UserLocation;
//import com.example.rai.myapplication.backend.UserLocation;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by rai on 21/10/15.
 */




class UpdateLocationAsyncTask extends AsyncTask<UserLocation, Void, Void> {
    private static UserLocationApi myApiService = null;
    private Context context;

    UpdateLocationAsyncTask(Context context) {
        this.context = context;
    }

//    @Override
//    protected List<UserLocation> doInBackground(Void... params) {
//
//    }

    @Override
    protected Void doInBackground(UserLocation... params) {
        if(myApiService == null) { // Only do this once
//            UserLocationEndpoint

            UserLocationApi.Builder builder = new UserLocationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://DogStudio.appspot.com/_ah/api/");
//                    AndroidHttp.newCompatibleTransport(),
//                    new AndroidJsonFactory(), null)
//// options for running against local devappserver
//
//// - 10.0.2.2 is localhost's IP address in Android emulator
//// - turn off compression when running against local devappserver
//                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")//for emulator
////                    .setRootUrl("http://192.168.0.4:8080/_ah/api/")
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                        @Override
//                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                            abstractGoogleClientRequest.setDisableGZipContent(true);
//                        }
//                    });
// end options for devappserver

            myApiService = builder.build();
        }
        try {

//            UserLocation l = new UserLocation();
//            l.setLatitude((long) params[0].getLongitude());
            myApiService.updateUserLocation(params[0]).execute();
            Toast.makeText(context, params[0].getLatitude() + " : " + params[0].getLongitude(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            return myApiService.updateUserLocation(new UserLocation(params[0],params[1]));
//            return myApiService.listQuote().execute().getItems();
//        } catch (IOException e) {
//            return Collections.EMPTY_LIST;
//        }
        return null;
    }

//    @Override
//    protected void onPostExecute(List<UserLocation> result) {
//        for (Quote q : result) {
//            Toast.makeText(context, q.getWho() + " : " + q.getWhat(), Toast.LENGTH_LONG).show();
//        }
    }
