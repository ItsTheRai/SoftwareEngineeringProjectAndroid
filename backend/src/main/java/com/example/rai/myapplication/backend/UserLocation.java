package com.example.rai.myapplication.backend;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by rai on 21/10/15.
 */
@Entity
public class UserLocation {
    @Id
    private Long id;
    private double longitude;
    private double latitude;


    public UserLocation(){

    }
    public UserLocation(double latitude,double longitude){
//        UserLocation a = new UserLocation(new Long(1),new Long(4));
        this.latitude=latitude;
        this.longitude=longitude;

    }
    public Long getId(){
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
//    public void setId(Long id){
//        this.id=id;
//    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
