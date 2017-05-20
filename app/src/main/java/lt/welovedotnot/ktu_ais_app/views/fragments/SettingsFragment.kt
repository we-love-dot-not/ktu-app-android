package lt.welovedotnot.ktu_ais_app.views.fragments

import android.os.Bundle
import android.preference.PreferenceFragment
import lt.welovedotnot.ktu_ais_app.R

/**
 * Created by simonas on 5/20/17.
 */

class SettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}
