package arturoki.myke;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;

/**
 * Created by arturocuellar on 15/12/2016.
 */

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferencias()).commit();

    }
}