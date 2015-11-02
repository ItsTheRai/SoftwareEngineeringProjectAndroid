package com.uni.rai.softwareengineeringproject.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.rai.myapplication.backend.salesInformationApi.model.SalesInformation;
import com.example.rai.myapplication.backend.userLocationApi.UserLocationApi;
import com.example.rai.myapplication.backend.userLocationApi.model.UserLocation;
import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by rai on 21/10/15.
 */




class UpdateMapTask extends AsyncTask<UserLocation, Void, SalesInformation> {
    private static UserLocationApi myApiService = null;
    private Context context;

    UpdateMapTask(Context context) {
        this.context = context;
    }

//    @Override
//    protected List<UserLocation> doInBackground(Void... params) {
//
//    }

    @Override
    protected SalesInformation doInBackground(UserLocation... params) {
        if(myApiService == null) { // Only do this once
            UserLocationApi.Builder builder = new UserLocationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    .setRootUrl("https://software-engineering-1102.appspot.com/_ah/api/");    //for servlet
            myApiService = builder.build();
        }
        try {
            myApiService.updateUserLocation(params[0]).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    protected void onPostExecute(List<UserLocation> result) {
//        for (Quote q : result) {
//            Toast.makeText(context, q.getWho() + " : " + q.getWhat(), Toast.LENGTH_LONG).show();
//        }
}
