package com.example.emily.table;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emily on 3/29/18.
 */

public class RestaurantFragment extends Fragment {

    static String api_key = "AIzaSyDB1j3umaGyjXOMFf7ECjZIsjipT5eHPUM";
    static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private RequestQueue requestQueue;
    private ArrayList<Restaurant> restList;
    private Location userLocation = null;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private RecyclerView rv;
    private RestaurantAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.restaurant_frag, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        queryPlaces();

//        //Try to add in a Table to the database
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//        Table table = new Table("test2");
//        myRef.child("Tables").setValue(table);
        return v;
    }

    private void queryPlaces() {

        if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.d("RestaurantFragment", "kill me");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userLocation = location;
                                Log.d("RestaurantFragment", userLocation.toString());
                                String location_str = userLocation.getLatitude() + "," + userLocation.getLongitude();
                                String request = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                                        + location_str
                                        + "&radius=500&types=restaurant&key="
                                        + api_key;
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.GET, request, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("RestaurantFragment", response.toString());
                                                restList = jsonToRestaurants(response);
                                                Log.d("RestaurantFragment", restList.toString());
                                                listAdapter = new RestaurantAdapter(getContext(), restList);
                                                rv.setAdapter(listAdapter);
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // TODO: Handle error

                                            }
                                        });
                                requestQueue.add(jsonObjectRequest);
                            }
                            else{
                                Log.d("RestaurantFragment", "location was null");
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            queryPlaces();
        }
    }

    public static ArrayList<Restaurant> jsonToRestaurants(JSONObject jO) {
        ArrayList<Restaurant> list = new ArrayList<Restaurant>();
        try {
            JSONArray children = jO.getJSONArray("results");
            int i = 0;
            while(i < children.length() && list.size() < 50){
                try {
                    Restaurant r = new Restaurant();
                    JSONObject entry = children.getJSONObject(i);
                    String name = entry.getString("name");
                    r.name = name;
                    list.add(r);
                }
                catch (JSONException e){}
                i++;
            }
        }
        catch (JSONException e) {}
        return list;
    }
}
