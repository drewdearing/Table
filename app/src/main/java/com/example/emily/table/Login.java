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


public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    protected LoginButton loginButton;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    static final int LOG_OUT_REQUEST = 1;  // The request code
    private FacebookCallback<LoginResult> callback;

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
            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUser(final Profile p) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot Userdata = dataSnapshot.child("Users");
                if (Userdata.child(p.getId()).exists()) {
                    User u = Userdata.child(p.getId()).getValue(User.class);
                    User newUser = new User(p.getId(),
                            p.getName(),
                            p.getFirstName(),
                            p.getProfilePictureUri(500, 500).toString(),
                            u.getBio());
                    myRef.child("Users").child(u.getId()).setValue(u);
                } else {
                    //create User and put it in database
                    User u = new User(p.getId(),
                            p.getName(),
                            p.getFirstName(),
                            p.getProfilePictureUri(500, 500).toString());
                    myRef.child("Users").child(u.getId()).setValue(u);
                }
                Intent goHome = new Intent(getApplicationContext(), Home.class);
                Bundle b = new Bundle();
                b.putString("id", p.getId());
                Log.d("HELP LOGIN", ";ddsdsnoidnds");
                goHome.putExtras(b);
                startActivityForResult(goHome, LOG_OUT_REQUEST);
                finish();
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
//        Log.w("onResume profile", profile.getName());
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
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOG_OUT_REQUEST){
            Log.w("TAG", "logged out");
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}