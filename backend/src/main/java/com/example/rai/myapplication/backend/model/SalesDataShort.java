package com.example.rai.myapplication.backend.model;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 03/11/15.
 */
@Entity
public class SalesDataShort {
    @Id
    private Long id;
    String transId;
    int priceInPouds;
    GeoPt location;

    public SalesDataShort(){
    }

    public SalesDataShort(String transId, int priceInPouds, GeoPt location) {
        this.transId = transId;
        this.priceInPouds = priceInPouds;
        this.location = location;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public int getPriceInPouds() {
        return priceInPouds;
    }

    public void setPriceInPounds(int priceInPouds) {
        this.priceInPouds = priceInPouds;
    }

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
