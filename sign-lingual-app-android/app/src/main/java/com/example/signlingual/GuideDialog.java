package com.example.signlingual;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideDialog extends AppCompatDialogFragment {
    TextView signLetter;
    public static GuideDialog newInstance(String imageFileName) {
        GuideDialog fragment = new GuideDialog();
        Bundle args = new Bundle();
        args.putString("imageFileName", imageFileName);
        fragment.setArguments(args);
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_guide_dialog, null);

        ImageView imageView = view.findViewById(R.id.LetterImage); // Replace with your ImageView ID
        String fileName = getArguments().getString("imageFileName");
        int imageResourceId = getResources().getIdentifier(fileName,"drawable", requireContext().getPackageName());
        imageView.setImageResource(imageResourceId);
        signLetter = view.findViewById(R.id.signLetter);
        if(fileName == "sign_language_space") {
            signLetter.setText("SPACE");
        } else {
            signLetter.setText(String.valueOf(fileName.charAt(fileName.length() - 1)).toUpperCase());
        }
        builder.setView(view);
        return builder.create();
    }
}