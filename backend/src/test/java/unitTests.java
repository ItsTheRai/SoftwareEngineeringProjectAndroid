/**
 * Created by Luke on 08/11/2015.
 */

import com.example.rai.myapplication.backend.IndexingServlet;

import org.junit.Test;

import static org.junit.Assert.*;

public class unitTests {

    IndexingServlet indexingServlet;

    //Checks to see if IndexServlet does not return null.
    @Test
    public void checkIndexServlet()
    {
        indexingServlet = new IndexingServlet();
        assertNotNull(indexingServlet);

    }

    //Checks to see if IndexServlet returns null.  Should fail.
    @Test
    public void checkNull()
    {
        indexingServlet = new IndexingServlet();
        assertNull(indexingServlet);
    }
}
