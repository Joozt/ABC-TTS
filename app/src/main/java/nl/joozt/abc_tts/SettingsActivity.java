package nl.joozt.abc_tts;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        bindPreferenceSummaryToValue(findPreference("alphabet_start_delay"));
        bindPreferenceSummaryToValue(findPreference("alphabet_character_delay"));
        bindPreferenceSummaryToValue(findPreference("alphabet_character_min_delay"));
        bindPreferenceSummaryToValue(findPreference("alphabet_character_max_delay"));
        bindPreferenceSummaryToValue(findPreference("alphabet_character_delay_step_size"));
        bindPreferenceSummaryToValue(findPreference("repeat_selected"));
        bindPreferenceSummaryToValue(findPreference("repeat_selected_delay"));
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        if (preference instanceof CheckBoxPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getBoolean(preference.getKey(), true));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
        }
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            preference.setSummary(value.toString());
            return true;
        }
    };

}
