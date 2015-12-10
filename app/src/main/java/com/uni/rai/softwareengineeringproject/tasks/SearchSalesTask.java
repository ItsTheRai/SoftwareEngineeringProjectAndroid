 package com.uni.rai.softwareengineeringproject.tasks;

 import android.app.Activity;
 import android.content.Context;
 import android.os.AsyncTask;

 import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;
 import com.example.rai.myapplication.backend.salesInformationApi.model.SalesData;
 import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataCollection;
 import com.google.api.client.extensions.android.http.AndroidHttp;
 import com.google.api.client.extensions.android.json.AndroidJsonFactory;

 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;


public class SearchSalesTask extends AsyncTask< String, Void , List<SalesData>> {
     Activity mActivity;
    private static SalesInformationApi myApiService = null;
    private Context context;
     OnDataSendToActivity dataSendToActivity;

     public SearchSalesTask(Activity activity){
         dataSendToActivity = (OnDataSendToActivity)activity;
     }

    @Override
    protected void onPreExecute() {
    }

    @Override
    //not type safe but gets the job done
    protected List<SalesData> doInBackground(String... params) {
        String paon = params[0];
        String saon =  params[1];
        String street =  params[2];
        String locality =  params[3];
        String postcode =  params[4];

//        }

        if(myApiService == null) { // Only do this once
            SalesInformationApi.Builder builder = new SalesInformationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    .setRootUrl("https://software-engineering-1102.appspot.com/_ah/api/");
            myApiService = builder.build();
        }
        SalesDataCollection result = null;

        try {
            result = myApiService.findSales(paon, saon, street, locality, postcode).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(result)
        if(result!=null && !result.isEmpty()) {
            return result.getItems();
        }
        return new ArrayList<>();
    }

}

