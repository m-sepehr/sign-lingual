package com.example.signlingual;

import android.os.Bundle;
import android.preference.Preference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

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

        getSupportActionBar().setTitle("Settings");

        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.idFrameLayout, new SettingsFragments()).commit();
        }
        // Marker
    }
}
