package com.example.emily.table;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TableDetails extends AppCompatActivity {

    private Table table;
    private String currentUserId;
    private Button button;
    private ActionBar actionBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<User> guests;
    private ArrayList<String> guestIds;
    private boolean isOwner = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_details);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        table = (Table) getIntent().getSerializableExtra("Table");
        currentUserId = getIntent().getExtras().getString("userId");
        actionBar = getSupportActionBar();
        actionBar.setTitle(table.getName());
        button = findViewById(R.id.details_button);
        isOwner = currentUserId.equals(table.getUserId());

        initViews();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isOwner) {
                    //Find user object in database
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot userdata = dataSnapshot.child("Users").child(currentUserId);

                            User user = userdata.getValue(User.class);
                            //Add current user to the guest list
                            myRef.child("Tables").child(table.getTableId()).child("Guests").child(currentUserId).setValue(currentUserId);
                            Toast.makeText(getApplicationContext(), "Joined table", Toast.LENGTH_LONG).show();
                            button.setEnabled(false);
                            button.setText("Joined");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                else{
                    myRef.child("Tables").child(table.getTableId()).removeValue();
                    Toast.makeText(getApplicationContext(), "Removing table.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    //Sets up all visible views as well as create the recycler view
    private void initViews() {
        TextView desc = findViewById(R.id.details_desc);
        desc.setMovementMethod(ScrollingMovementMethod.getInstance());
        desc.setText(table.getDescription());

        if(isOwner){
            button.setText("End Table");
        }

        ImageView img = findViewById(R.id.details_image);
        String photoURL = table.getRestaurant().photo;
        Glide
                .with(getApplicationContext())
                .load(photoURL)
                .dontTransform()
                .into(img);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.details_guest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        guests = new ArrayList<>();
        guestIds = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot guestSnapShot = dataSnapshot.child("Tables").child(table.getTableId()).child("Guests");
                for (DataSnapshot guestData : guestSnapShot.getChildren()) {
                    String userId = guestData.getKey();
                    //Hide button if the user is already on guest list
                    if (!isOwner && userId.equals(currentUserId)) {
                        button.setEnabled(false);
                        button.setText("Joined");
                    }
                    guestIds.add(userId);
                    User u = dataSnapshot.child("Users").child(userId).getValue(User.class);
                    guests.add(u);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) { }
         });

        GuestAdapter adapter = new GuestAdapter(this, guests, table.getUserId());
        recyclerView.setAdapter(adapter);

        Log.w("guest size", "" + guestIds.size());

    }

}