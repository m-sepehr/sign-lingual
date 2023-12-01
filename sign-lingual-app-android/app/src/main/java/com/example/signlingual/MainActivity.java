package com.example.signlingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends BaseActivity {

    private Button buttonSettings, buttonGuide;
    private LinearLayout layoutStandalone, layoutConRPI;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userRef, ip_address;
    String ip_address_string="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        loadPreferences();
    }



    protected void setupUI() {
        super.setupUI();
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonGuide = findViewById(R.id.buttonGuide);
        layoutStandalone = findViewById(R.id.layoutStandalone);
        layoutConRPI = findViewById(R.id.layoutConRPI);
        //for firebase
        preferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        userID = preferences.getString("userID", null);
        userRef = mDatabase.getReference("users").child(userID);
        ip_address = userRef.child("ip_address");

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
                ip_address.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                String ip_address_string = snapshot.getValue(String.class);
                                Log.d("MainActivity", "ip address is: " + ip_address_string);
                                // Rest of your code using ip_address_string
                                if (ip_address_string.equals("")) {
                                    Log.d("MainActivity", "ip address is empty");
                                    Intent intent = new Intent(getApplicationContext(), networkActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), LiveTranslation.class);
                                    startActivity(intent);
                                }
                            } else {
                                Log.d("MainActivity", "ip address does not exist");
                            }
                        } else {
                            Log.e("MainActivity", "Error getting IP address", task.getException());
                        }
                    }
                });
            }
        });

    }



    private void loadPreferences() {
        // Set Language Configs. Check Android version for implementing backwards compatibility
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
            Log.d("loadPreferences", locales.toString());
            AppCompatDelegate.setApplicationLocales(locales);
            //TODO: load any other preference for devices sdk < Tiramisu
        }
    }
}