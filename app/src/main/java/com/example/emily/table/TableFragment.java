package com.example.emily.table;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.emily.table.RestaurantFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

/**
 * Created by emily on 3/29/18.
 */

public class TableFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TableAdapter adapter;
    private boolean isDebug;
    private ActionBar actionBar;
    private String userId;
    private Location userLocation;



    //Code from FCListRecyclerView
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.table_frag, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tableRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userId = getArguments().getString("userId");
        isDebug = ((Home) getActivity()).isDebug();
        //get current user location
        getCurrentLocation();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "table onDataChange");
                ArrayList<Table> tables = new ArrayList<>();
                DataSnapshot tableSnapShot = dataSnapshot.child("Tables");
                for(DataSnapshot tableData : tableSnapShot.getChildren()) {
                    Table table = tableData.getValue(Table.class);
                    //Only show tables that are nearby
                    if (isNearby(table.getRestaurant().getLat(), table.getRestaurant().getLon())) {
                        tables.add(table);
                    }
                }

                adapter = new TableAdapter(container.getContext(), tables, userId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return v;
    }


    private boolean isNearby(double lat1, double lng1) {
        Location otherLocation = new Location("other location");
        otherLocation.setLatitude(lat1);
        otherLocation.setLongitude(lng1);

        double distance = userLocation.distanceTo(otherLocation);
        return distance < 5000;

    }

    private void getCurrentLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userLocation = location;
                            }
                        }
                    });
        }
        if(userLocation == null && isDebug){
            userLocation = new Location("currentLocation");
            userLocation.setLatitude(30.286253);
            userLocation.setLongitude(-97.7386227);
        }
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Find a Table");
    }

    @Override
    public void onResume() {
        Log.d("Login", "onResume");
        super.onResume();
        getCurrentLocation();
    }
}
