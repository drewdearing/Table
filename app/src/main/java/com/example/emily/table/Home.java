package com.example.emily.table;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends AppCompatActivity {

    private TextView mTextMessage;
    private final String restaurantTag = "RestaurantFragment";
    private final String tableTag = "TableFragment";
    private final String profileTag = "Profile";
    private  Fragment startFragment;
    private  Fragment findFragment;
    private  Fragment profileFragment;
    private  Fragment prevFragment;
    private User user = null;
    private ActionBar actionBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.start:
                    showFragment(startFragment, restaurantTag);
                    prevFragment = startFragment;
                    return true;
                case R.id.find:
                    showFragment(findFragment, tableTag);
                    prevFragment = findFragment;
                    return true;
                case R.id.profile:
                    showFragment(profileFragment, profileTag);
                    prevFragment = profileFragment;
                    return true;
            }
            return false;
        }
    };

    // From DemoListViewFrag
    // Java 8 functional interface only works on API 24+.  Call with ClassName::new for constructor
    private void showFragment (Fragment fragment, String fragmentTag) {
        FragmentTransaction ft;
        // Start Fragment transactions
        ft = getSupportFragmentManager().beginTransaction();
        ft.detach(prevFragment);
        ft.attach(fragment);
        // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //mTextMessage = (TextView) findViewById(R.id.message);

        actionBar = getSupportActionBar();

        //Init Bottom Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the user's profile
        Intent activityThatCalled = getIntent();
        Bundle callingBundle = activityThatCalled.getExtras();
        if (callingBundle != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            final String id = callingBundle.getString("id");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot Userdata = dataSnapshot.child("Users");
                    if (Userdata.child(id).exists()) {
                        user = Userdata.child(id).getValue(User.class);
                    } else {
                        Log.d("HELP", "CHILD DOESN'T EXIST");
                        finish();
                    }
                    Log.d("HELP", user.getFirstName());
                    //Create Fragments, copied from DemoListViewFrag
                    startFragment = new RestaurantFragment();
                    findFragment = new TableFragment();
                    profileFragment = new ProfileFragment();
                    actionBar.setTitle("Start a Table");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.theFrame, startFragment, restaurantTag);
                    ft.add(R.id.theFrame, findFragment, tableTag);
                    ft.detach(findFragment);
                    ft.add(R.id.theFrame, profileFragment, profileTag);
                    ft.detach(profileFragment);
                    ft.commit();
                    prevFragment = startFragment;

                    //Set Refreshing
                    final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipe_container);
                    swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //Well do this later
                            swipeLayout.setRefreshing(false);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Log.d("HELP", "BUNDLE == NULL");
            finish();
        }
    }

}
