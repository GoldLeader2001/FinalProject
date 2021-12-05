package com.example.jsfinalcryptoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class UserProf extends AppCompatActivity {
    //declare Variables
    FirebaseAuth mAuth;
    MaterialButton logout_Button;
    String name, email;
    Uri photoUrl;

    TextView welcomeTV, emailTV, countTV;
    ImageView userPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //initializing firebase user
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();


        // declare bottom nav view
        BottomNavigationView bottomNavigationView =  (BottomNavigationView) findViewById(R.id.Bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Profile);
        //set Variables
        logout_Button = findViewById(R.id.LogoutButton);
        welcomeTV = findViewById(R.id.welcome_user);
        emailTV = findViewById(R.id.email_user);
        countTV =  findViewById(R.id.Fav_count);
        userPic =  findViewById(R.id.user_image);

        //add listener to the Bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Favorites:
                        startActivity(new Intent(getApplicationContext(), Favorites.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Profile:
                        return true;
                }
                return true;
            }
        });

        //adding logout button action listener
        logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set null Firebase user
                mAuth.signOut();
                //redirecting to login activity
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(0 , 0);
                Toast.makeText(getApplicationContext(),"User Successfully Logout!!!", Toast.LENGTH_LONG).show();
            }
        });

        //when everything ready updateUI
        updateUI(user);

    } //end of onCreate

    private void get_user_info(FirebaseUser user){

        for (UserInfo profile: user.getProviderData()){
            //id of the provider
            String providerID = profile.getProviderId();

            //getting user name, email and photo
            name = profile.getDisplayName();
            email = profile.getEmail();
            photoUrl = profile.getPhotoUrl();
            Log.e("TAG","in get user info");
            Log.e("TAG", name);
            Log.e("TAG", email);
            Log.e("TAG", ""+ photoUrl);
            place_info();
        }
    }// end of get user info

    private void place_info(){
        welcomeTV.setText("Welcome " + name +"!" );
        emailTV.setText(email);
        //for now is going to be 0 --> not count being made yet
        countTV.setText("0");
        if(photoUrl != null){
            Log.e("TAG", "Photo is here");
        } else{
            //do nothing photo is null
            Log.e("TAG", "photo is null");
            userPic.setImageResource(R.drawable.ic_profile);
        }

    }

    private void updateUI(FirebaseUser user) {
        //redirect to the corresponding activity
        if (user != null){
            Log.e("TAG", "user is in");
            //send user to the corresponding page
            get_user_info(user);
        }else{
            Log.e("TAG", "User not logged in");
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    } //end of updateUI
}