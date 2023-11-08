package com.example.signlingual;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.erkutaras.showcaseview.ShowcaseManager;

public class SettingsActivity extends AppCompatActivity {

    private boolean startTutorial;

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

        SharedPreferences shared = getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
        startTutorial = shared.getBoolean("startTutorial", true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Start Tutorial", "Start Tutorial? "+startTutorial);
        // Required as tutorial does not open as soon as the app starts.
        if (startTutorial) {
            Handler handler = new Handler();
            handler.postDelayed(this::buildTutorialSettings, 300);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowcaseManager.REQUEST_CODE_SHOWCASE && resultCode == Activity.RESULT_OK && startTutorial) {
            Log.i("Activity Result", "Tutorial Dismissed #4");
            startTutorial = false;
            SharedPreferences shared = getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putBoolean("startTutorial", false);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void buildTutorialSettings() {
        ShowcaseManager.Builder builder = new ShowcaseManager.Builder();
        builder.context(SettingsActivity.this)
                .key("Settings")
                .developerMode(true)
                .view(findViewById(R.id.toolbar_settings))
                .descriptionTitle("Settings Menu")
                .descriptionText("Here you can customize your preferences.")
                .buttonText("Done")
                .roundedRectangle()
                .add()
                .build()
                .show();
    }
}
