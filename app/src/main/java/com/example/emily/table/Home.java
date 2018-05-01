package com.example.emily.table;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    private static boolean isDebug = true;
    private DatabaseReference myRef;
    private BottomNavigationView navigation;
    private int prevLayoutId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.start:
                    showFragment(startFragment, restaurantTag);
                    prevFragment = startFragment;
                    prevLayoutId = R.id.start;
                    return true;
                case R.id.find:
                    showFragment(findFragment, tableTag);
                    prevFragment = findFragment;
                    prevLayoutId = R.id.find;
                    return true;
                case R.id.profile:
                    showFragment(profileFragment, profileTag);
                    prevFragment = profileFragment;
                    prevLayoutId = R.id.profile;
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
            prevLayoutId = R.id.start;


            //Set Refreshing
            final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    navigation.setSelectedItemId(prevLayoutId);
                    swipeLayout.setRefreshing(false);
                }
            });
        }
        else{
            Log.d("HELP", "BUNDLE == NULL");
            finish();
        }
    }

    public boolean isDebug(){
        return isDebug;
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
            Intent intent = new Intent(this,Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
