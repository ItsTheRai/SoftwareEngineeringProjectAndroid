package com.example.rai.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 28/10/15.
 */
@Entity
public class SalesInformation {
    @Id
    private Long id;
    private String UniqueRef;
    private String price;
    private String Date;
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
    private double latitude;
    private double longitude;

    public SalesInformation(){
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
