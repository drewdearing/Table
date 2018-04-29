package com.example.emily.table;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by emily on 3/29/18.
 */

public class ProfileFragment extends Fragment {
    private ActionBar actionBar;
    private String userId;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView username;
    private TextView bio;
    private TextView tableHeader;
    private ImageView profilePic;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_frag, container, false);
        userId = getArguments().getString("userId");
        username = v.findViewById(R.id.usernameText);
        bio = v.findViewById(R.id.bioText);
        tableHeader = v.findViewById(R.id.tableHeader);
        profilePic = v.findViewById(R.id.profilePic);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot Userdata = dataSnapshot.child("Users");
                if (Userdata.child(userId).exists()) {
                    user = Userdata.child(userId).getValue(User.class);
                    setUserInfo();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return v;
    }

    private void setUserInfo(){
        username.setText(user.getName());
        bio.setText(user.getBio());
        tableHeader.setText(user.getFirstName() + "'s Tables");
        Glide
                .with(getContext())
                .load(user.getPic())
                .dontTransform()
                .into(profilePic);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(user.getFirstName()+"'s Profile");
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        
    }
}
