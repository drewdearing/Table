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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.facebook.login.LoginManager;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends AppCompatActivity {

    private final String restaurantTag = "RestaurantFragment";
    private final String tableTag = "TableFragment";
    private final String profileTag = "Profile";
    private  Fragment startFragment;
    private  Fragment findFragment;
    private  Fragment profileFragment;
    private  Fragment prevFragment;
    private String userId;
    private String profileId;
    private ActionBar actionBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private BottomNavigationView navigation;

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
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("profileId", profileId);
        fragment.setArguments(bundle);
        ft.attach(fragment);
        // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        profileId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        actionBar = getSupportActionBar();

        //Init Bottom Navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the user's profile
        Intent activityThatCalled = getIntent();
        Bundle callingBundle = activityThatCalled.getExtras();
        if (callingBundle != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            userId = callingBundle.getString("id");
            profileId = userId;
            String profileId = callingBundle.getString("profileId");
            Bundle userBundle = new Bundle();
            userBundle.putString("userId", userId);
            //Create Fragments, copied from DemoListViewFrag
            startFragment = new RestaurantFragment();
            startFragment.setArguments(userBundle);
            findFragment = new TableFragment();
            profileFragment = new ProfileFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.theFrame, startFragment, restaurantTag);
            ft.add(R.id.theFrame, findFragment, tableTag);
            ft.detach(findFragment);
            ft.add(R.id.theFrame, profileFragment, profileTag);
            ft.detach(profileFragment);
            ft.commit();
            prevFragment = startFragment;
            if(profileId != null)
                openProfile(profileId);


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
        else{
            Log.d("HELP", "BUNDLE == NULL");
            finish();
        }
    }

    public void openProfile(String profile_id){
        profileId = profile_id;
        navigation.setSelectedItemId(R.id.profile);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            // do something here
            LoginManager.getInstance().logOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
