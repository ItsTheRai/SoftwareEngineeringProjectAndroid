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
import org.junit.After;
import org.junit.Test;


import static org.junit.Assert.*;

public class unitTests {

    IndexingServlet indexingServlet;
    NearPlacesFinder placeFinder;
    OfyService ofy;
    SalesInformationEndpoint sales;
    UserLocationEndpoint user;

    @Before

    public void before()
    {
        System.out.println("Initiating test...");
    }

    @After
    public void after()
    {
        System.out.println("End of test.");
    }

    //Checks to see if IndexServlet does not return null.
    @Test
    public void checkIndexServlet() {
        System.out.println("checkIndexServlet() Test");
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
        System.out.println("checkNearPlaces() Test");
        placeFinder = new NearPlacesFinder();
        assertNotNull(placeFinder);

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
        System.out.println("checkOfyService() Test");
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
        System.out.println("checkSales() Test");
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
        System.out.println("checkUserLocation() Test");
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

    //Checks that CheckBuildDocument() builds correctly with  Long, String, Int, GeoPoint.
    @Test
    public void checkBuildDocument()
    {
        System.out.println("checkBuildDocument() Test");
        placeFinder = new NearPlacesFinder();

        assertNotNull(NearPlacesFinder.buildDocument(0L, "", 1, new GeoPoint(51.5034070, -0.1275920)));

    }

    //Checks to see if the getPlaces returns correctly.
    //Returns null pointer - cannot access Google app engine.
    /**
    @Test
    public void checkGetPlaces()
    {
        placefinder = new NearPlacesFinder();

        assertNotNull(placefinder.getPlaces(new GeoPt(51f, -1f), 10000000L, 1));
    }
    **/

    //Check to see if the distance between two locations returns.
    @Test
    public void checkGetDistance()
    {
        System.out.println("checkGetDistance() Test");
        placeFinder= new NearPlacesFinder();

        assertNotNull(placeFinder.getDistanceInKm(51.5034070, -0.1275920, 50.8646070,-0.0828680));
    }
}
