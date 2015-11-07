package com.example.rai.myapplication.backend.model;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 28/10/15.
 */
@Entity
public class SalesData {
    @Id
    private Long id;
    private String UniqueRef;
    private int price;
    private DateTime dateTime;
    private String postcode;
    private String propertyType;
    private String oldOrNew;
    private String duration;
    private String PAON;
    private String SAON;
    private String street;
    private String locality;
    private String town;
    private String district;
    private String  county;
    private String PPDCategory;
    private float latitude;
    private float longitude;
    private GeoPt locationGeo;
//    private GeoPt location;

    public SalesData(){
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getUniqueRef() {
        return UniqueRef;
    }

    public void setUniqueRef(String uniqueRef) {
        UniqueRef = uniqueRef;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public DateTime getDate() {
        return dateTime;
    }

    public void setDate(DateTime date) {
        dateTime = date;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getOldOrNew() {
        return oldOrNew;
    }

    public void setOldOrNew(String oldOrNew) {
        this.oldOrNew = oldOrNew;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPAON() {
        return PAON;
    }

    public void setPAON(String PAON) {
        this.PAON = PAON;
    }

    public String getSAON() {
        return SAON;
    }

    public void setSAON(String SAON) {
        this.SAON = SAON;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPPDCategory() {
        return PPDCategory;
    }

    public void setPPDCategory(String PPDCategory) {
        this.PPDCategory = PPDCategory;
    }


    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocation(GeoPt location) {
        this.locationGeo = location;
    }
}
