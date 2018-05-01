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

//haverSign formula to calculate distance between long and lat
//        double currLat = userLocation.getLatitude();
//        double currLon = userLocation.getLongitude();
//        double earthRadius = 6371000; //meters
//        double dLat = Math.toRadians(currLat-lat1);
//        double dLng = Math.toRadians(currLon-lng1);
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(currLat)) *
//                            Math.sin(dLng/2) * Math.sin(dLng/2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        float dist = (float) (earthRadius * c);
//
//        return dist < 1000;
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
