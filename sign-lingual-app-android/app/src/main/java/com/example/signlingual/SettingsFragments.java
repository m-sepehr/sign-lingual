package com.example.signlingual;


import static com.example.signlingual.BaseActivity.userID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.signlingual.LocaleConfig;
import com.example.signlingual.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SettingsFragments extends PreferenceFragment {
    Preference btn_linkDevice,btn_unlinkDevice,btn_tutorial, btn_feedback;
    ListPreference list_language, list_customVoice;
    SwitchPreference switch_subtitles, switch_enableVoice;

    LocaleConfig localeConfig;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userRef, ip_address;
    SharedPreferences preferences;

    // On update change to "extends PreferenceFragmentCompat"
    /*
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);


        userRef = mDatabase.getReference("users").child(userID);
        ip_address = userRef.child("ip_address");
        localeConfig = new LocaleConfig(getResources());
        findPreferences();
        populatePreferenceLists();
        setDefaultValues();
        setPreferenceListeners();
    }

    private void findPreferences() {
        // Find Buttons
        btn_linkDevice = findPreference("link");
        btn_unlinkDevice = findPreference("unlink");
        btn_tutorial = findPreference("tutorial");
        btn_feedback = findPreference("feedback");
        // Find Lists
        list_language = (ListPreference) findPreference("language");
        list_customVoice = (ListPreference) findPreference("custom_voice");
        // Find Switches
        switch_subtitles = (SwitchPreference) findPreference("subtitles");
        switch_enableVoice = (SwitchPreference) findPreference("enable_voice");
    }

    private void setDefaultValues() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isVoiceEnabled = preference.getBoolean(switch_enableVoice.getKey(), false);
        list_customVoice.setEnabled(isVoiceEnabled);
    }
    private void setPreferenceListeners() {
        // Button Listeners
        btn_linkDevice.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), networkActivity.class);
            startActivity(intent);
            return true;
        });

        btn_unlinkDevice.setOnPreferenceClickListener(preference -> {
            ip_address.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        String ip_address_string = snapshot.getValue(String.class);
                        Log.d("MainActivity", "ip address is: " + ip_address_string);
                        // Rest of your code using ip_address_string
                        ip_address.setValue("");
                        Toast.makeText(getContext(), "Device unlinked", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), "No device linked", Toast.LENGTH_LONG).show();
                    }
                }
            });

            return true; // Assuming you want to consume the click event
        });

        btn_tutorial.setOnPreferenceClickListener(preference -> {
            //TODO: implement Tutorial
            // STUB
            Toast.makeText(getContext(), "Not implemented Yet", Toast.LENGTH_LONG).show();
            return true;
        });
        btn_feedback.setOnPreferenceClickListener(preference -> {
            //TODO: implement open site for feedback or email
            // STUB
            Toast.makeText(getContext(), "Not implemented Yet", Toast.LENGTH_LONG).show();
            return true;
        });
        // Switch Listeners
        switch_subtitles.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                if (preference instanceof SwitchPreference) {
                    boolean value = (Boolean) newVal;
                    Toast.makeText(getContext(), "Not implemented. Val: " + value, Toast.LENGTH_LONG).show();
                    // Maybe there is a better way to check.
                    if (value) {
                        //TODO: implement subtitles active
                        // STUB
                    } else {

                    }
                }
                Toast.makeText(getContext(), "Not implemented Yet", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        switch_enableVoice.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                if (preference instanceof SwitchPreference) {
                    boolean value = (Boolean) newVal;
                    Toast.makeText(getContext(), "Not implemented. Val: " + value, Toast.LENGTH_LONG).show();
                    // Maybe there is a better way to check.
                    if (value) {
                        // I want to make this option disappear, but cannot in the current sdk version
                        list_customVoice.setEnabled(true);

                        //TODO: implement subtitles active
                        // STUB
                    } else {
                        list_customVoice.setEnabled(false);
                    }
                }
                return true;
            }
        });
        // List listeners

        list_customVoice.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                Toast.makeText(getContext(), "No choice of languages yet.", Toast.LENGTH_LONG).show();
                //TODO: implement sign language change
                return true;
            }
        });
        list_language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                if (newVal instanceof String) {
                    localeConfig.setLocaleLang((String) newVal);
                }
                return true;
            }
        });
    }

    private void populatePreferenceLists() {
        Map<String,String> langEntries = localeConfig.getLangPreferenceDropdownEntries();
        list_language.setEntries(localeConfig.getLangDropdownEntries());
        list_language.setEntryValues(localeConfig.getLangDropdownValues());

        // TODO: Set the sign language selection and infrastructure
        String[] dummy1 = {"ASL"};
        list_customVoice.setEntries(dummy1);
        list_customVoice.setEntryValues(dummy1);
    }
}
