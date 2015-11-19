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


//import com.example.rai.myapplication.backend.model.SalesLocationData;
//import com.example.rai.myapplication.backend.userLocationApi.model.UserLocation;
//import com.example.rai.myapplication.backend.model.SalesLocationData;
//import com.example.rai.myapplication.backend.salesInformationApi.SalesInformationApi;

//import com.example.rai.myapplication.backend.model.SalesInformation;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesData;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataCollection;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShort;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesLocationData;
//import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShort;
import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.uni.rai.softwareengineeringproject.tasks.UpdateLocationAsyncTask;
import com.uni.rai.softwareengineeringproject.tasks.UpdateMapTask;
//import com.uni.rai.softwareengineeringproject.tasks.*;
//import com.uni.rai.softwareengineeringproject.tasks.UpdateLocationAsyncTask;

import android.widget.Toast;
import android.view.MenuItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.view.Menu;


public class MapsActivity extends FragmentActivity implements OnTaskCompleted, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final long LOCATION_REQUEST_INTERVAL = 20000;//ms
    private static final long FASTEST_LOCATION_INTERVAL = 20000;//ms
    private static final double EARTH_RADIUS = 6378.1;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
//    private Location mLastLocation;

    private List<SalesDataShort> currentSalesData;

    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;
    private boolean isLockedOn;
    private TileOverlay tOverlay;
    private boolean firstRequest;
    private double currentRangeInKm;
    public MenuItem item;
    public View view;
    public Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //set tracking flag
        firstRequest=true;
        isLockedOn = false;currentRangeInKm=0.0;
        currentSalesData = new ArrayList<SalesDataShort>();

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
            //heatmap gets displayed when map is ready
        //init variables
//        currentSalesData = new SalesDataShortCollection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item = item;
        getItem(item);
        switch (item.getItemId()) {
            case R.id.heat_map:
                clearHeatmap();
                try {
                    updateHeatmap();    //querries the DB to update the heatmap
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                return true;
        }
    }

    public boolean getItem(MenuItem i) {
        switch (i.getItemId()) {
            case R.id.heat_map:
                return true;
            case R.id.normal_map:
                return true;
            default:
                return false;
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

    protected boolean stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
        return true;
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

    protected synchronized boolean buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return true;
    }

    //create a callback to google and set parameters for frequency
    protected boolean createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   //request highest possible accuracy from device
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        isLockedOn=false;
//        showUser();
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            //add listener to update camera when the user zooms in/out
            @Override
            public void onCameraChange(CameraPosition pos) {
                //TODO uncomment method
                float minZoom = 15.0f;
                if(pos.zoom<minZoom){
                    mMap.animateCamera((CameraUpdateFactory.zoomTo(minZoom)));
                }
//                pos.target
                try {
                    updateHeatmap();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //create button listener to lock on/off user
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //TODO: Any custom actions
//                isLockedOn = !isLockedOn;
                isLockedOn = false;
//                if (isLockedOn) {
                showUser();
//                }
                if (!isLockedOn) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                } else if (isLockedOn) {
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                }
                return true;
            }
        });

    }

    public boolean  showUser() {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        return true;
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
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//            mMap.getCameraPosition().zoom;
        }
    }

    public boolean clearHeatmap(){
        if(tOverlay!=null) {
            tOverlay.remove();
        }else {
            return false;
        }
        return true;
    }

    public void updateHeatmap() throws ExecutionException, InterruptedException {
        //calculate necassary range to load data from screen size and zoom
//        if(currentSalesData.getItems()!=null){
        double rangeInKm = getRange(mMap);
        if (rangeInKm > currentRangeInKm) {
                currentRangeInKm = rangeInKm;
                //query DB with async taks
                SalesDataShortCollection data = getDataInRange(mMap.getMyLocation(), rangeInKm);
                if (data != null) {
                    if (!data.isEmpty()) {
                        List<SalesDataShort> places = data.getItems();
                        //check if matches found
                        if (places != null) {
                            Toast.makeText(getApplicationContext(),
                                    String.valueOf(places.size()),
                                    Toast.LENGTH_LONG).show();
                            //init add list
//                                List<SalesDataShort> items = new ArrayList<>();
                            for (SalesDataShort d : places) {
                                if (currentSalesData.isEmpty()) {
                                    currentSalesData.add(d);
//                                    if (!items.contains(d)) {
//                                        items.add(d);
//                                    }
                                }
                                else if(!currentSalesData.contains(d)) {
                                    currentSalesData.add(d);
                                }
                            }

//                            drawHeatmapOverlay();
                        }
                    }
                    //start new task in background to pre- fetch datapoints
//            UpdateMapTask asyncTask = new UpdateMapTask(new OnTaskCompleted(){
//            @Override
//            public void processFinish(Object output) {
//                if(((SalesDataShortCollection) output).getItems().size()>currentSalesData.getItems().size()){
//                    currentSalesData=(SalesDataShortCollection)output;
////            currentRangeInKm=get
//                }
//            }
//        };
//        asyncTask.execute(mCurrentLocation,currentRangeInKm*1.5);

//            new UpdateMapTask(this).execute(mCurrentLocation,currentRangeInKm*1.5);
            }
        }
        drawHeatmapOverlay();
    }

    public void drawHeatmapOverlay(){
        ArrayList<WeightedLatLng> llList = new ArrayList<>();
        if(!currentSalesData.isEmpty()) {
            for (SalesDataShort d : currentSalesData) {
                llList.add(new WeightedLatLng(new LatLng((double) d.getLocationGeo().getLatitude(),
                        (double) d.getLocationGeo().getLongitude()),
                        d.getPrice() / 10000));//TODO this is not a great heuristic, find something that scales based on data
            }
            createHeatmap(llList);
        }

    }

    ///return a collection of SalesDataShort objects within a specific range
    //make sure that first parameter is the current location and the second one - the range of results in km
    public SalesDataShortCollection getDataInRange(Location location, double range) throws ExecutionException, InterruptedException {
        return new UpdateMapTask(this).execute(location,range).get();
    }

    //this method returns the distance in kilometers from the top left to the bottom right corners of the visible map as double
    public double getRange(GoogleMap map){
        double leftLat = map.getProjection().getVisibleRegion().farLeft.latitude;
        double leftLon = map.getProjection().getVisibleRegion().farLeft.longitude;
        double rightLat = map.getProjection().getVisibleRegion().farRight.latitude;
        double rightLon = map.getProjection().getVisibleRegion().farRight.longitude;
        //calculate distance from LatLong values
        return getDistanceInKm(leftLat, leftLon, rightLat, rightLon);
    }

    /**
     * Computes the geodesic distance between two GPS coordinates.
     * @param latitude1 the latitude of the first point.
     * @param longitude1 the longitude of the first point.
     * @param latitude2 the latitude of the second point.
     * @param longitude2 the longitude of the second point.
     * @return the geodesic distance between the two points, in kilometers.
     */
    public static double getDistanceInKm(
            final double latitude1, final double longitude1,
            final double latitude2, final double longitude2) {

        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        double long1 = Math.toRadians(longitude1);
        double long2 = Math.toRadians(longitude2);

        return EARTH_RADIUS * Math
                .acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
                        * Math.cos(lat2) * Math.cos(Math.abs(long1 - long2)));
    }

    // check for internet connection using the isInternetConnected method, if not connected, show a warning message
    public boolean checkInternet() {
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
        }else{
            return false;
        }
        return true;
    }

    // return true if connected to the internet, false otherwise
    public boolean isInternetConnected() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        //use && netInfo.isConnected() since as this may not get the right results (airplane mode etc)
        return netInfo != null && netInfo.isConnected();
    }

    // check if GPS is on, if it's on, this method should do nothing, otherwise display a dialog that redirect the user to the location setting screen
    public boolean checkGPS() {
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
        }else {
            return false;
        }
        return true;
    }

    //request location udates from google
    protected boolean startLocationUpdates() {
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
        return true;
    }

    @Override
    //Update as the device is moving
    public void onLocationChanged(Location location) {
        mCurrentLocation = location; //update current location
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());  //get last update time

        if (mCurrentLocation == null) {
            checkGPS();
        }
        if(firstRequest) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            firstRequest=false;
        }
        else {
            if (isLockedOn) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            }
        }
//        GeoPt pt = new GeoPt();
//        new UpdateMapTask().execute()
        //push current location
        //        new UpdateUserLocation().execute(new Pair<Context, Location>(this, location));


        if(isLockedOn) {
            //map centers in on the current location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }
    }

    //  create heatmap using the list taken from a parameter
    public boolean createHeatmap(ArrayList<WeightedLatLng> pointsList) {
        HeatmapTileProvider tProvider = new HeatmapTileProvider.Builder().weightedData(pointsList).build();
        tOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tProvider));
        return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public boolean ScreenPage (View view){
        this.view = view;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onTaskCompleted(SalesDataShortCollection output) {
//        if(((SalesDataShortCollection) output).getItems().size()>currentSalesData.getItems().size()){
//            currentSalesData=(SalesDataShortCollection)output;
//            currentRangeInKm=get
//        }
    }
}
