/**
 * Created by Luke on 08/11/2015.
 */

//import com.example.rai.myapplication.backend.IndexingServlet;
import com.example.rai.myapplication.backend.MyServlet;
import com.example.rai.myapplication.backend.NearPlacesFinder;
import com.example.rai.myapplication.backend.OfyService;
import com.example.rai.myapplication.backend.SalesInformationEndpoint;
import com.example.rai.myapplication.backend.UserLocationEndpoint;
import com.example.rai.myapplication.backend.model.SalesDataShort;
import com.google.appengine.api.search.GeoPoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class unitTests {

    //IndexingServlet indexingServlet;
    MyServlet myServlet;
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
        myServlet = new MyServlet();
        assertNotNull(myServlet);

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

    @Test
    public void testSalesGetIDBounds() throws Exception {
        //Finding whether or not the correct SalesData list has been returned (within the right KM distance).
        //Important to note that both search range in KM & house return limit are both constrictions on data return.
        //Both can override each other, which is fine within the context of this program.
        //Should return
        sales = new SalesInformationEndpoint();
        List<SalesDataShort> methodOutput = sales.getPointsInRange("53.724276876242600", "-1.269308676070620", 2, 1000);

        //INSIDE 2KM AREA = Vale Walk, Knottingley (53.7075914,-1.2656739000000243)
        //OUTSIDE 2KM AREA = Cleveland Avenue, Knottingley (53.70670579999999,-1.2518277000000353)

        for(SalesDataShort sales : methodOutput){

            if(sales.getLocationGeo().getLatitude() == 53.7075914 && sales.getLocationGeo().getLongitude() == -1.2656739000000243){
                assertTrue(1==1);
            }
            if(sales.getLocationGeo().getLatitude() == 53.70670579999999 && sales.getLocationGeo().getLongitude() == -1.2518277000000353) {
                assertFalse(1==1);
            }
        }
    }

}
