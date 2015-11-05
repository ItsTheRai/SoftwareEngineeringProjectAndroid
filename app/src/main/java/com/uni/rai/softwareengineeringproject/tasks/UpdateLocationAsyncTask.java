package com.uni.rai.softwareengineeringproject.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rai.myapplication.backend.salesInformationApi.model.GeoPt;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesData;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesInformation;
import com.example.rai.myapplication.backend.userLocationApi.UserLocationApi;
//import com.example.rai.myapplication.backend.userLocationApi.model.UserLocation;
import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by rai on 21/10/15.
 */


public class UpdateLocationAsyncTask extends AsyncTask<Void, Void, Void> {
    private static UserLocationApi myApiService = null;
    private static SalesInformationApi myApiService2 = null;
    private Context context;

    public UpdateLocationAsyncTask(Context context) {
        this.context = context;
    }

//    @Override
//    protected List<UserLocation> doInBackground(Void... params) {
//
//    }

    @Override
    protected Void doInBackground(Void... params) {
//        if(myApiService == null) { // Only do this once
////            UserLocationEndpoint
//
//            UserLocationApi.Builder builder = new UserLocationApi.Builder(
//                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                    // options for running against local devappserver
//                    .setRootUrl("https://software-engineering-1102.appspot.com/_ah/api/");    //for servlet
////                  .setRootUrl("http://192.168.0.4:8080/_ah/api/") //for local wifi
////                  .setRootUrl("http://10.0.2.2:8080/_ah/api/")//for emulator
//// - 10.0.2.2 is localhost's IP address in Android emulator   //for emulator
//// - turn off compression when running against local devappserver
////                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
////                        @Override
////                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
////                            abstractGoogleClientRequest.setDisableGZipContent(true);
////                        }
////                    }
////        );
//            myApiService = builder.build();
//        }
//        try {
//            myApiService.updateUserLocation(params[0]).execute();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        if(myApiService2 == null) { // Only do this once
//            UserLocationEndpoint

            SalesInformationApi.Builder builder = new SalesInformationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    .setRootUrl("https://software-engineering-1102.appspot.com/_ah/api/");    //for servlet
//                  .setRootUrl("http://192.168.0.4:8080/_ah/api/") //for local wifi
//                  .setRootUrl("http://10.0.2.2:8080/_ah/api/")//for emulator
// - 10.0.2.2 is localhost's IP address in Android emulator   //for emulator
// - turn off compression when running against local devappserver
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                        @Override
//                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                            abstractGoogleClientRequest.setDisableGZipContent(true);
//                        }
//                    }
//        );
            myApiService2 = builder.build();
        }
//            SalesData a = new SalesData();
//            a.setCounty("couty");
////            a.setDate("Date");
//            a.setDistrict("District");
//            a.setDuration("Duration");
//            a.setLocality("Locality");
//            GeoPt pt = new GeoPt();
//            a.setLatitude((float) 50.8680860);
//            a.setLongitude((float) -0.0904250);
////            a.setLocation(pt);
//            a.setOldOrNew("OldOrNew");
//            a.setPaon("PAON");
//            a.setPostcode("POSTCODE");
//            a.setPpdcategory("PPDCat");
//            a.setPrice(000);
//            a.setPropertyType("PriopertyType");
//            a.setSaon("SAON");
//            a.setStreet("Street");
//            a.setTown("Town");
//            a.setUniqueRef("UNIQUREF");
//            myApiService2.insert(a).execute();
        return null;
    }

//    @Override
//    protected void onPostExecute(Void... result) {
//        for (Quote q : result) {
//            Toast.makeText(context, q.getWho() + " : " + q.getWhat(), Toast.LENGTH_LONG).show();
//        }
    }
