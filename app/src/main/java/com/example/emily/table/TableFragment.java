package com.example.emily.table;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private Context context;
    private SwipeRefreshLayout swipeLayout;



    //Code from FCListRecyclerView
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.table_frag, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tableRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userId = getArguments().getString("userId");
        isDebug = ((Home) getActivity()).isDebug();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        context = container.getContext();

        //Set Refreshing
        swipeLayout = v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentLocation();
            }
        });

        //Get current user location
        getCurrentLocation();
        return v;
    }


    private boolean isNearby(double lat1, double lng1) {
        Location otherLocation = new Location("other location");
        otherLocation.setLatitude(lat1);
        otherLocation.setLongitude(lng1);

        if (userLocation != null) {
            double distance = userLocation.distanceTo(otherLocation);
            return distance < 5000;
        } else {
            return false;
        }

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
                            else{
                                if(isDebug){
                                    userLocation = new Location("currentLocation");
                                    userLocation.setLatitude(30.286253);
                                    userLocation.setLongitude(-97.7386227);
                                }
                            }
                            setData();
                        }
                    });
        }
    }

    private void setData(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Table> tables = new ArrayList<>();
                DataSnapshot tableSnapShot = dataSnapshot.child("Tables");
                for(DataSnapshot tableData : tableSnapShot.getChildren()) {
                    Table table = tableData.getValue(Table.class);
                    //Only show tables that are nearby
                    if (isNearby(table.getRestaurant().getLat(), table.getRestaurant().getLon())) {
                        tables.add(table);
                    }
                }

                adapter = new TableAdapter(context, tables, userId);
                recyclerView.setAdapter(adapter);
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(userLocation != null){
            outState.putDoubleArray("userLocation", new double[]{userLocation.getLatitude(), userLocation.getLongitude()});
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            double[] loc = savedInstanceState.getDoubleArray("userLocation");
            userLocation = new Location("currentLocation");
            userLocation.setLatitude(loc[0]);
            userLocation.setLongitude(loc[1]);
        }
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
