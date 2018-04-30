package com.example.emily.table;

import java.io.Serializable;

/**
 * Created by drewdearing on 4/4/18.
 */

public class Restaurant implements Serializable{

    public String name;

    public String photo;

    public String address;

    public Restaurant () {}

    public Restaurant (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {return address;}

}
