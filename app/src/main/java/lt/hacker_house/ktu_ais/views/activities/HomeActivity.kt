package lt.hacker_house.ktu_ais.views.activities

import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import lt.hacker_house.ktu_ais.R
import kotlinx.android.synthetic.main.activity_home.*
import lt.hacker_house.ktu_ais.models.UserModel
import lt.hacker_house.ktu_ais.db.User
import lt.hacker_house.ktu_ais.adapters.DrawerItemCustomAdapter
import lt.hacker_house.ktu_ais.events.EventActivity
import lt.hacker_house.ktu_ais.events.UpdateEvent
import lt.hacker_house.ktu_ais.models.ScreenModel
import lt.hacker_house.ktu_ais.services.GetGradesIntentService
import lt.hacker_house.ktu_ais.utils.Prefs
import lt.hacker_house.ktu_ais.views.fragments.*


/**
 * Created by simonas on 4/30/17.
 */

class HomeActivity: EventActivity() {
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var mScreenList: List<ScreenModel>

    var MAIN_FRAGMENT = GradesFragment()
    var currentFragmentIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        GetGradesIntentService.startBackgroundService(this)

        mDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, 0, 0) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        drawerLayout.addDrawerListener(mDrawerToggle)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        User.get { it?.also { loadData(it) } }
    }

    override fun onUpdateEvent(event: UpdateEvent) {
        super.onUpdateEvent(event)
        loadData(event.userModel)
    }

    fun loadData(userModel: UserModel) {
        setUserModel(userModel)
        loadDrawer(userModel)

        if (currentFragmentIndex == -1) {
            selectItem(indexOfFragmentItem(MAIN_FRAGMENT))
        } else {
            selectItem(currentFragmentIndex)
        }
    }

    fun loadDrawer(model: UserModel) {
        val semesterNum = Prefs.getCurrentSemester(model).semesterString.toInt()
        mScreenList = listOf(
                ScreenModel(
                        name = getString(R.string.grades),
                        subtitle = "$semesterNum semestras",
                        fragment = GradesFragment(),
                        isEnabled = true),
                ScreenModel(
                        name = getString(R.string.contacts),
                        fragment = ContactsFragment(),
                        isEnabled = false), // TODO
                ScreenModel(
                        name = getString(R.string.map),
                        fragment = MapFragment(),
                        isEnabled = false), // TODO
                ScreenModel(
                        name = getString(R.string.schedule),
                        fragment = ScheduleFragment(),
                        isEnabled = false), // TODO
                ScreenModel(
                        name = getString(R.string.settings),
                        fragment = SettingsFragment(),
                        isEnabled = true)
        )

        // filter out stuff that is in dev stage.
        // TODO rework this before 1.0 release.
        mScreenList = mScreenList.filter { it.isEnabled == true }

        val adapter = DrawerItemCustomAdapter(this, mScreenList)
        drawerListView.adapter = adapter

        drawerListView.setOnItemClickListener { _, _, position, _ ->
            selectItem(position)
        }
    }

    fun setUserModel(model: UserModel) {
        drawerStudentCode.text = model.studId
        drawerStudentName.text = model.fullName
        val currentSemester = Prefs.getCurrentSemester(model)
        val year = currentSemester.year
        val semester = currentSemester.semesterString.toInt()
        drawerSemesterNo.text = "$semester semestras, $year"
    }

    private fun indexOfFragmentItem(fragment: Fragment): Int {
        val findLast = mScreenList.findLast { it.fragment.javaClass.name == fragment.javaClass.name}
        return mScreenList.indexOf(findLast)
    }
    /**
     *  Swaps fragments in the main content view
     */
    private fun selectItem(position: Int) {
        currentFragmentIndex = position
        if (position != -1) {
            val selectedScreen = mScreenList[currentFragmentIndex]
            setFragment(selectedScreen.fragment)
            // Highlight the selected item, update the title, and close the drawer
            drawerListView.setItemChecked(position, true)
            supportActionBar?.title = mScreenList[position].name
            supportActionBar?.subtitle = mScreenList[position].subtitle
        } else {
            setFragment(Fragment())
        }
        drawerLayout.closeDrawer(navSide)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item)
    }

}

