package com.example.emily.table;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Form extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ImageView photoView;
    private ActionBar actionBar;
    private Button submitButton;
    private String restaurantName;
    private String photoURL;
    private String userId;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);
        photoView = findViewById(R.id.form_image);
        submitButton = findViewById(R.id.form_submit);
        actionBar = getSupportActionBar();
        Intent restaurantInfo = getIntent();
        restaurantName = restaurantInfo.getExtras().getString("name");
        photoURL = restaurantInfo.getExtras().getString("photo");
        userId = restaurantInfo.getExtras().getString("userId");
        actionBar.setTitle(restaurantName);
        Glide
                .with(getApplicationContext())
                .load(photoURL)
                .dontTransform()
                .into(photoView);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //need to handle user trees and random ids.
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference();
                key = myRef.push().getKey();
                myRef.child("Tables").child(key).setValue(initTable());
                //add creator onto the guest list
                myRef.child("Tables").child(key).child("Guests").child(userId).setValue(userId);
                onBackPressed();
            }
        });

    }

    private Table initTable() {
        AppCompatEditText nameView = (AppCompatEditText) findViewById(R.id.form_name);
        String name = nameView.getText().toString();
        AppCompatEditText descView = (AppCompatEditText) findViewById(R.id.form_text);
        String desc = descView.getText().toString();
        //Create Restaurant object
        Restaurant r = new Restaurant(restaurantName);
        r.photo = photoURL;

        //Create new table object
        Table table = new Table(name, userId, key);
        table.description = desc;
        table.restaurant = r;
        addUser(table);
        return table;
    }

    private void addUser(final Table t){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot userdata = dataSnapshot.child("Users").child(userId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }
}
