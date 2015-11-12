package com.uni.rai.softwareengineeringproject;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

import android.view.MenuItem;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShort;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.TileOverlay;
import java.util.concurrent.ExecutionException;
import com.uni.rai.softwareengineeringproject.tasks.UpdateMapTask;
import android.net.wifi.WifiManager;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.app.ActionBar;
import android.view.MenuItem;
import android.app.Instrumentation.ActivityMonitor;
import android.view.KeyEvent;


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
    private GoogleMap mMap;
    private LatLng coordinates;
    private Menu menu;





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
            assertEquals(mapsActivity.checkGPS(), true);
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
        if (mLocationRequest.getInterval() != LOCATION_REQUEST_INTERVAL && mLocationRequest.getFastestInterval() != FASTEST_LOCATION_INTERVAL
                && mLocationRequest.getPriority() != LocationRequest.PRIORITY_HIGH_ACCURACY  ){

            assertEquals(mapsActivity.createLocationRequest(), true);

        } else {
            assertEquals(mapsActivity.createLocationRequest(), false);
        }
    }


    @SmallTest
    public void testonConnected() {

        assertTrue(mapsActivity.startLocationUpdates());
        if (mCurrentLocation == null) {
            assertTrue(mapsActivity.checkGPS());
        } else {

            mMap =((MapFragment) mapsActivity.getFragmentManager().findFragmentById(R.id.map)).getMap();

            mMap = SupportMapFragment.newInstance(new GoogleMapOptions()
                    .camera(new CameraPosition(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 0, 0, 0))
                    .zoomControlsEnabled(true)
                    .scrollGesturesEnabled(true))
                    .getMap();


            // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            // mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            // mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            coordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLatitude());

            assertEquals(mMap.getCameraPosition().target, coordinates);

            //assertEquals(mMap.getMaxZoomLevel(), 15);

        }


    }

    @SmallTest
    public void testonMapReady() throws ExecutionException, InterruptedException {
        MapFragment mMap = (MapFragment) mapsActivity.getFragmentManager()
                .findFragmentById(R.id.map);
        assertNotNull(mMap);
    }
/**
 @SmallTest
 public void testScreenPage (){
 assertEquals(mapsActivity.ScreenPage(mapsActivity.view), true);
 }
 /**

 @SmallTest
 public  void testonCreateOptionsMenu() {
 assertEquals(mapsActivity.onCreateOptionsMenu(mapsActivity.menu), true);
 }
 **/







}
