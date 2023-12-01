package com.example.signlingual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    protected static String userID; // static variable to store userId
    SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void setupUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        Bundle params = new Bundle();  // Create a bundle to hold parameters

        if (itemID == R.id.navHome) {
            params.putString("message", "User navigated to Home");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else if (itemID == R.id.liveTranslation) {
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userRef, ip_address;
            userRef = mDatabase.getReference("users").child(userID);
            ip_address = userRef.child("ip_address");
            ip_address.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            String ip_address_string = snapshot.getValue(String.class);
                            Log.d("toolBarActivity", "ip address is: " + ip_address_string);
                            // Rest of your code using ip_address_string
                            if (ip_address_string.equals("")) {
                                Log.d("toolBarActivity", "ip address is empty");
                                Intent intent = new Intent(getApplicationContext(), networkActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), LiveTranslation.class);
                                startActivity(intent);
                            }
                        } else {
                            Log.d("toolBarActivity", "ip address does not exist");
                        }
                    } else {
                        Log.e("toolBarActivity", "Error getting IP address", task.getException());
                    }
                }
            });
        } else if (itemID == R.id.standaloneMode) {
            params.putString("message", "User navigated to Standalone");
            Intent intent = new Intent(getApplicationContext(), StandaloneActivity.class);
            startActivity(intent);
        } else if (itemID == R.id.settings) {
            params.putString("message", "User navigated to Settings");
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }else if (itemID == R.id.LogOut) {//adding the logout and logic behind it
            params.putString("message", "User signed out");
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            preferences = getSharedPreferences("Credentials", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("userId");
            editor.putBoolean("isLoggedIn", false);
            editor.remove("token");
            editor.apply();

            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
