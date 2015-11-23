package com.example.rai.myapplication.backend.model;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 03/11/15.
 */
public class SalesDataShort {
    String id;
    double price;
    GeoPt locationGeo;
    double distanceInKilometers;

    public SalesDataShort(){
    }

    public SalesDataShort(String id, int price, GeoPt locationGeo, double distanceInKilometers) {
        this.id = id;
        this.price = price;
        this.locationGeo = locationGeo;
        this.distanceInKilometers = distanceInKilometers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public GeoPt getLocationGeo() {
        return locationGeo;
    }

    public void setLocationGeo(GeoPt locationGeo) {
        this.locationGeo = locationGeo;
    }

    public double getDistanceInKilometers() {
        return distanceInKilometers;
    }

    public void setDistanceInKilometers(double distanceInKilometers) {
        this.distanceInKilometers = distanceInKilometers;
    }
}
