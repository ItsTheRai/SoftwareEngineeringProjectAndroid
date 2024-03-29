package com.uni.rai.softwareengineeringproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.view.MenuInflater;
import android.view.View;

import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.AdapterView;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.uni.rai.softwareengineeringproject.tasks.MarkerTask;
import com.uni.rai.softwareengineeringproject.tasks.OnDataSendToActivity;
import com.uni.rai.softwareengineeringproject.tasks.SearchSalesTask;
import com.uni.rai.softwareengineeringproject.tasks.UpdateMapTask;
//import com.uni.rai.softwareengineeringproject.tasks.UpdateLocationAsyncTask;
//import com.uni.rai.softwareengineeringproject.tasks.UpdateMapTask;
//import com.uni.rai.softwareengineeringproject.tasks.*;
//import com.uni.rai.softwareengineeringproject.tasks.UpdateLocationAsyncTask;

import android.widget.Toast;
import android.view.MenuItem;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.view.Menu;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;


public class MapsActivity extends FragmentActivity implements OnDataSendToActivity, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final long LOCATION_REQUEST_INTERVAL = 20000;//ms
    private static final long FASTEST_LOCATION_INTERVAL = 20000;//ms
    private static final double EARTH_RADIUS = 6378.1;
    private static final int TEN_SECONDS = 10 * 1000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
//    private Location mLastLocation;

    private List<WeightedLatLng> currentSalesData;

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
    private boolean isHeatmap;
    private WeightedLatLng dummy;
    private double minPrice = 0;
    private double maxPrice = 0;
    private double averagePrice = 0;

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private String[] drawerItems;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    List<List<String>> sales;
    List<Marker> markerList = new ArrayList<Marker>();
    private boolean isPinmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        //set tracking flag
        firstRequest=true;
        isLockedOn = false;
        currentRangeInKm=0.0;
        isHeatmap=true;isPinmap = false;
        currentSalesData = new ArrayList<>();
        sales= new ArrayList<List<String>>();
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
        setContentView(R.layout.drawer);
        //init the map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        //call onMapReady()
        mapFragment.getMapAsync(this);
        //heatmap gets displayed when map is ready
        //init variables
//        currentSalesData = new SalesDataShortCollection();


        mNavItems.add(new NavItem("Home", "Meetup destination", R.mipmap.arrowleft));

        //Heatmap Overlay
//        updatePrices();

/**
 // DrawerLayout
 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

 // Populate the Navigtion Drawer with options
 mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
 mDrawerList = (ListView) findViewById(R.id.navList);
 DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
 mDrawerList.setAdapter(adapter);

 **/

        drawerItems = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerList = (ListView)findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this);
        mDrawerList.setAdapter(adapter);
        //listview.setAdapter(new ArrayAdapter<>(this , android.R.layout.simple_list_item_1,drawerItems));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectItemFromDrawer(position);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,
               R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View drawerview){
                super.onDrawerClosed(drawerview);
                Toast toast = Toast.makeText(MapsActivity.this,"Drawer closed", Toast.LENGTH_LONG);
                toast.show();
                invalidateOptionsMenu();

            }

            public void onDrawerOpened(View drawView){
                super.onDrawerOpened(drawView);
                Toast toast = Toast.makeText(MapsActivity.this,"Drawer open", Toast.LENGTH_LONG);
                toast.show();
                invalidateOptionsMenu();

            }


        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);



        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //houseNumberText = intent.getStringExtra("houseNumberText");


    }

    public void updatePrices() {
        if (isHeatmap) {
            final TextView minText = (TextView) findViewById(R.id.textView2);
            final TextView maxText = (TextView) findViewById(R.id.textView3);
            final TextView averageText = (TextView) findViewById(R.id.textView4);

            //Replace second strings with values.
            minText.setText("" + minPrice);
            maxText.setText("" + maxPrice);
            averageText.setText("Average price:  " + (minPrice+maxPrice)/2);
        }
    }

    /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    **/

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_actions, menu);
/**
        //Searchable stuff
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //Look at  if not working.
        SearchView searchView = (SearchView) menu.findItem(R.id.search_id).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
 **/
        return super.onCreateOptionsMenu(menu);
    }

    /**
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
     this.menu = menu;
     getMenuInflater().inflate(R.menu.map_activity, menu);
     return true;
     }
     **/

/**
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
     this.item = item;

     switch (item.getItemId()) {
     case R.id.heat_map:
     isHeatmap = true;
     try {
     updateHeatmap();    //querries the DB to update the heatmap
     } catch (ExecutionException e) {
     e.printStackTrace();
     } catch (InterruptedException e) {
     e.printStackTrace();
     } catch (ClassNotFoundException e) {
     e.printStackTrace();
     } catch (SQLException e) {
     e.printStackTrace();
     }
     item.setChecked(true);
     //                Toast.makeText(getApplicationContext(),
     //                        "Heat Map selected",
     //                        Toast.LENGTH_LONG).show();
     return true;
     case R.id.normal_map:
     isHeatmap = false;
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
**/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item = item;

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.search_id:
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
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
    **/

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
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

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
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        isLockedOn=false;
        ArrayList<String> salesList = this.getIntent().getStringArrayListExtra("salesList");
        if (salesList != null) {
            for (String s : salesList) {
                String[] tokens = s.split(",");
                List<String> newList = new ArrayList<String>();
                System.out.println("unique ref: " + tokens[0] + ", lat: " + tokens[1] + ", long: " + tokens[2]);
                newList.add(tokens[0]);
                newList.add(tokens[1]);
                newList.add(tokens[2]);
                sales.add(newList);
            }
//            createMarkers(sales);
        }
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            //add listener to update camera when the user zooms in/out
            @Override
            public void onCameraChange(CameraPosition pos) {

                if (isHeatmap) {
                    //TODO uncomment method
                    float minZoom = 10.0f;
                    if (pos.zoom < minZoom) {
                        mMap.animateCamera((CameraUpdateFactory.zoomTo(minZoom)));
                    }
                    if (!firstRequest) {
                        if (pos.zoom >= minZoom) {
                            try {
                                updateHeatmap();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
//                else if (isPinmap) {
//                    //TODO uncomment method
//                    float minZoom = 13.0f;
//                    if (pos.zoom < minZoom) {
//                        mMap.animateCamera((CameraUpdateFactory.zoomTo(minZoom)));
//                    }
////                    if (!firstRequest) {
////                        if (pos.zoom >= minZoom) {
//                            try {
//                                createMarkers();             ;
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
////                            } catch (ClassNotFoundException e) {
////                                e.printStackTrace();
////                            } catch (SQLException e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    }
                else {
                    //TODO here you put stuff to control queries for the marker view to query db when
                    //TODO the user moves the map
                }
            }
        });

        //create button listener to lock on/off user
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                isLockedOn = false;
                showUser();
                if (!isLockedOn) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                } else if (isLockedOn) {
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                }
                return true;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getTitle();
                marker.showInfoWindow();
                 // TODO: this is the unique ref, use it to query the database
                // TODO: code to query db here
//                marker.setTitle(paon + " " + saon + " " + street); // TODO: uncomment these two lines
//                marker.setSnippet("£" + price);   // TODO: and assign the right value for paon, saon, street and price
                return false;
            }
        });
    }

    public boolean  showUser() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
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
        } else {
            showUser();
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
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

    public void updateHeatmap() throws ExecutionException, InterruptedException, SQLException, ClassNotFoundException {
        //calculate necassary range to load data from screen size and zoom
        double rangeInKm = getRange(mMap);
        Location location = mCurrentLocation;
        if (rangeInKm > currentRangeInKm*1.6) {
            currentRangeInKm = rangeInKm;
            //query DB with async taks
//                new UpdateMapTask(this).execute(location,range).get();
            new UpdateMapTask(this).execute(location,currentRangeInKm);
//                new UpdateMapTask(this).execute();//currentSalesData = getDataInRange(location, rangeInKm);
//                if (data != null) {
//                    if (!data.isEmpty()) {
//                        List<SalesDataShort> places = data.getItems();
            //check if matches found
//                        if (!currentSalesData.isEmpty()) {
//                            Toast.makeText(getApplicationContext(),
//                                    String.valueOf(currentSalesData.size()+" houses found"),
//                                    Toast.LENGTH_LONG).show();
            //init add list
//                                List<SalesDataShort> items = new ArrayList<>();
//                            for (SalesDataShort d : places) {
//                                if (currentSalesData.isEmpty()) {
//                                    currentSalesData.add(d);
////                                    if (!items.contains(d)) {
////                                        items.add(d);
////                                    }
//                                } else if (!currentSalesData.contains(d)) {
//                                    currentSalesData.add(d);
//                                }
//                            }

//                            drawHeatmapOverlay();
//                        }
//                    }
            //start new task in background to pre- fetch datapoints
//            UpdateMapTask asyncTask = new UpdateMapTask(new OnTaskCompleted(){
//        };
//        asyncTask.execute(mCurrentLocation,currentRangeInKm*1.5);

//            new UpdateMapTask(this).execute(mCurrentLocation,currentRangeInKm*1.5);
        }
    }
//        }

    public void drawHeatmapOverlay(){
        ArrayList<WeightedLatLng> llList = new ArrayList<>();
        if(!currentSalesData.isEmpty()) {
            createHeatmap(currentSalesData);
        }

    }

    ///return a collection of SalesDataShort objects within a specific range
    //make sure that first parameter is the current location and the second one - the range of results in km
    public List<WeightedLatLng> getDataInRange(Location location, double range) throws ExecutionException, InterruptedException {
        List<List<Double>> temp= new UpdateMapTask(this).execute(location,range).get();
        return getWeightedFromList(temp);
//        List<WeightedLatLng> list = new ArrayList<>();
//        for(List<Double> heat:temp){
//            list.add(new WeightedLatLng(new LatLng(heat.get(0),heat.get(1)),heat.get(2)/10000));
//        }
//        return list;
    }

    public List<WeightedLatLng> getWeightedFromList(List<List<Double>> temp){
        // first normalise the price range between 1 to 30
        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
//        final double MAX_INTENSITY = 30;
//        final double MIN_INTENSITY = 1;
        // find out the min and max price of properties in the list retrieved from database
        for(List<Double> heat:temp) {
            if (heat.get(2) < minPrice) {
                minPrice = heat.get(2);
            } else if (heat.get(2) > maxPrice){
                maxPrice = heat.get(2);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
            }
        }
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
        updatePrices();
        List<WeightedLatLng> list = new ArrayList<>();
//        double min = Double.MAX_VALUE;
//        double max = Double.MIN_VALUE;
//        double sum = 0;
//        HashMap<String, Double> intensityByArea = new HashMap<String, Double>();
        for(List<Double> heat:temp){
            // calculate the normalised intensity
//            double intensity = MIN_INTENSITY + ((heat.get(2) - minPrice) * (MAX_INTENSITY - MIN_INTENSITY)) / (maxPrice - minPrice);
//            sum += intensity;
            // find out the sum of intensity of nearby places and store them in a hashmap
//            int lat = (int) (heat.get(0).doubleValue() * 10);
//            int longt = (int) (heat.get(1).doubleValue() * 10);
//            Double currentArea = intensityByArea.get(lat + "," + longt);
//            if (currentArea != null) {
//                intensityByArea.put(lat + "," + longt, currentArea + intensity);
//            } else {
//                intensityByArea.put(lat + "," + longt, intensity);
//            }

            //System.out.println("intensity: " + intensity);
//            if (intensity < min) {
//                min = intensity;
//                System.out.println("min: " + min + ", max: " + max);
//            } else if (intensity > max) {
//                max = intensity;
//                System.out.println("min: " + min + ", max: " + max);
//            }
            list.add(new WeightedLatLng(new LatLng(heat.get(0),heat.get(1)), heat.get(2)));
        }

        // find out the area with the greatest sum of intensity and use that sum/10 as the intensity of the dummy weighted lat long
//        double biggestArea = 0;
//        System.out.println("List Size: " + temp.size());
//        System.out.println("Map Size: " + intensityByArea.values().size());
//        for (Double d: intensityByArea.values()) {
//            System.out.println("d: " + d);
//            if (d > biggestArea) {
//                biggestArea = d;
//            }
//        }
//        System.out.println("biggestArea: " + biggestArea);
//        dummy = new WeightedLatLng(new LatLng(54, -1), biggestArea / 10);
        // only display the dummy if the user zoomed out past level 13
//        if (mMap.getCameraPosition().zoom < 13) {
//            list.add(dummy);
//            System.out.println("add dummy");
//        }
        return list;
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
        return true;
    }

    @Override
    //Update as the device is moving
    public void onLocationChanged(Location location) {
        if(isBetterLocation(location)) {
            mCurrentLocation = location; //update current location
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());  //get last update time
        }
        if (mCurrentLocation == null) {
            checkGPS();
        }
        if(firstRequest) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            firstRequest = false;
        }else if(isHeatmap){
            try {
                updateHeatmap();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(isLockedOn) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));         }
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     */
    protected boolean isBetterLocation(Location location) {
        Location currentBestLocation= mCurrentLocation;
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TEN_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -TEN_SECONDS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate) {
            return true;
        }
        return false;
    }

    //  create heatmap using the list taken from a parameter
    public boolean createHeatmap(List<WeightedLatLng> pointsList) {
        HeatmapTileProvider tProvider = null;
        if (mMap.getCameraPosition().zoom<13){
        tProvider = new HeatmapTileProvider.Builder().weightedData(pointsList).
                radius(10
//                        * (int)mMap.getCameraPosition().zoom-40)
                ).build()
                ;
        }
        else if(mMap.getCameraPosition().zoom<15){
            tProvider = new HeatmapTileProvider.Builder().weightedData(pointsList).
                radius(20
//                        * (int)mMap.getCameraPosition().zoom-40)
                ).build()
                ;
    }
        else {
            tProvider = new HeatmapTileProvider.Builder().weightedData(pointsList).
                    radius(50).build();
        }

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
    public void sendData(List<List<Double>> list) {
        if(isHeatmap) {
            if (list != null) {
                this.currentSalesData = this.getWeightedFromList(list);
                clearHeatmap();
                createHeatmap(currentSalesData);
                Toast.makeText(getApplicationContext(),
                        "Done retrieving results",
                        Toast.LENGTH_LONG).show();
//                updateSalesData(currentSalesData);
            }
        }

    }

    private void updateSalesData(List<WeightedLatLng>salesData){
        sales.clear();
        for (WeightedLatLng s : salesData){
            List<String> data = new ArrayList<>();
            data.add(" ");
            data.add(String.valueOf(s.getPoint().x));
            data.add(String.valueOf(s.getPoint().y));
            sales.add(data);
        }
    }

    private void selectItemFromDrawer(int position) throws ExecutionException, InterruptedException {


        switch (position) {
            case 0:
                isHeatmap = true;
//                mMap.clear();
                removeMarkers();
                Toast toast = Toast.makeText(MapsActivity.this,drawerItems[position], Toast.LENGTH_LONG);
                toast.show();
                showUser();
                if (mMap.getCameraPosition().zoom < 10) {
                    mMap.animateCamera((CameraUpdateFactory.zoomTo(10)));
                }
                createHeatmap(currentSalesData);
                try {
                    updateHeatmap();    //querries the DB to update the heatmap
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case 1:
                currentRangeInKm=0;

                isHeatmap = false;
                isPinmap = false;
                removeMarkers();
                Toast toast1 = Toast.makeText(MapsActivity.this,drawerItems[position], Toast.LENGTH_LONG);
                toast1.show();
                clearHeatmap();
//                removeMarkers();
//                createMarkers(sales);
                break;
            case 2:
                isPinmap=true;
                currentRangeInKm = 0;
                if (mMap.getCameraPosition().zoom < 13) {
                    mMap.animateCamera((CameraUpdateFactory.zoomTo(13)));
                }
                Toast toast2 = Toast.makeText(MapsActivity.this,drawerItems[position], Toast.LENGTH_LONG);
                toast2.show();
                isHeatmap = false;
                isPinmap = true;
                clearHeatmap();
                removeMarkers();
                createMarkers();
            default:
                break;
        }
        mDrawerLayout.closeDrawers();

        /**
         Fragment fragment = new PreferencesFragment();

         FragmentManager fragmentManager = getSupportFragmentManager();
         fragmentManager.beginTransaction()
         .replace(R.id.mainContent, fragment)
         .commit();


         mDrawerList.setItemChecked(position, true);
         setTitle(mNavItems.get(position).mTitle);

         // Close the drawer
         mDrawerLayout.closeDrawer(mDrawerPane);
         **/
    }

    public List<List<String>> searchSales(String paon, String saon, String street, String town, String postcode) throws ExecutionException, InterruptedException {
        if  (paon == null) {
            paon = "";
        } else {
            paon = paon.toUpperCase();
        }
        if  (saon == null) {
            saon = "";
        } else {
            saon = saon.toUpperCase();
        }
        if  (street == null) {
            street = "";
        } else {
            street = street.toUpperCase();
        }
        if (town != null) {
            town = town.toUpperCase();
        }
        if  (postcode == null) {
            postcode = "";
        } else if (!postcode.equals("") && postcode.length() >= 5){
            postcode = postcode.toUpperCase();
            // properly format the postcode
            String outwardCode = postcode.substring(0, postcode.length() - 3);
            outwardCode = outwardCode.trim();
            String inwardCode = postcode.substring(postcode.length() - 3);
            postcode = outwardCode + " " + inwardCode;
        }
        List<List<String>> temp= new SearchSalesTask(this).execute(paon, saon, street, town, postcode).get();
        System.out.println("List: " + temp.size());
        if (!temp.isEmpty() && temp.size() <= 100) {

        } else if (temp.isEmpty()) {
            Toast.makeText(MapsActivity.this,"No result found", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MapsActivity.this,"Too many results found, please refine your search criteria", Toast.LENGTH_LONG).show();
        }
        sales = temp;
        return temp;
    }

    private void createMarkers() throws ExecutionException, InterruptedException {
//        if (!sales.isEmpty()) {
//            isHeatmap = false;
//            clearHeatmap();
//        }
        double rangeInKm = getRange(mMap);
        Location location = mCurrentLocation;
        if (rangeInKm > currentRangeInKm*1.6) {
            currentRangeInKm = rangeInKm;
//            double xOffset = -0.0001;
//            double yOffset = -0.0001;
            List<List<String>> l = new MarkerTask(this).execute(location, rangeInKm).get();
            if (l != null) {
                for (int i = 0; i < l.size(); i++) {
                    double lat = Double.parseDouble(l.get(i).get(1));// + xOffset;
                    double lng = Double.parseDouble(l.get(i).get(2));// + yOffset;

//                double lat = Double.parseDouble(l.get(i).get(1));
//                double lng = Double.parseDouble(l.get(i).get(2));
//                    xOffset += 0.0001;
//                    if (i % 10 == 0) {
//                        yOffset += 0.0001;
//                    }

                    mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(l.get(i).get(5) + "-" + l.get(i).get(6) + " " + l.get(i).get(7) + ", " +
                                            l.get(i).get(8))
//                                .snippet("aasdfasd")
//                                .snippet()
                                    .snippet("Price: " + l.get(i).get(4))
                    );// + " " + sales.get(i).get(4) + " " + sales.get(i).get(5))
//                    .snippet("£" + sales.get(i).get(6)));
                }
            }
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        intent.putExtra("method", "searchSales");
//        intent.putExtra("houseNumberText", houseNumberText);
//        intent.putExtra("flatNumberText", FlatNumberText);
//        intent.putExtra("StreetText", StreetText);
//        intent.putExtra("CityText", CityText);
//        intent.putExtra("PostCodeText", PostCodeText);

//        super.onNewIntent(intent);
//        if(intent.getStringExtra("methodName").equals("searchSales")){
//            String houseNumberText = intent.getStringExtra("houseNumberText");
//            String flatNumberText = intent.getStringExtra("flatNumberText");
//            String streetText = intent.getStringExtra("StreetText");
//            String cityText = intent.getStringExtra("CityText");
//            String postsCodeText = intent.getStringExtra("PostsCodeText");
//            this.isHeatmap =false;
//
//            try {
//                searchSales(houseNumberText,flatNumberText,streetText,cityText,postsCodeText);
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    private void removeMarkers() {
        markerList.clear();
//        for (Marker m :
                mMap.clear();
//        {
//            m.remove();
//        }
//        markerList = new ArrayList<Marker>();
    }


//    @Override
//    public void onTaskCompleted(SalesDataShortCollection output) {
//        if(((SalesDataShortCollection) output).getItems().size()>currentSalesData.getItems().size()){
//            currentSalesData=(SalesDataShortCollection)output;
//            currentRangeInKm=get
//        }
//    }



    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;
        String[] drawerItems;
        String[] subtile;
        int[] images= {R.mipmap.heatmap , R.mipmap.normalmap,R.mipmap.pinmap};


        public DrawerListAdapter(Context context) {
            mContext = context;
            drawerItems = context.getResources().getStringArray(R.array.nav_drawer_items);
            subtile = context.getResources().getStringArray(R.array.subtile_items);
            //subtile_items
            //mNavItems = navItems;
        }


        @Override
        public int getCount() {
            return drawerItems.length;
        }

        @Override
        public Object getItem(int position) {
            return  drawerItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //view = inflater.inflate(R.layout.drawer_item, null);
                view=  inflater.inflate(R.layout.drawer_item,parent, false);

            }
            else {
                view = convertView;
            }

            /**
             *
             * View row = null;
             if(convertView == null){
             LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             row=  mInflater.inflate(R.layout.custom_xml,arg2, false);


             }
             else{
             row = convertView;
             }


             ImageView imgIcon = (ImageView) row.findViewById(R.id.imageView1);
             TextView txtTitle = (TextView) row.findViewById(R.id.textView1);

             txtTitle.setText(drawerItems[position]);
             imgIcon.setImageResource(images[position]);
             *
             */


            TextView txtTitle = (TextView) view.findViewById(R.id.title);
            TextView subTitle = (TextView) view.findViewById(R.id.subTitle);
            ImageView imgIcon = (ImageView) view.findViewById(R.id.icon);

            txtTitle.setText(drawerItems[position]);
            subTitle.setText(subtile[position]);
            imgIcon.setImageResource(images[position]);
            /**

             TextView titleView = (TextView) view.findViewById(R.id.title);
             TextView subtitleView = (TextView) view.findViewById(R.id.subtitle);
             ImageView iconView = (ImageView) view.findViewById(R.id.icon);

             titleView.setText( mNavItems.get(position).mTitle );
             subtitleView.setText( mNavItems.get(position).mSubtitle );
             iconView.setImageResource(mNavItems.get(position).mIcon);
             **/

            return view;
        }
    }



}
