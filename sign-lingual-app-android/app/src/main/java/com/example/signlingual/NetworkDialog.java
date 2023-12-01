package com.example.signlingual;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NetworkDialog extends AppCompatDialogFragment {
    TextView signLetter;
    ImageView qrCodeImage;
    public static NetworkDialog newInstance(Bitmap qrCode) {
        NetworkDialog fragment = new NetworkDialog();
        Bundle args = new Bundle();
        args.putParcelable("qrCode", qrCode);
        fragment.setArguments(args);
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_network_dialog, null);

        qrCodeImage = view.findViewById(R.id.qrCode);
        Bitmap qrCode = getArguments().getParcelable("qrCode");
        qrCodeImage.setImageBitmap(qrCode);

        builder.setView(view);
        return builder.create();
    }
}