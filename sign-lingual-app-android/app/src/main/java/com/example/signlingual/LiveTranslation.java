package com.example.signlingual;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class LiveTranslation extends BaseActivity {
    String message="";
    String ip_address_string;
    TextView conversation;
    Button launch, stop;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference readyReference, ip_address;
    DatabaseReference userRef, databaseReference;
    SharedPreferences sharedPreferences;

    private boolean readable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_translation);
        sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);
        userID = sharedPreferences.getString("userID", null);
        if (userID != null) {
            userRef = mDatabase.getReference("users").child(userID);
            databaseReference = userRef.child("sentence");
            readyReference = userRef.child("ready");
            ip_address = userRef.child("ip_address");
            conversation = findViewById(R.id.text_translated);
            launch = findViewById(R.id.start_translation);
            stop = findViewById(R.id.stop_translation);
            readyReference.setValue(false);
        }else{
            Log.d("LiveTranslation", "userID is null");
            finish();
        }

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }



        setupUI();

        readyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().equals(true)){
                    readable = true;
                }else{
                    readable = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ip_address.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ip_address_string = snapshot.getValue().toString();
                    Log.d("LiveTranslation", "ip address is: " + ip_address_string);
//                    Toast.makeText(LiveTranslation.this, "ip address is: " + ip_address_string, Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("LiveTranslation", "ip address does not exist");
                }
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

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                user.getIdToken(true)
                        .addOnCompleteListener(tokenTask -> {
                            if (tokenTask.isSuccessful()) {
                                //getting the token
                                String token = tokenTask.getResult().getToken();
                                preferences = getSharedPreferences("Credentials", MODE_PRIVATE);
                                String userID = user.getUid();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID", userID);
                                editor.apply();
                                Log.i("Token", token);

                                // Initialize Python
                                Python python = Python.getInstance();
                                PyObject pyObject = python.getModule("network");

                                // Call the Python function with arguments
                                pyObject.callAttr("main", userID, token, ip_address_string);
                            } else {
                                // Handle failure to obtain token
                                Log.e(TAG, "Failed to obtain authentication token.", tokenTask.getException());
                            }
                        });
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
    }
    //when leaving app, set the ready in Firebase to false
    @Override
    protected void onPause() {
        super.onPause();
        // Set readyReference to false when the activity is paused
        if (readyReference != null) {
            readyReference.setValue(false);
            launch.setClickable(true);
            launch.setVisibility(View.VISIBLE);
            stop.setVisibility(View.INVISIBLE);
            stop.setClickable(false);

        }
    }


    protected void setupUI() {
        super.setupUI();
    }
}