package com.example.signlingual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

public class GuideDialog extends AppCompatDialogFragment {

    public static GuideDialog newInstance(int imageResourceId) {
        GuideDialog fragment = new GuideDialog();
        Bundle args = new Bundle();
        args.putInt("imageResourceId", imageResourceId);
        fragment.setArguments(args);
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_guide_dialog, null);

        ImageView imageView = view.findViewById(R.id.LetterImage); // Replace with your ImageView ID
        int imageResourceId = getArguments().getInt("imageResourceId");
        imageView.setImageResource(imageResourceId);

        builder.setView(view);
        return builder.create();
    }
}