package lt.welovedotnot.ktu_ais_app.views.activities

import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import lt.welovedotnot.ktu_ais_app.R
import kotlinx.android.synthetic.main.activity_home.*
import lt.welovedotnot.ktu_ais_app.models.UserModel
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.adapters.DrawerItemCustomAdapter
import lt.welovedotnot.ktu_ais_app.models.ScreenModel
import lt.welovedotnot.ktu_ais_app.utils.startActivityNoBack
import lt.welovedotnot.ktu_ais_app.services.GetGradesIntentService
import lt.welovedotnot.ktu_ais_app.views.fragments.ContactsFragment
import lt.welovedotnot.ktu_ais_app.views.fragments.GradesFragment
import lt.welovedotnot.ktu_ais_app.views.fragments.MapFragment
import lt.welovedotnot.ktu_ais_app.views.fragments.ScheduleFragment


/**
 * Created by simonas on 4/30/17.
 */

class HomeActivity: AppCompatActivity() {
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    val MAIN_FRAGMENT = GradesFragment()

    lateinit var mScreenList: List<ScreenModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        mScreenList = listOf(
                ScreenModel(
                    name = getString(R.string.grades),
                    fragment = GradesFragment(),
                    isEnabled = true),
                ScreenModel(
                    name = getString(R.string.contacts),
                    fragment = ContactsFragment(),
                    isEnabled = false),
                ScreenModel(
                    name = getString(R.string.map),
                    fragment = MapFragment(),
                    isEnabled = false),
                ScreenModel(
                    name = getString(R.string.schedule),
                    fragment = ScheduleFragment(),
                    isEnabled = false)
        )

        // filter out stuff that is in dev stage.
        // TODO rework this before 1.0 release.
        mScreenList = mScreenList.filter { it.isEnabled == true }

        GetGradesIntentService.startBackgroundService(this)

        val adapter = DrawerItemCustomAdapter(this, mScreenList)
        drawerListView.adapter = adapter

        drawerListView.setOnItemClickListener { _, _, position, _ ->
            selectItem(position)
        }

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

        drawerLogout.setOnClickListener {
            User.logout { isSuccess ->
                if (isSuccess) {
                    startActivityNoBack(SplashActivity::class.java)
                }
            }
        }

        User.get { it?.also { setUserModel(it) } }

        setFragment(MAIN_FRAGMENT)
    }

    fun setUserModel(model: UserModel) {
        drawerStudentCode.text = model.studId
        drawerStudentName.text = model.fullName
    }

    /**
     *  Swaps fragments in the main content view
     */
    private fun selectItem(position: Int) {
        // Insert the fragment by replacing any existing fragment
        val selectedScreen = mScreenList[position]
        setFragment(selectedScreen.fragment)
        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true)
        title = mScreenList[position].name
        drawerLayout.closeDrawer(navSide)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit()
    }

    override fun setTitle(title: CharSequence) {
        val mTitle = title
        actionBar?.title = mTitle
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

