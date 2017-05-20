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
import lt.welovedotnot.ktu_ais_app.utils.startActivityNoBack
import lt.welovedotnot.ktu_ais_app.views.activities.SplashActivity


/**
 * Created by simonas on 5/20/17.
 */

class SettingsFragment: PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    val SELECTED_SEMESTER = "selected_semester"
    val UPDATE_INTERVAL = "update_interval"
    val SHOW_NOTIFICATION = "show_notification"
    val LOGOUT = "logout"
    val ABOUT = "about"

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

        User.get { userModel ->
            val semesterEntries = mutableSetOf<String>()
            val semesterValues = mutableSetOf<String>()
            userModel?.semesterList?.forEachIndexed { index, yearModel ->
                val autumNo = index + 1
                val springNo = index + 2
                val autumNoStr = String.format("%02d", autumNo)
                val springNoStr = String.format("%02d", springNo)
                val year = yearModel.year
                semesterEntries.add("$year Rudens. $autumNoStr")
                semesterValues.add(autumNoStr)

                semesterEntries.add("$year Pavasario. $springNoStr")
                semesterValues.add(springNoStr)
            }
            selectedSemester.entries = semesterEntries.toTypedArray()
            selectedSemester.entryValues = semesterValues.toTypedArray()
        }

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

    /**
     * Shows selected values
     */
    private fun showSelected() {

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when(key) {
            SELECTED_SEMESTER -> {
                User.update { _, _ -> }
            }
        }
    }
}
