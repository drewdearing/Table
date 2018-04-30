package com.example.emily.table;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import java.util.ArrayList;

/**
 * Created by emily on 3/29/18.
 */

public class ProfileFragment extends Fragment {
    private ActionBar actionBar;
    private String userId;
    private String profileId;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView username;
    private TextView bioText;
    private EditText bioEdit;
    private TextView tableHeader;
    private ImageView profilePic;
    private User user;
    private RecyclerView recyclerView;
    private TableAdapter adapter;
    private AppCompatButton button;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_frag, container, false);
        userId = getArguments().getString("userId");
        profileId = getArguments().getString("profileId");
        if(profileId == null){
            profileId = userId;
        }
        Log.d("PROFILE", profileId);
        username = v.findViewById(R.id.usernameText);
        bioEdit = v.findViewById(R.id.bioEdit);
        bioText = v.findViewById(R.id.bioText);
        tableHeader = v.findViewById(R.id.tableHeader);
        profilePic = v.findViewById(R.id.profilePic);
        recyclerView = (RecyclerView) v.findViewById(R.id.profile_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot Userdata = dataSnapshot.child("Users");
                if (Userdata.child(profileId).exists()) {
                    user = Userdata.child(profileId).getValue(User.class);
                    setUserInfo();
                }

                //read tables into table arraylist
                ArrayList<Table> tables = new ArrayList<>();
                DataSnapshot tableSnapShot = dataSnapshot.child("Tables");
                for(DataSnapshot tableData : tableSnapShot.getChildren()) {
                    Table table = tableData.getValue(Table.class);
                    if (table.getUserId().equals(profileId))
                        tables.add(table);
                }
                adapter = new TableAdapter(container.getContext(), tables, profileId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        button = v.findViewById(R.id.edit_button);

        if(!userId.equals(profileId)) {
            button.setEnabled(false);
            button.setVisibility(View.GONE);
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    bioEdit.setVisibility(View.VISIBLE);
                    bioText.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    bioEdit.requestFocus();
                    bioEdit.setSelection(bioEdit.getText().length());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(bioEdit, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
        bioEdit.setHorizontallyScrolling(false);
        bioEdit.setMaxLines(5);
        bioEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //something
                    Log.w("action done", "pls");
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(container.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    bioText.setText(bioEdit.getText());
                    bioEdit.setVisibility(View.GONE);
                    bioText.setVisibility(View.VISIBLE);
                    user = new User(user, bioEdit.getText().toString());
                    myRef.child("Users").child(user.getId()).setValue(user);
                    button.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        return v;
    }

    private void setUserInfo(){
        username.setText(user.getName());
        bioText.setText(user.getBio());
        bioEdit.setText(user.getBio());
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
