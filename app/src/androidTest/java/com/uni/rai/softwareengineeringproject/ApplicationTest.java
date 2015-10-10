package com.uni.rai.softwareengineeringproject;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.uni.rai.softwareengineeringproject.MapsActivity;

import org.junit.runner.RunWith;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    private MapsActivity mapsActivity;
    @Before
    public void setup() {
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        activity = Robolectric.setupActivity(MapsActivity.class);
    }

    @Test
    public void testStart(){
        assertEquals(1,1);
    }
}
