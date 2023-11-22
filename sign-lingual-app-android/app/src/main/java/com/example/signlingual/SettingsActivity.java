package com.example.signlingual;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupUI();
        // Requires update for the comment to work in androidx. It replaces everything until marker.
        /*
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.idFrameLayout, new SettingsFragments())
            .commit();
         */
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

    @Override
    protected void setupUI() {
        super.setupUI();
    }
}
