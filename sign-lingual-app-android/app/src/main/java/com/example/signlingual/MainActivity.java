package com.example.signlingual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;


import android.view.View;
import android.widget.Button;

import com.erkutaras.showcaseview.ShowcaseManager;

import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button buttonSettings, buttonGuide;
    private LinearLayout layoutStandalone, layoutConRPI;
    private boolean startTutorial;
    private static final String tutString = "startTutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        loadPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Required as tutorial does not open as soon as the app starts.
        if (startTutorial) {
            Handler handler = new Handler();
            handler.postDelayed(this::buildTutorialStart, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowcaseManager.REQUEST_CODE_SHOWCASE && resultCode == Activity.RESULT_OK && startTutorial) {
            // Required to prevent the first tutorial from starting on top of the camera tutorial
            startTutorial = false;
            Log.i("Activity Result", "Tutorial Dismissed #1");
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivity(intent);
        }
    }

    private void setupUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        setButtons();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        getSupportActionBar().setTitle("SignLingual");
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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadPreferences() {
        // Load shared preferences
        SharedPreferences shared = getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
        startTutorial = shared.getBoolean(tutString, true);
        Log.i("StartTutorial", "Start Tutorial? " + startTutorial);

        // Set Language Configs. Check Android version for implementing backwards compatibility
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
            Log.d("loadPreferences", locales.toString());
            AppCompatDelegate.setApplicationLocales(locales);
            //TODO: load any other preference for devices sdk < Tiramisu
        }
    }

    private void setButtons() {
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonGuide = findViewById(R.id.buttonGuide);
        layoutStandalone = findViewById(R.id.layoutStandalone);
        layoutConRPI = findViewById(R.id.layoutConRPI);

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        buttonGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LiveTranslation.class);
                startActivity(intent);
            }
        });

        layoutStandalone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StandaloneActivity.class);
                startActivity(intent);
            }
        });

        layoutConRPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LiveTranslation.class); //LiveTranslation
                startActivity(intent);
            }
        });
    }

    private void addPoint(ShowcaseManager.Builder builder, Context context, String key, View view, String shape, String title, String text) {
        builder.context(context)
                .key(key)
                .developerMode(true)
                .view(view)
                .descriptionTitle(title)
                .descriptionText(text)
                .buttonText("Done");
        switch (shape) {
            case "rectangle":
                builder.rectangle();
                break;
            case "circle":
                builder.circle();
                break;
            case "roundtangle":
                builder.roundedRectangle();
                break;
        }
        builder.add();
    }

    private void buildTutorialStart() {
        ShowcaseManager.Builder builder = new ShowcaseManager.Builder();
        addPoint(builder,
                MainActivity.this,
                "Connect",
                layoutConRPI,
                "roundtangle",
                "Connect",
                "With this button you can connect to a near Raspberry Pi.");
        addPoint(builder,
                MainActivity.this,
                "StandAlone",
                layoutStandalone,
                "roundtangle",
                "Continue",
                "Continue without connecting to external device");

        addPoint(builder,
                MainActivity.this,
                "Toolbar",
                findViewById(R.id.toolbar),
                "roundtangle",
                "Toolbar",
                "This is the main toolbar. Here you can Navigate the App.");
        builder.build().show();
    }
    /*
    // Unused for now as the Showcase does not work properly with the drawer.
    private void buildTutorialMenu() {
        ShowcaseManager.Builder builder = new ShowcaseManager.Builder();
        addPoint(builder,
                MainActivity.this,
                "Home",
                findViewById(R.id.navHome),
                "roundtangle",
                "Got to main page",
                "Here you can go back to the main page.");
        addPoint(builder,
                MainActivity.this,
                "Page 1",
                findViewById(R.id.navHome),
                "roundtangle",
                "Camera",
                "Here you can go to the camera view.");
        addPoint(builder,
                MainActivity.this,
                "Settings",
                findViewById(R.id.settings),
                "roundtangle",
                "Go to Settings",
                "You can change your preferences here");

        builder.build().show();
    }
 */

}