package com.example.rai.myapplication.backend.model;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 03/11/15.
 */
public class SalesDataShort {
    Long id;
    int price;
    GeoPt locationGeo;

    public SalesDataShort(){
    }

    public SalesDataShort(Long transId, int priceInPouds, GeoPt location) {
        this.id = transId;
        this.price = priceInPouds;
        this.locationGeo = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrice() {
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
}
