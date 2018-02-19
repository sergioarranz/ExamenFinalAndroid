package com.utad.sergio.examenandroid;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by sergio on 19/2/18.
 */

public class Profile {

    public String name;
    public double lat, lon;
    public int number;
    public Marker profileMarker;

    public Profile(){
    }

    public Profile(String name, double lat, double lon, int number){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.number = number;
    }

    public void setMarker(Marker marker){
        this.profileMarker = marker;
    }

    public Marker getMarker(){
        return profileMarker;
    }
}
