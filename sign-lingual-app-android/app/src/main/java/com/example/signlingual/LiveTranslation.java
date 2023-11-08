package com.example.signlingual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.erkutaras.showcaseview.ShowcaseManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LiveTranslation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String message = "";
    TextView conversation;
    Button launch, stop;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private boolean readable = false;
    private boolean startTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_translation);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("sentence");
        DatabaseReference readyReference = FirebaseDatabase.getInstance().getReference().child("ready");
        conversation = findViewById(R.id.text_translated);
        launch = findViewById(R.id.start_translation);
        stop = findViewById(R.id.stop_translation);
        readyReference.setValue(false);

        setupUI();

        readyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readable = snapshot.getValue().equals(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if there is any data
                if (snapshot.exists() && readable) {
                    // Get the key from the parent of the snapshot
                    String key = snapshot.getKey();

                    // get the value
                    message += snapshot.getValue().toString();
                    conversation.setText(message);


                    databaseReference.setValue(" ");

                } else {
                    Log.d("MainActivity", "Data does not exist at this location");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MainActivity", "Failed to read value.", error.toException());
            }
        });






        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch.setClickable(false);
                launch.setVisibility(View.INVISIBLE);
                stop.setClickable(true);
                stop.setVisibility(View.VISIBLE);
                readyReference.setValue(true);


            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch.setClickable(true);
                launch.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);
                stop.setClickable(false);
                readyReference.setValue(false);


            }
        });

        SharedPreferences shared = getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
        startTutorial = shared.getBoolean("startTutorial", true);
    }


    private void setupUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(Color.BLACK);
        toolbar.setTitle("SignLingual");
        toolbar.setTitleTextColor(Color.WHITE);

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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Start Tutorial", "Start Tutorial? " + startTutorial);
        // Required as tutorial does not open as soon as the app starts.
        if (startTutorial) {
            Handler handler = new Handler();
            handler.postDelayed(this::buildTutorialLiveTranslation, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowcaseManager.REQUEST_CODE_SHOWCASE && resultCode == Activity.RESULT_OK && startTutorial) {
            Log.i("Activity Result", "Tutorial Dismissed #2");
            startTutorial = false;
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
    }

    private void buildTutorialLiveTranslation() {
        ShowcaseManager.Builder builder = new ShowcaseManager.Builder();
        builder.context(LiveTranslation.this)
                .key("Translation")
                .developerMode(true)
                .view(findViewById(R.id.toolbar))
                .descriptionTitle("Translation")
                .descriptionText("Here you can see the application's interpretation of Sign Language.")
                .buttonText("Done")
                .roundedRectangle()
                .add();

        builder.build()
                .show();
    }
}