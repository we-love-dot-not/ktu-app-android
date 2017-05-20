package lt.welovedotnot.ktu_ais_app.views.fragments

import android.os.Bundle
import android.preference.PreferenceFragment
import lt.welovedotnot.ktu_ais_app.R
import android.preference.Preference
import android.content.SharedPreferences
import android.preference.ListPreference
import android.preference.CheckBoxPreference
import android.widget.ListView
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.services.GetGradesIntentService
import lt.welovedotnot.ktu_ais_app.utils.Prefs
import lt.welovedotnot.ktu_ais_app.utils.Prefs.ABOUT
import lt.welovedotnot.ktu_ais_app.utils.Prefs.LOGOUT
import lt.welovedotnot.ktu_ais_app.utils.Prefs.SELECTED_SEMESTER
import lt.welovedotnot.ktu_ais_app.utils.Prefs.SHOW_NOTIFICATION
import lt.welovedotnot.ktu_ais_app.utils.Prefs.UPDATE_INTERVAL
import lt.welovedotnot.ktu_ais_app.utils.startActivityNoBack
import lt.welovedotnot.ktu_ais_app.views.activities.SplashActivity


/**
 * Created by simonas on 5/20/17.
 */

class SettingsFragment: PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var selectedSemester: ListPreference
    lateinit var updateInterval: ListPreference
    lateinit var showNotification: CheckBoxPreference
    lateinit var logout: Preference
    lateinit var about: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)

        selectedSemester = findPreference(SELECTED_SEMESTER) as ListPreference
        updateInterval = findPreference(UPDATE_INTERVAL) as ListPreference
        showNotification = findPreference(SHOW_NOTIFICATION) as CheckBoxPreference
        logout = findPreference(LOGOUT) as Preference
        about = findPreference(ABOUT) as Preference

        User.getSemesters { userModel, entries, values ->
            selectedSemester.entries = entries
            selectedSemester.entryValues = values
            selectedSemester.value = Prefs.getCurrentSemester(userModel).toDataString()
        }

        updateInterval.entries = Prefs.UPDATE_INTERVAL_ENTRIES_NAMES
        updateInterval.entryValues = Prefs.UPDATE_INTERVAL_ENTRIES_VALUES
        updateInterval.value = Prefs.UPDATE_INTERVAL_DEFAULT

        showNotification.isChecked = Prefs.SHOW_NOTIFICATION_DEFAULT

        logout.setOnPreferenceClickListener { _ ->
            User.logout { isSuccess ->
                if (isSuccess) {
                    activity.startActivityNoBack(SplashActivity::class.java)
                }
            }
            false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list = view.findViewById(android.R.id.list) as ListView
        list.divider = null
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when(key) {
            SELECTED_SEMESTER -> User.update { _, _ -> }
            SHOW_NOTIFICATION -> GetGradesIntentService.startBackgroundService(activity)
            UPDATE_INTERVAL -> GetGradesIntentService.startBackgroundService(activity)
        }
    }
}
