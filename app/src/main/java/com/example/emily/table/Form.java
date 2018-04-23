package com.example.emily.table;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Form extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ImageView photoView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);
        photoView = findViewById(R.id.form_image);
        actionBar = getSupportActionBar();
        Intent restaurantInfo = getIntent();
        String restaurantName = restaurantInfo.getExtras().getString("name");
        String photoURL = restaurantInfo.getExtras().getString("photo");
        actionBar.setTitle(restaurantName);
        Log.d("Form.java", photoURL);
        Glide
                .with(getApplicationContext())
                .load(photoURL)
                .dontTransform()
                .into(photoView);

    }

    public void submitOnClick(View v){
        //create new Table object and insert into database
        //Try to add in a Table to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Table table = new Table("test2");
        myRef.child("Tables").setValue(table);
    }

}
