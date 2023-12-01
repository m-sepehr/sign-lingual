package com.example.signlingual;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends BaseActivity {

    private Button buttonSettings, buttonGuide;
    private LinearLayout layoutStandalone, layoutConRPI;
    private boolean isNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPreferences();
        setupUI();

    }



    protected void setupUI() {
        super.setupUI();
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
                Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
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

    private void loadPreferences() {
        // Night Mode
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            isNightMode = false;
        } else {
            isNightMode = true;
        }
        // Set Language Configs. Check Android version for implementing backwards compatibility
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
            Log.d("loadPreferences", locales.toString());
            AppCompatDelegate.setApplicationLocales(locales);
            //TODO: load any other preference for devices sdk < Tiramisu
        }
    }
}