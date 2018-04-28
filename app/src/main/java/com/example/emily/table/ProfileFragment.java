package com.example.emily.table;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by emily on 3/29/18.
 */

public class ProfileFragment extends Fragment {
    private ActionBar actionBar;
    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Profile");
    }
}
