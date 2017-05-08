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
import android.widget.AdapterView
import lt.welovedotnot.ktu_ais_app.models.UserModel
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.adapters.DrawerItemCustomAdapter
import lt.welovedotnot.ktu_ais_app.startActivityNoBack
import lt.welovedotnot.ktu_ais_app.services.GetGradesBGService
import lt.welovedotnot.ktu_ais_app.views.fragments.ContactsFragment
import lt.welovedotnot.ktu_ais_app.views.fragments.GradesFragment
import lt.welovedotnot.ktu_ais_app.views.fragments.MapFragment
import lt.welovedotnot.ktu_ais_app.views.fragments.ScheduleFragment
import java.util.*


/**
 * Created by simonas on 4/30/17.
 */

class HomeActivity: AppCompatActivity() {
    lateinit var mWindowTitles: Array<String>
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    val MAIN_FRAGMENT = GradesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mWindowTitles = resources.getStringArray(R.array.windowTitles)

        GetGradesBGService.startBackgroundService(this)

        val adapter = DrawerItemCustomAdapter(this, R.layout.drawer_list_item, mWindowTitles)
        drawerListView.adapter = adapter
        drawerListView.onItemClickListener = DrawerItemClickListener()

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
        val updateCal = Calendar.getInstance()
        updateCal.timeInMillis = model.timestamp
        val hour: String = updateCal.get(Calendar.HOUR_OF_DAY).toString()
        val minute: String = updateCal.get(Calendar.MINUTE).toString()
        val studentId: String = model.studId!!
        drawerStudentCode.text = "$studentId $hour:$minute"
        drawerStudentName.text = model.fullName
    }

    /** Swaps fragments in the main content view  */
    private fun selectItem(position: Int) {
        // Insert the fragment by replacing any existing fragment

        when (position) {
            0 -> setFragment(GradesFragment())
            1 -> setFragment(ContactsFragment())
            2 -> setFragment(MapFragment())
            3 -> setFragment(ScheduleFragment())
        }

        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true)
        title = mWindowTitles[position]
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

    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectItem(position)
        }
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

