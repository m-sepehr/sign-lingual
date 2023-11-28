package com.example.signlingual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    private Bundle user_ID = new Bundle();
    protected static String userID; // static variable to store userId
    SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the Bundle from the Intent
        handleIntent(getIntent());
//        setupUI();
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                 userID = extras.getString("userID");
                if (userID != null) {
                    user_ID.putString("userID", userID);
                }
            }
        }
    }

    protected void setupUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        getSupportActionBar().setTitle("SignLingual");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
//                Toast.makeText(MainActivity.this, "inside", Toast.LENGTH_SHORT).show();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
//                Toast.makeText(MainActivity.this, "else", Toast.LENGTH_SHORT).show();
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
        } else if (itemID == R.id.page1) {
            params.putString("message", "User navigated to Page 1");
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivity(intent);
        } else if (itemID == R.id.settings) {
            params.putString("message", "User navigated to Settings");
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }else if (itemID == R.id.liveTranslation) {
            params.putString("message", "User navigated to Live Translation");
            Intent intent = new Intent(getApplicationContext(), LiveTranslation.class);
            startActivity(intent);
        } else if (itemID == R.id.standaloneMode) {
            params.putString("message", "User navigated to Standalone");
            Intent intent = new Intent(getApplicationContext(), StandaloneActivity.class);
            startActivity(intent);
        }else if (itemID == R.id.LogOut) {//adding the logout and logic behind it
            params.putString("message", "User signed out");
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            preferences = getSharedPreferences("Credentials", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("userId");
            editor.apply();
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
