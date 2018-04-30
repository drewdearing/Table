package com.example.emily.table;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by emily on 4/2/18.
 * Used to represent a group of people eating together at a restaurant
 */

public class Table implements Serializable{

    public String name; //event or table name
    public String description;
    public Restaurant restaurant;
    public String userId;
    public String tableId;

    public Table () {
    }

    public Table (String name, String id, String tableId) {
        this.name = name;
        this.userId = id;
        this.tableId = tableId;
    }
    public String getName() {return name;}
    public String getDescription() {return description; }
    public Restaurant getRestaurant() {return restaurant;}
    public String getUserId(){return userId;}
    public String getTableId(){return tableId;}

}
