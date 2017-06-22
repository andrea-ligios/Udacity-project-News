package com.example.android.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ana on 14/06/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            /*
             * Use the findPreference() method to get the Preference object and setup the preference
             * using a helper method called bindPreferenceSummaryToValue()
             */
            Preference order = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(order);

        }

        /**
         * Define the bindPreferenceSummaryToValue() helper method to set the current
         * NewsPreferenceFragment instance as the listener on each preference. Read the current value
         * of the preference stored in the SharedPreferences on the device, and display that in the
         * preference summary so the user can see the current value of the preference
         *
         * @param preference is the user's preferred value setting
         */
        private void bindPreferenceSummaryToValue(Preference preference) {

            preference.setOnPreferenceChangeListener(this);

            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            String preferenceString = preferences.getString(preference.getKey(), "");

            onPreferenceChange(preference, preferenceString);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();

            /*
             * Update the onPreferenceChange() method to properly update the summary of a
             * ListPreference (using the label instead of the key).
             */

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);

                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else
                preference.setSummary(stringValue);

            return true;
        }

    }
}
