package com.uni.rai.softwareengineeringproject;

import android.app.Instrumentation;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
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
/**
 * Created by arsalanmerchant on 08/11/2015.
 */
public class MapsActivityTest  extends ActivityInstrumentationTestCase2<MapsActivity> {

    MapsActivity mapsActivity;
    private Context mContext;


    public MapsActivityTest(Context mContext) {
        super(MapsActivity.class);
        this.mContext = mContext;

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mapsActivity = getActivity();
    }


    @SmallTest
    public void testActivityExists() {
        assertNotNull(mapsActivity);
    }
/**
    @SmallTest
    public void checkScreenPageTest() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();


        if (netInfo == null) {
            Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
            .addMonitor(MapsActivity.class.getName(), null, false);


            AlertDialog dialog = mapsActivity.getLastDialog();
            // I create getLastDialog method in MyActivity class. Its return last created AlertDialog
            if (dialog.isShowing()) {
                try {
                    performClick(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }


        }
    }
**/

/**
    private void performClick(Button button) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }
**/








}
