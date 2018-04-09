package com.example.emily.table;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);
    }

    public void submitOnClick(View v){
        //create new Table object and insert into database
        //Try to add in a Table to the database
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//        Table table = new Table("test2");
//        myRef.child("Tables").setValue(table);
    }

}
