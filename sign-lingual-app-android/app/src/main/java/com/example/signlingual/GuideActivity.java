package com.example.signlingual;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class GuideActivity extends BaseActivity {
    private ImageView LetterA, LetterB, LetterC, LetterD, LetterE, LetterF, LetterG, LetterH, LetterI, LetterJ;
    private ImageView LetterK, LetterL, LetterM, LetterN, LetterO, LetterP, LetterQ, LetterR, LetterS, LetterT;
    private ImageView LetterU, LetterV, LetterW, LetterX, LetterY, LetterZ, LetterSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        setupUI();
    }


    protected void setupUI() {
        super.setupUI();
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
        LetterSpace = findViewById(R.id.LetterSpace);
        LetterA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_a");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_b");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_c");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_d");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_e");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_f");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_g");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_h");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_i");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_j");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_k");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_l");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_m");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_n");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_o");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_p");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_q");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_r");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_s");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_t");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_u");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_v");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_w");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_x");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_y");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_z");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
        LetterSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN IMAGE DIALOG
                GuideDialog dialog = GuideDialog.newInstance("sign_language_space");
                dialog.show(getSupportFragmentManager(), "Show Image");
            }
        });
    }

}