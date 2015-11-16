package com.example.rai.myapplication.backend.model;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 03/11/15.
 */
public class SalesDataShort {
    Long id;
    double price;
    GeoPt locationGeo;
    double distanceInKilometers;

    public SalesDataShort(){
    }

    public SalesDataShort(Long id, int price, GeoPt locationGeo, double distanceInKilometers) {
        this.id = id;
        this.price = price;
        this.locationGeo = locationGeo;
        this.distanceInKilometers = distanceInKilometers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
