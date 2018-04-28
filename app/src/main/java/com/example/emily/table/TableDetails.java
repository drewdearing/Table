package com.example.emily.table;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TableDetails extends AppCompatActivity {

    private Table table;
    private String currentUserId;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_details);
        table = (Table) getIntent().getSerializableExtra("Table");
        Log.w("table id", table.getTableId());
        currentUserId = getIntent().getExtras().getString("userId");
        Log.w("current user id", currentUserId);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(table.getName());
        initViews();
        button = findViewById(R.id.details_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();
                //Find user object in database
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot userdata = dataSnapshot.child("Users").child(currentUserId);

                        User user = userdata.getValue(User.class);
                        //Add current user to the guest list
                        table.getGuests().add(user);
                        //Update the database
//                        myRef.child("Tables").child(table.getTableId()).child("Guests").child(currentUserId).setValue(user);
                        myRef.child("Tables").child(table.getTableId()).child("Guests").child(currentUserId).setValue(currentUserId);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void initViews() {
        TextView desc = findViewById(R.id.details_desc);
        desc.setText(table.getDescription());

        ImageView img = findViewById(R.id.details_image);
        String photoURL = table.getRestaurant().photo;
        Glide
                .with(getApplicationContext())
                .load(photoURL)
                .dontTransform()
                .into(img);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.details_guest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GuestAdapter adapter = new GuestAdapter(this, table.getGuests());
        recyclerView.setAdapter(adapter);
    }

}