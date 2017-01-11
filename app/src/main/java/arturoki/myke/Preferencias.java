package arturoki.myke;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by arturocuellar on 15/12/2016.
 */

public class Preferencias extends PreferenceFragment{ //implements SharedPreferences.OnSharedPreferenceChangeListener { no es necesario
    public static final String KEY= "blanco";//esto será util para luego ya que no permite un string si no una variable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        Preference button = (Preference)findPreference(getString(R.string.myCoolButton));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Toast.makeText(getContext(),"Teclado cambiado a rojo",Toast.LENGTH_LONG).show();
                Preferencias.this.startActivity(new Intent("android.settings.INPUT_METHOD_SETTINGS"));
                return true;
            }
        });

    }

   /* @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY)) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }*/
}