package com.example.emily.table;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class TableDetails extends AppCompatActivity {

    private Table table;
    private String currentUserId;
    private Button button;
    private ActionBar actionBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<User> guests;
    private ArrayList<String> guestIds;
    private GuestAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isOwner = false;
    private double lat;
    private double lon;


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
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                            //Add current user to the guest list
                            myRef.child("Tables").child(table.getTableId()).child("Guests").child(currentUserId).setValue(currentUserId);
                            Toast.makeText(getApplicationContext(), "Joined table", Toast.LENGTH_LONG).show();
                            button.setEnabled(false);
                            initViews();
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

        //Set Refreshing
        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    //Sets up all visible views as well as create the recycler view
    private void initViews() {
        TextView desc = findViewById(R.id.details_desc);
        desc.setText(table.getDescription());

        if(isOwner){
            button.setText("End Table");
        }

        TextView address = findViewById(R.id.address);
        address.setText(table.getRestaurant().getName()+"\n"+table.getRestaurant().getAddress());
        Log.d("address in table detail", table.getRestaurant().getAddress());

        ImageView img = findViewById(R.id.details_image);
        String photoURL = table.getRestaurant().photo;
        Glide
                .with(getApplicationContext())
                .load(photoURL)
                .dontTransform()
                .into(img);

        recyclerView = (RecyclerView) findViewById(R.id.details_guest_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        guests = new ArrayList<>();
        guestIds = new ArrayList<>();
        final Context context = this;
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
                adapter = new GuestAdapter(context, guests, table.getUserId(), currentUserId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) { }
         });

        Log.w("guest size", "" + guestIds.size());

    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.map) {
            //open Google Maps
            lat = table.getRestaurant().getLat();
            lon = table.getRestaurant().getLon();
            String uri = "http://maps.google.co.in/maps?q=" + table.getRestaurant().getAddress();
            Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(maps);
            return true;
        }
        else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}