package com.example.signlingual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class networkActivity extends BaseActivity {

    EditText wifiName;
    EditText wifiPassword;
    Button generateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        super.setupUI();

        preferences = getSharedPreferences("Credentials", MODE_PRIVATE);

        wifiName = findViewById(R.id.wifiNameInput);
        wifiPassword = findViewById(R.id.passwordInput);
        generateButton = findViewById(R.id.generateButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(invalidTextBox()) {

                } else {
                    String ssid = wifiName.getText().toString();
                    String password = wifiPassword.getText().toString();
                    String encryptionType = "WPA";
                    String userID = preferences.getString("userID", "default_value");
                    String userToken = preferences.getString("token", "noToken");
                    Bitmap qrCodeBitmap = generateWifiQRCode(ssid, password, encryptionType, userID, userToken);
                    if (qrCodeBitmap != null) {
                        shareQRCode(qrCodeBitmap);
                    }
                }
            }
        });

    }
    public Bitmap generateWifiQRCode(String ssid, String password, String encryptionType, String userID, String userToken) {
        String wifiData = "WIFI:S:" + ssid + "|" + userID + "|" + userToken  + ";T:" + encryptionType + ";P:" + password + ";;";

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(wifiData, BarcodeFormat.QR_CODE, 512, 512);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void shareQRCode(Bitmap qrCodeBitmap) {
        // OPEN IMAGE DIALOG
        NetworkDialog dialog = NetworkDialog.newInstance(qrCodeBitmap);
        dialog.show(getSupportFragmentManager(), "Show Image");
    }

    public boolean invalidTextBox() {

        String ssid = wifiName.getText().toString();
        String password = wifiPassword.getText().toString();

        if(ssid.length() == 0) {
            Toast.makeText(networkActivity.this, "Wi-Fi name cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if(password.length() ==0) {
            Toast.makeText(networkActivity.this, "Wi-Fi password cannot be empty",
                    Toast.LENGTH_SHORT).show();
            Toast.makeText(networkActivity.this, "Wi-Fi with no password is not secure",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}