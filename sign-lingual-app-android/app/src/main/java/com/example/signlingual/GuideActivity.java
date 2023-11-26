package com.example.signlingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class GuideActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView LetterA, LetterB, LetterC, LetterD, LetterE, LetterF, LetterG, LetterH, LetterI, LetterJ;
    private ImageView LetterK, LetterL, LetterM, LetterN, LetterO, LetterP, LetterQ, LetterR, LetterS, LetterT;
    private ImageView LetterU, LetterV, LetterW, LetterX, LetterY, LetterZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        setupUI();
    }
    private void setupUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Language Alphabet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LetterA = findViewById(R.id.LetterA);
        LetterB = findViewById(R.id.LetterB);
        LetterC = findViewById(R.id.LetterC);
        LetterD = findViewById(R.id.LetterD);
        LetterE = findViewById(R.id.LetterE);
        LetterF = findViewById(R.id.LetterF);
        LetterG = findViewById(R.id.LetterG);
        LetterH = findViewById(R.id.LetterH);
        LetterI = findViewById(R.id.LetterI);
        LetterJ = findViewById(R.id.LetterJ);

        LetterK = findViewById(R.id.LetterK);
        LetterL = findViewById(R.id.LetterL);
        LetterM = findViewById(R.id.LetterM);
        LetterN = findViewById(R.id.LetterN);
        LetterO = findViewById(R.id.LetterO);
        LetterP = findViewById(R.id.LetterP);
        LetterQ = findViewById(R.id.LetterQ);
        LetterR = findViewById(R.id.LetterR);
        LetterS = findViewById(R.id.LetterS);
        LetterT = findViewById(R.id.LetterT);

        LetterU = findViewById(R.id.LetterU);
        LetterV = findViewById(R.id.LetterV);
        LetterW = findViewById(R.id.LetterW);
        LetterX = findViewById(R.id.LetterX);
        LetterY = findViewById(R.id.LetterY);
        LetterZ = findViewById(R.id.LetterZ);
        LetterA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_a", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_b", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_c", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_d", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_e", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_f", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_g", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_h", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_i", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_j", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_k", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_l", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_m", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_n", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_o", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_p", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_q", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_r", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_s", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_t", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_u", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_v", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_w", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_x", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_y", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                int imageResourceId = getResources().getIdentifier("sign_language_z", "drawable", getPackageName());
                GuideDialog dialog = GuideDialog.newInstance(imageResourceId);
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
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
        } else if (itemID == R.id.standaloneMode) {
            params.putString("message", "User navigated to Standalone");
            Intent intent = new Intent(getApplicationContext(), StandaloneActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}