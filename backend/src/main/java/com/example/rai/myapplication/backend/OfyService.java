package com.example.rai.myapplication.backend;

/**
 * Created by rai on 21/10/15.
 */
import com.example.rai.myapplication.backend.model.SalesInformation;
//import com.example.rai.myapplication.backend.model.UserLocation;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
//        ObjectifyService.register(UserLocation.class);
        ObjectifyService.register(SalesInformation.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}