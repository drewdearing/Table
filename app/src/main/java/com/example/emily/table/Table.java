package com.example.emily.table;

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
    public ArrayList<Guest> guests;

    public Table () {
        guests = new ArrayList<Guest>();
    }

    public Table (String name) {
        guests = new ArrayList<Guest>();
        this.name = name;
    }

    public String getName() {return name;}
    public String getDescription() {return description; }
    public Restaurant getRestaurant() {return restaurant;}
    public ArrayList<Guest> getGuests() {return guests; }
}
