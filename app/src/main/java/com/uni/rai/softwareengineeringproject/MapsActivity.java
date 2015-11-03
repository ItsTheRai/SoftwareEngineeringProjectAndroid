package com.uni.rai.softwareengineeringproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.v7.app.ActionBar;


import com.example.rai.myapplication.backend.userLocationApi.model.UserLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.appengine.api.datastore.GeoPt;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.uni.rai.softwareengineeringproject.tasks.*;
import com.uni.rai.softwareengineeringproject.tasks.UpdateLocationAsyncTask;
import android.support.v7.app.ActionBarActivity;

import android.widget.Toast;
import android.view.MenuItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.view.Menu;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final long LOCATION_REQUEST_INTERVAL = 100;//ms
    private static final long FASTEST_LOCATION_INTERVAL = 100;//ms
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
//    private Location mLastLocation;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;
    private boolean isLockedOn;
    private TileOverlay tOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start the back end task

//
        //set tracking flag
        isLockedOn=false;
        //since API 23, need to ask for user permission to use location
        //so create a check and dialog box

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explenation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0   //could not find the right constant, 0 seems to work for now
                );//MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

//        reate a new location request for the map locator
        createLocationRequest();
        //build a google api client
        buildGoogleApiClient();
        //set view as the map activity
        setContentView(R.layout.activity_maps);
        //init the map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        //call onMapReady()
        mapFragment.getMapAsync(this);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.heat_map:
//                clearHeatmap();
                updateHeatmap();    //querries the DB to update the heatmap
                item.setChecked(true);
                Toast.makeText(getApplicationContext(),
                        "Heat Map selected",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.normal_map:
                clearHeatmap(); // clear the current heatmap
                item.setChecked(true);
                Toast.makeText(getApplicationContext(),
                        "Normal map selected",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        //check if connected
        checkInternet(); // check to see if connected to internet
        //connect to the server
        mGoogleApiClient.connect();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        mCurrentLocation = null; // reset the current location to null
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        //check connection
        checkInternet();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //create a callback to google and set parameters for frequency
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   //request highest possible accuracy from device
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //TODO: Any custom actions
                isLockedOn = !isLockedOn;
                if (isLockedOn) {
                    showUser();
                }
                if (!isLockedOn) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                } else if (isLockedOn) {
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                }
                return true;
            }
        });
        
    }

    public void showUser(){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @Override
    //updated the location variables once a connection is established with the google server
    public void onConnected(Bundle connectionHint) {
        //should have internet since connected

        startLocationUpdates(); //start listening for location changes
        // if can't find current location, check to see if GPS is turned on
        if (mCurrentLocation == null) {
            checkGPS();
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//            mMap.getCameraPosition().zoom;
        }
    }

    public void clearHeatmap(){

    }

    public void updateHeatmap() {
        ArrayList<WeightedLatLng> llList = new ArrayList<WeightedLatLng>();
        // These are just example locations and only creates heatmap overlay surrounding the current location. We will need to query the server to get the actual latitude, longitude, as well as the price.
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 237));
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude() - 0.0008, mCurrentLocation.getLongitude() - 0.0025), 182));
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude() - 0.0007, mCurrentLocation.getLongitude() - 0.005), 82));
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude() - 0.0005, mCurrentLocation.getLongitude() - 0.0021), 167));
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude() - 0.001), 192));
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude() - 0.001, mCurrentLocation.getLongitude() - 0.0005), 142));
        llList.add(new WeightedLatLng(new LatLng(mCurrentLocation.getLatitude() - 0.0012, mCurrentLocation.getLongitude() - 0.0018), 217));

        createHeatmap(llList);
    }

    // check for internet connection using the isInternetConnected method, if not connected, show a warning message
    private void checkInternet() {
        if (!isInternetConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unable to connect to the internet");
            builder.setMessage("If you see a white screen instead of a map, please enable wifi or mobile data and try again");
            builder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // go to the Wifi settings screen
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  Do nothing
                }
            });
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    // return true if connected to the internet, false otherwise
    private boolean isInternetConnected() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        //use && netInfo.isConnected() since as this may not get the right results (airplane mode etc)
        return netInfo != null && netInfo.isConnected();
    }

    // check if GPS is on, if it's on, this method should do nothing, otherwise display a dialog that redirect the user to the location setting screen
    private void checkGPS() {
        System.out.println("checking GPS");
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unable to determine current location");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    //request location udates from google
    protected void startLocationUpdates() {
        //request google to start receiving regular updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mCurrentLocation == null) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
//        else{
//            mCurrentLocation=mLastLocation;
//        }

        //map centers in on the current location

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(25));

    }

    @Override
    //Update as the device is moving
    public void onLocationChanged(Location location) {
//        GeoPt pt = new GeoPt();
//        new UpdateMapTask().execute()
        //push current location
        //        new UpdateUserLocation().execute(new Pair<Context, Location>(this, location));

        mCurrentLocation = location; //get current location
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());  //get last update time
        if(isLockedOn) {
            //map centers in on the current location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }
    }

    //  create heatmap using the list taken from a parameter
    public void createHeatmap(ArrayList<WeightedLatLng> pointsList) {
        HeatmapTileProvider tProvider = new HeatmapTileProvider.Builder().weightedData(pointsList).build();
        tOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tProvider));
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void ScreenPage (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
