package com.example.rai.myapplication.backend.model;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by rai on 28/10/15.
 */
@Entity
public class SalesData {
    @Id
    private Long id;
    private String County;
    //cant persist datetime object
    private String Datetime;
    private String District;
    private String Duration;
    private double Latitude;
    private String Locality;
    private double Longitude;
    private String Old_or_new;
    private String PAON;
    private String Postcode;
    private long Price;
    private String Property_type;
    private String SAON;
    private String Street;
    private String Town;
    private String UniqueRef;
    private String PDD_category;

    public SalesData() {
    }

    public SalesData(Long id, String county, String datetime, String district, String duration, float latitude, String locality, float longitude, String old_or_new, String PAON, String postcode, int price, String property_type, String SAON, String street, String town, String uniqueRef, String PDD_category) {
        this.id = id;
        County = county;
        Datetime = datetime;
        District = district;
        Duration = duration;
        Latitude = latitude;
        Locality = locality;
        Longitude = longitude;
        Old_or_new = old_or_new;
        this.PAON = PAON;
        Postcode = postcode;
        Price = price;
        Property_type = property_type;
        this.SAON = SAON;
        Street = street;
        Town = town;
        UniqueRef = uniqueRef;
        this.PDD_category = PDD_category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueRef() {
        return UniqueRef;
    }

    public void setUniqueRef(String uniqueRef) {
        UniqueRef = uniqueRef;
    }

    public long getPrice() {
        return Price;
    }

    public void setPrice(long price) {
        Price = price;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public String getProperty_type() {
        return Property_type;
    }

    public void setProperty_type(String property_type) {
        Property_type = property_type;
    }

    public String getOld_or_new() {
        return Old_or_new;
    }

    public void setOld_or_new(String old_or_new) {
        Old_or_new = old_or_new;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
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
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getPDD_category() {
        return PDD_category;
    }

    public void setPDD_category(String PPD_category) {
        this.PDD_category = PPD_category;
    }
}