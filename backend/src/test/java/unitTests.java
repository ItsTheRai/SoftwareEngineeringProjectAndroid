/**
 * Created by Luke on 08/11/2015.
 */

import com.example.rai.myapplication.backend.IndexingServlet;
import com.example.rai.myapplication.backend.NearPlacesFinder;
import com.example.rai.myapplication.backend.OfyService;
import com.example.rai.myapplication.backend.SalesInformationEndpoint;
import com.example.rai.myapplication.backend.UserLocationEndpoint;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
}
