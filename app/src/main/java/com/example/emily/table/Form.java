package com.example.emily.table;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Form extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ImageView photoView;
    private ActionBar actionBar;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);
        photoView = findViewById(R.id.form_image);
        submitButton = findViewById(R.id.form_submit);
        actionBar = getSupportActionBar();
        Intent restaurantInfo = getIntent();
        String restaurantName = restaurantInfo.getExtras().getString("name");
        String photoURL = restaurantInfo.getExtras().getString("photo");
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
                Table table = new Table();
                String key = myRef.push().getKey();
                myRef.child("Tables").child(key).setValue(table);
                onBackPressed();
            }
        });

    }
}
