package com.android.example.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class StoryPreferenceFragment extends PreferenceFragment
                                                implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference searchTarget = findPreference(getString(R.string.settings_search_pref_key));
            Preference displayNum = findPreference(getString(R.string.settings_display_num_key));

            bindPreferenceSummaryToValue(searchTarget);
            bindPreferenceSummaryToValue(displayNum);
        }

        private void bindPreferenceSummaryToValue (Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String prefString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, prefString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue = o.toString();
            preference.setSummary(stringValue);
            return true;
        }
    }
}
