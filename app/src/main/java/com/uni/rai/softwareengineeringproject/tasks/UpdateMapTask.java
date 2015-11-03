package com.uni.rai.softwareengineeringproject.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.rai.myapplication.backend.salesInformationApi.model.SalesInformation;
import com.example.rai.myapplication.backend.userLocationApi.UserLocationApi;
import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
import com.example.rai.myapplication.backend.userLocationApi.model.UserLocation;
import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.appengine.api.datastore.GeoPt;
import com.uni.rai.softwareengineeringproject.MainActivity;

import java.io.IOException;
import java.util.List;

/**
 * Created by rai on 21/10/15.
 */


public class UpdateMapTask extends AsyncTask<GeoPt, Void, List<SalesInformation>> {
    private static SalesInformationApi myApiService = null;
    private Context context;

    UpdateMapTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
//        placesListLabel.setText(R.string.retrievingPlaces);
//        MainActivity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected List<SalesInformation> doInBackground(GeoPt... params) {
        if(myApiService == null) { // Only do this once
            SalesInformationApi.Builder builder = new SalesInformationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    .setRootUrl("https://software-engineering-1102.appspot.com/_ah/api/");    //for servlet
            myApiService = builder.build();
        }
//        try {
////            myApiService.get()
////
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

//    @Override
//    protected void onPostExecute(List<UserLocation> result) {
//        for (Quote q : result) {
//            Toast.makeText(context, q.getWho() + " : " + q.getWhat(), Toast.LENGTH_LONG).show();
//        }
}
