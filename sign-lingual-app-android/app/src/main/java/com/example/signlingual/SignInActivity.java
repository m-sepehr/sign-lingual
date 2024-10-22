package com.example.signlingual;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import org.checkerframework.checker.units.qual.A;

public class SignInActivity extends BaseActivity {
    //shared preferences
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    //get the instance of the database
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private EditText emailInput;
    private EditText passwordInput;
    private Button signInButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //get the shared preferences
        sharedPreferences = getSharedPreferences("Credentials", MODE_PRIVATE);


        //declare variables
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInButton = findViewById(R.id.signInButton);
        createAccountButton = findViewById(R.id.createAccountButton);
//        if(!Python.isStarted()){
//            Python.start(new AndroidPlatform(this));
//        }
    }

    private void signIn(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Show an error message or toast indicating that email or password is empty
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                // Get the authentication token
                                user.getIdToken(true)
                                        .addOnCompleteListener(tokenTask -> {
                                            if (tokenTask.isSuccessful()) {
                                                //getting the token
                                                String token = tokenTask.getResult().getToken();
                                                updateUI(user);
                                                preferences = getSharedPreferences("Credentials", MODE_PRIVATE);
                                                String userID = user.getUid();
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("userID", userID);
                                                editor.putBoolean("isLoggedIn", true);
                                                editor.putString("token", token);
                                                editor.apply();
                                                Log.i("Token", token);

                                                // Initialize Python
//                                                Python python = Python.getInstance();
//                                                PyObject pyObject = python.getModule("network");
//
//                                                // Call the Python function with arguments
//                                                pyObject.callAttr("main", userID, token);

                                            } else {
                                                // Handle failure to obtain token
                                                Log.e(TAG, "Failed to obtain authentication token.", tokenTask.getException());
                                                updateUI(null);
                                            }
                                        });} else {
                                Toast.makeText(SignInActivity.this, "Please verify your email.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if(currentUser != null){
            reload();
        }
        signInButton.setOnClickListener(v -> {
            signIn(emailInput.getText().toString(), passwordInput.getText().toString());
        });
        createAccountButton.setOnClickListener(v -> {
            createAccount(emailInput.getText().toString(), passwordInput.getText().toString());
        });
    }

    private void createAccount(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Show an error message or toast indicating that email or password is empty
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(password.length() < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference userRef = mDatabase.getReference("users").child(user.getUid());
                            userRef.child("ready").setValue(false);
                            userRef.child("sentence").setValue("");
                            userRef.child("ip_address").setValue("");
                            sendEmailVerification();
                            Toast.makeText(SignInActivity.this, "Email verification sent, Please verify", Toast.LENGTH_SHORT).show();

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }
    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {
        try {
            if (user != null) {
                String userID = user.getUid();
                Intent newIntent = new Intent(this, MainActivity.class);
                newIntent.putExtra("userID", userID); // Pass the userID as an extra
                startActivity(newIntent);
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in updateUI", e);
        }
    }

}