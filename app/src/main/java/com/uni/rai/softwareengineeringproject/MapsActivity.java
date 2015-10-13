package com.uni.rai.softwareengineeringproject;

import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long LOCATION_REQUEST_INTERVAL = 1000;
    private static final long FASTEST_LOCATION_INTERVAL = 5000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private Location mLastLocation;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //build a google api client
        buildGoogleApiClient();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //create a new location request for the map locator
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   //request highest possible accuracy from device
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
    }


    @Override
    //updated the location variables once a connection is established
    public void onConnected(Bundle connectionHint) {
            startLocationUpdates();
        ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap().addMarker(new MarkerOptions().
                position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
    }

    //request location udates from google
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, this.mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    //Update as the device is moving
    public void onLocationChanged(Location location) {
        mCurrentLocation = location; //get current location
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());  //get las update time
        ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap().addMarker(new MarkerOptions().
                position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))); // try to drop a marker on the last location
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

        @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
