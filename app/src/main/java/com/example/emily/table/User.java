package com.example.emily.table;

import android.net.Uri;
import android.os.Parcelable;

import java.util.ArrayList;

public class User{

    private String id;
    private String name;
    private String firstName;
    private ArrayList<String> tables;
    private String bio;
    private Uri pictureURL;

    public User(String id, String name, String firstName, Uri pic) {
        this.id = id;
        this.pictureURL = pic;
        this.name = name;
        this.firstName = firstName;
    }

    public User(){}

    public String getId(){return id;}
    public String getName(){return name;}
    public String getFirstName(){return firstName;}
    public Uri getPic(){return pictureURL;}

}
