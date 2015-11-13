package com.uni.rai.softwareengineeringproject;

import android.test.ActivityInstrumentationTestCase2;


import java.lang.Exception;
import java.lang.Override;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.content.Intent;
import junit.framework.Assert;
import android.app.Instrumentation;
import android.app.Activity;
import android.test.UiThreadTest;
import android.app.Instrumentation.ActivityMonitor;
import android.location.Location;
import android.location.LocationManager;

import android.net.wifi.WifiManager;


public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mainActivity;


    public ApplicationTest() {
        super(MainActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
    }


    /**
     * Check if the main activity is exist
     *
     */
    @SmallTest
    public void testActivityExists() {
        assertNotNull(mainActivity);
    }


    /**
     * Check to see the button is exist
     *
     */
    @SmallTest
    public void testButtonExists() {
        Button button = (Button) mainActivity.findViewById(R.id.button);
        assertNotNull(button);
    }

    /**
     * when you click on the button the button should work
     */
    @SmallTest
    public void testStartActivity()throws Exception {
        ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MapsActivity.class.getName(), null, false);
        MainActivity myActivity = getActivity();
        final Button button = (Button) mainActivity.findViewById(R.id.button);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        MapsActivity nextActivity = (MapsActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }





/**
    public void testMaidenhead() {
        // this is a single test which doesn't really validate the algorithm
        // identifying a bunch of edge cases would do that
        publishMockLocation();
        final String expectedMH = "CM87wk";


        // final String actualMH = mActivity.gridSquare(mLocation);
        assertEquals(expectedMH, actualMH);
    }


    protected void publishMockLocation() {
        final double TEST_LONGITUDE = -122.084095;
        final double TEST_LATITUDE = 37.422006;
        final String TEST_PROVIDER = "test";
        final Location mLocation;
        final LocationManager mLocationManager;

        mLocationManager = (LocationManager) MapsActivity.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager.getProvider(TEST_PROVIDER) != null) {
            mLocationManager.removeTestProvider(TEST_PROVIDER);
        }
        if (mLocationManager.getProvider(TEST_PROVIDER) == null) {
            mLocationManager.addTestProvider(TEST_PROVIDER,
                    false, //requiresNetwork,
                    false, // requiresSatellite,
                    false, // requiresCell,
                    false, // hasMonetaryCost,
                    false, // supportsAltitude,
                    false, // supportsSpeed,
                    false, // supportsBearing,
                    android.location.Criteria.POWER_MEDIUM, // powerRequirement
                    android.location.Criteria.ACCURACY_FINE); // accuracy
        }
        mLocation = new Location(TEST_PROVIDER);
        mLocation.setLatitude(TEST_LATITUDE);
        mLocation.setLongitude(TEST_LONGITUDE);
        mLocation.setTime(System.currentTimeMillis());
        mLocation.setAccuracy(25);
        mLocationManager.setTestProviderEnabled(TEST_PROVIDER, true);
        mLocationManager.setTestProviderStatus(TEST_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        mLocationManager.setTestProviderLocation(TEST_PROVIDER, mLocation);
    }




**/















}


