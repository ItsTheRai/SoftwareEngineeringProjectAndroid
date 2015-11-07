package com.example.rai.myapplication.backend;

//import com.example.rai.myapplication.backend.model.UserLocation;
import com.example.rai.myapplication.backend.model.UserLocation;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import static com.example.rai.myapplication.backend.OfyService.ofy;

import java.util.logging.Logger;

/**
 * An endpoint class we are exposing
 */
@Api(
//        root = "http://127.0.0.1:8080/_ah/api", //for local server only TODO remove this line before deploying
        name = "userLocationApi",
        version = "v1",
        resource = "userLocation",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.rai.example.com",
                ownerName = "backend.myapplication.rai.example.com",
                packagePath = ""
        )
)
public class UserLocationEndpoint {

    private static final Logger logger = Logger.getLogger(UserLocationEndpoint.class.getName());

    /**
     * This method gets the <code>UserLocation</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>UserLocation</code> associated with <code>id</code>.
     */
//    @ApiMethod(name = "getAUserLocation")
//    public UserLocation getUserLocation(@Named("id") Long id) {
//        // TODO: Implement this function
//        logger.info("Calling getUserLocation method");
//        return null;
//    }


    /**
     * This inserts a new <code>UserLocation</code> object.
     *
     * @param location The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "updateUserLocation")
    public UserLocation updateUserLocation(UserLocation location) {
        // TODO: Implement this function
        UserLocation l = ofy().load().type(UserLocation.class).filter("Id", location.getId()).first().now();
        if (l!=null) {
            l.setLatitude(location.getLatitude());
            l.setLongitude(location.getLongitude());
        }
        else{
            ofy().save().entity(location).now();
        }//TODO uncomment all
        ofy().save().entity(new UserLocation()).now();//location.getLatitude(), location.getLongitude())).now();
        logger.info("Calling insertUserLocation method");
        return null;
    }
}
