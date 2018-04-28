package com.example.emily.table;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;


public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    protected LoginButton loginButton;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("Login", "default callback success");
            Profile profile = Profile.getCurrentProfile();
            login(profile);
        }
        @Override
        public void onCancel() {

        }
        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Login", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.d("Login", "currentProfileChanged");
                login(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "loginbutton callback success");
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                login(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, callback);

    }

    private void login(Profile p) {
        if(p != null) {
            checkUser(p);
            Log.d("Login", "Login(p)");

        }
    }

    protected void checkUser(final Profile p) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if (data.child(p.getId()).exists()) {
                        //do nothing?
                    } else {
                        //create User and put it in database
                        User u = new User(p.getId(), p.getName(), p.getFirstName(), p.getProfilePictureUri(100,100));
                        Log.d("new user", u.getId() + u.getFirstName() + u.getName());
                        myRef.child("Users").child(u.getId()).setValue(u);

                    }
                }
                Intent goHome = new Intent(getApplicationContext(), Home.class);
                startActivity(goHome);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("Login", "onResume");
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        login(profile);
    }

    @Override
    protected void onPause() {
        Log.d("Login", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("Login", "onStop");
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
