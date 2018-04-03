package com.example.emily.table;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by emily on 3/29/18.
 */

public class RestaurantFragment extends Fragment {

    static String api_key = "AIzaSyDB1j3umaGyjXOMFf7ECjZIsjipT5eHPUM";
    static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            queryPlaces();
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.restaurant_frag, container, false);
    }

    private void queryPlaces(){
        String location = "";
        String request = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + location
                + "&radius=500&types=food&key="
                + api_key;
        Log.d("RestaurantFragment", "given permission");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            queryPlaces();
        }
    }
}
