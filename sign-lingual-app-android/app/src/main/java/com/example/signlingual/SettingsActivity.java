package com.example.signlingual;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Requires update for the comment to work in androidx. It replaces everything until marker.
        /*
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.idFrameLayout, new SettingsFragments())
            .commit();
         */
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (findViewById(R.id.frame_settings) != null) {
            if (savedInstanceState != null) {
                Log.d("SettingActivity", "Saved Instance State Not Found");
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.frame_settings, new SettingsFragments()).commit();
            Log.i("SettingsActivity", "Fragment found and initialized");
        }
        // Marker
    }
}
