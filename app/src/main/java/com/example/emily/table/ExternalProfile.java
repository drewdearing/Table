package com.example.emily.table;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExternalProfile extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_frag);
        Intent intent = getIntent();
        userId = intent.getExtras().getString("id");
        profileId = intent.getExtras().getString("profileId");
        if(profileId == null){
            profileId = userId;
        }
        username = findViewById(R.id.usernameText);
        bioEdit = findViewById(R.id.bioEdit);
        bioText = findViewById(R.id.bioText);
        tableHeader = findViewById(R.id.tableHeader);
        profilePic = findViewById(R.id.profilePic);
        recyclerView = (RecyclerView) findViewById(R.id.profile_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        final Context context = this;
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
                    else{
                        DataSnapshot guestData = tableData.child("Guests");
                        for(DataSnapshot guest : guestData.getChildren()){
                            if(guest.getKey().equals(profileId)){
                                tables.add(table);
                            }
                        }
                    }
                }
                adapter = new TableAdapter(context, tables, userId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        button = findViewById(R.id.edit_button);

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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    Log.w("action done", "pls");
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
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
    }

    private void setUserInfo(){
        username.setText(user.getName());
        bioText.setText(user.getBio());
        bioEdit.setText(user.getBio());
        tableHeader.setText(user.getFirstName() + "'s Tables");
        Glide
                .with(this)
                .load(user.getPic())
                .dontTransform()
                .into(profilePic);
        actionBar = getSupportActionBar();
        actionBar.setTitle(user.getFirstName()+"'s Profile");
    }
}