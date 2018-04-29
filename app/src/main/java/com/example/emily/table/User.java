package com.example.emily.table;

import android.net.Uri;
import android.os.Parcelable;

import java.util.ArrayList;

public class User{

    private String id;
    private String name;
    private String firstName;
    private String bio;
    private String pic;

    public User(String id, String name, String firstName, String pic) {
        this.id = id;
        this.pic = pic;
        this.name = name;
        this.firstName = firstName;
        this.bio = "";
    }

    public User(String id, String name, String firstName, String pic, String bio) {
        this.id = id;
        this.pic = pic;
        this.name = name;
        this.firstName = firstName;
        this.bio = bio;
    }

    public User(User u, String bio){
        this.id = u.getId();
        this.pic = u.getPic();
        this.name = u.getName();
        this.firstName = u.getFirstName();
        this.bio = bio;
    }

    public User(){}

    public String getId(){return id;}
    public String getName(){return name;}
    public String getFirstName(){return firstName;}
    public String getPic(){return pic;}
    public String getBio(){return bio; }
}
