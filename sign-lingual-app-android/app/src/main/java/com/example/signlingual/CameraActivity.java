package com.example.signlingual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.MenuItem;
import android.view.Surface;
import android.Manifest;

import com.erkutaras.showcaseview.ShowcaseManager;
import com.google.android.material.navigation.NavigationView;

public class CameraActivity extends AppCompatActivity implements ImageReader.OnImageAvailableListener, NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private boolean startTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        askCameraPermission();

        setupUI();

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
            handler.postDelayed(this::buildTutorialCamera, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowcaseManager.REQUEST_CODE_SHOWCASE && resultCode == Activity.RESULT_OK && startTutorial) {
            Log.i("Activity Result", "Tutorial Dismissed #2");
            startTutorial = false;
            Intent intent = new Intent(getApplicationContext(), LiveTranslation.class);
            startActivity(intent);
        }
    }

    public void askCameraPermission() {
        //TODO ask for camera permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA}, 121);
            }else{
                //TODO show live camera footage
                setFragment();
            }
        } else {
            //TODO show live camera footage
            setFragment();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO show live camera footage
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //TODO show live camera footage
            setFragment();
        } else {
            finish();
        }
    }

    //TODO fragment which show live footage from camera
    int previewHeight = 0,previewWidth = 0;
    int sensorOrientation;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void setFragment() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = manager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        CameraConnectionFragment fragment;
        CameraConnectionFragment camera2Fragment =
                CameraConnectionFragment.newInstance(
                        (size, rotation) -> {
                            previewHeight = size.getHeight();
                            previewWidth = size.getWidth();
                            Log.d("tryOrientation","rotation: "+rotation+"   orientation: "+getScreenOrientation()+"  "+previewWidth+"   "+previewHeight);
                            sensorOrientation = rotation - getScreenOrientation();
                        },
                        this,
                        R.layout.camera_fragment,
                        new Size(640, 480));

        camera2Fragment.setCamera(cameraId);
        fragment = camera2Fragment;
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        Image image = imageReader.acquireLatestImage();
        if (image != null) {
            image.close();
        }
    }

    private void setupUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SignLingual");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
//                Toast.makeText(CameraActivity.this, "inside", Toast.LENGTH_SHORT).show();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
//                Toast.makeText(CameraActivity.this, "else", Toast.LENGTH_SHORT).show();
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
        } else if (itemID == R.id.camera) {
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

    private void buildTutorialCamera() {
        ShowcaseManager.Builder builder = new ShowcaseManager.Builder();
        builder.context(CameraActivity.this)
                .key("Camera")
                .developerMode(true)
                .view(findViewById(R.id.toolbar))
                .descriptionTitle("Camera View")
                .descriptionText("Here you can see what your camera is aiming.")
                .buttonText("Done")
                .roundedRectangle()
                .add()
                .build()
                .show();
    }
}