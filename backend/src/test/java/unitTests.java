/**
 * Created by Luke on 08/11/2015.
 */

import com.example.rai.myapplication.backend.IndexingServlet;
import com.example.rai.myapplication.backend.NearPlacesFinder;
import com.example.rai.myapplication.backend.OfyService;
import com.example.rai.myapplication.backend.SalesInformationEndpoint;
import com.example.rai.myapplication.backend.UserLocationEndpoint;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.search.GeoPoint;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class unitTests {

    IndexingServlet indexingServlet;
    NearPlacesFinder placefinder;
    OfyService ofy;
    SalesInformationEndpoint sales;
    UserLocationEndpoint user;

    //Checks to see if IndexServlet does not return null.
    @Test
    public void checkIndexServlet() {
        indexingServlet = new IndexingServlet();
        assertNotNull(indexingServlet);

    }
    /**
    //Checks to see if IndexServlet returns null.  Should fail.
    @Test
    public void checkIndexNull() {
        indexingServlet = new IndexingServlet();
        assertNull(indexingServlet);
    }
    **/

    //Checks to see if NearPlacesFinder does not return null.
    @Test
    public void checkNearPlaces()
    {
        placefinder = new NearPlacesFinder();
        assertNotNull(placefinder);

    }

    /**
    //Checks to see if NearPlacesFinder does returns null.  Should fail
    @Test
    public void checkNearNull()
    {
        placefinder = new NearPlacesFinder();
        assertNull(placefinder);
    }
    **/

    //Checks to see if OfyService does not return null
    @Test
    public void checkOfyService()
    {
        ofy = new OfyService();
        assertNotNull(ofy);
    }

    /**
    //Checks to see if OfyService returns null.  Should fail.
    @Test
    public void checkOfyNull()
    {
        ofy = new OfyService();
        assertNull(ofy);
    }
    **/

    //Checks to see if checkSales does not return null.
    @Test
    public void checkSales()
    {
        sales = new SalesInformationEndpoint();
        assertNotNull(sales);
    }

    /**
    //Checks to see if checkSalesNull does return null.  Should fail
    @Test
    public void checkSalesNull()
    {
        sales = new SalesInformationEndpoint();
        assertNull(sales);
    }
    **/

    //Checks to see if checkUserLocation does not return null.
    @Test
    public void checkUserLocation()
    {
        user = new UserLocationEndpoint();
        assertNotNull(user);
    }

    /**
    //Checks to see if checkUserNull does return null.  Should fail
    @Test
    public void checkUserNull()
    {
        user = new UserLocationEndpoint();
        assertNull(user);
    }
    **/

    //Checks that CheckBuildDocument() returns Long, String, Int, GeoPoint
    @Test
    public void checkBuildDocument()
    {
        placefinder = new NearPlacesFinder();

        assertNotNull(NearPlacesFinder.buildDocument(0L, "", 1, new GeoPoint(51.5034070, -0.1275920)));

    }

    //Checks to see if the getPlaces returns correctly.
    //Returns null pointer.
    @Test
    public void checkGetPlaces()
    {
        placefinder = new NearPlacesFinder();

        assertNotNull(placefinder.getPlaces(new GeoPt(51f, -1f), 10000000L, 1));
    }

    //Check to see if the distance between two locations return something.
    @Test
    public void checkGetDistance()
    {
        placefinder= new NearPlacesFinder();

        assertNotNull(placefinder.getDistanceInKm(51.5034070, -0.1275920, 50.8646070,-0.0828680));
    }
}
