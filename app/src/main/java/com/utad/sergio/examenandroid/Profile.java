package com.utad.sergio.examenandroid;

/**
 * Created by sergio on 19/2/18.
 */

public class Profile {

    public String name;
    public double lat, lon;
    public int number;

    public Profile(){
    }

    public Profile(String name, double lat, double lon, int number){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.number = number;
    }
}
