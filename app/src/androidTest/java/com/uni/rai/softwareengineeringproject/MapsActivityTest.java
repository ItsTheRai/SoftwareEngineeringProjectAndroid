package com.uni.rai.softwareengineeringproject;

import android.app.Instrumentation;
import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.Map;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.Button;
import junit.framework.Assert;
import android.test.TouchUtils;
import android.content.DialogInterface;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.TileOverlay;
import java.util.concurrent.ExecutionException;
import com.uni.rai.softwareengineeringproject.tasks.UpdateMapTask;
import android.net.wifi.WifiManager;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
/**
 * Created by arsalanmerchant on 08/11/2015.
 */
public class MapsActivityTest  extends ActivityInstrumentationTestCase2<MapsActivity> {

    MapsActivity mapsActivity;
    private Context mContext;
    private TileOverlay tOverlay;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private static final long  LOCATION_REQUEST_INTERVAL = 3000;
    private static final long FASTEST_LOCATION_INTERVAL = 3000;



    public MapsActivityTest() {
        super(MapsActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mapsActivity = getActivity();
        mContext = getInstrumentation().getTargetContext().getApplicationContext();
    }

    @SmallTest
    public void testActivityExists() {
        assertNotNull(mapsActivity);
    }


    @SmallTest
    public void testConnectionTest() {
       ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

        if (netInfo != null) {
            assertEquals(mapsActivity.isInternetConnected(), netInfo.isConnected());
            assertEquals(mapsActivity.checkInternet(), false);

        } else {
            assertEquals(mapsActivity.checkInternet(),true);
        }
    }


    @SmallTest
    public void testGPS() {
        LocationManager locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            assertEquals(mapsActivity.checkGPS(),true);
        } else {
            assertEquals(mapsActivity.checkGPS(),false);
        }

    }

    /**
     * Review later
     *
     */
    @SmallTest
    public void teststartLocationUpdates() {
        assertEquals(mapsActivity.startLocationUpdates(),true);

    }




    @SmallTest
    public void testcreateLocationRequest() {
        mLocationRequest = new LocationRequest();
        if (mLocationRequest.getInterval() == LOCATION_REQUEST_INTERVAL && mLocationRequest.getFastestInterval() == FASTEST_LOCATION_INTERVAL
                && mLocationRequest.getPriority() == LocationRequest.PRIORITY_HIGH_ACCURACY  ){

        assertEquals(mapsActivity.createLocationRequest(), false);

        } else {
            assertEquals(mapsActivity.createLocationRequest(), true);
        }
    }




    @SmallTest
    public void testonConnected() {

        //mMap = (MapFragment) mapsActivity.getFragmentManager()
             //   .findFragmentById(R.id.map);

        if (mCurrentLocation == null) {
            assertTrue(mapsActivity.checkGPS());
        }



    }



/**
    @SmallTest
    public void testupdateHeatmap() throws ExecutionException, InterruptedException {

        SalesDataShortCollection data = new UpdateMapTask(this).execute(mCurrentLocation).get();
        if(!data.isEmpty()) {

            assertEquals(mapsActivity.updateHeatmap(), true);

        } else{

            assertEquals(mapsActivity.updateHeatmap(), false);

        }
    }

**/

    /**


    @MediumTest
    public void testStartMyActivity() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MapsActivity.class.getName(), null, false);


        if (netInfo == null) {

            MapsActivity myActivity = (MapsActivity) monitor.waitForActivityWithTimeout(2000);
            assertNotNull("MyActivity activity not started, activity is null", myActivity);

            AlertDialog dialog = myActivity.getDialog(); // I create getLastDialog method in MyActivity class. Its return last created AlertDialog
            if (dialog.isShowing()) {
                try {
                    performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }


            myActivity.finish();
            getInstrumentation().removeMonitor(monitor);

        }

    }

    private void performClick(final Button button) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }




**/






/**
    @MediumTest
    public void testConnectivityExists() {
        //ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

        //assertEquals(mapsActivity.isInternetConnected(),netInfo.isConnected());

        //assertEquals(mapsActivity.isInternetConnected(),netInfo.isConnected());
        //assertEquals(mapsActivity.isInternetConnected(),netInfo != null);

        assertNotNull(mapsActivity);
/**
        if (!mapsActivity.isInternetConnected()) {

            Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MapsActivity.class.getName(), null, false);
            mapsActivity = (MapsActivity) activityMonitor.waitForActivityWithTimeout(2000);

            AlertDialog.Builder dialog = mapsActivity.getDialog();

            assertNotNull("aboutbox is null",mapsActivity.getDialog());
            /**
            assertTrue(dialog.isShowing());
            if (dialog.isShowing()) {
                try {
                    performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                mapsActivity.finish();
                getInstrumentation().removeMonitor(activityMonitor);

                //AlertDialog.Builder dialog = myActivity.getDialog();


                // Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                //assertTrue(okButton.performClick());


            }

        }
    }
 **/

/**
        private void performClick(final Button button) throws Throwable {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.performClick();
                }
            });
            getInstrumentation().waitForIdleSync();
        }
**/
/**
    private void performClick(Button button) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPositiveButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }



**/
/**
 *
 *  // return true if connected to the internet, false otherwise
 public boolean isInternetConnected() {
 ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo netInfo = conManager.getActiveNetworkInfo();
 //use && netInfo.isConnected() since as this may not get the right results (airplane mode etc)
 return netInfo != null && netInfo.isConnected();
 }
 *
 */










}
