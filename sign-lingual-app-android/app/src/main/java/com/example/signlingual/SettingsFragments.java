package com.example.signlingual;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Map;

public class SettingsFragments extends PreferenceFragment {
    Preference btn_linkDevice, btn_tutorial, btn_feedback;
    ListPreference list_language, list_customVoice;
    SwitchPreference switch_subtitles, switch_enableVoice;

    LocaleConfig localeConfig;

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

        localeConfig = new LocaleConfig(getResources());
        findPreferences();
        populatePreferenceLists();
        setDefaultValues();
        setPreferenceListeners();
    }

    private void findPreferences() {
        // Find Buttons
        btn_linkDevice = findPreference("link");
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
            //TODO: implement connection protocols. i.e. bluetooth pairing, WiFi/Cloud/HotSpot
            // STUB
            Toast.makeText(getContext(), "Not implemented Yet", Toast.LENGTH_LONG).show();
            return true;
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
