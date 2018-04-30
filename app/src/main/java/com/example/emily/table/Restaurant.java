package com.example.emily.table;

import java.io.Serializable;

/**
 * Created by drewdearing on 4/4/18.
 */

public class Restaurant implements Serializable{

    public String name;

    public String photo;

    public String address;

    public double lat;

    public double lon;

    public Restaurant () {}

    public Restaurant (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {return address;}

    public double getLat() {return lat;}

    public double getLon() {return lon;}
}
