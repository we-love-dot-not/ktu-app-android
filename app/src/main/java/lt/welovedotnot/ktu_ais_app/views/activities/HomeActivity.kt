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
import lt.welovedotnot.ktu_ais_app.views.activities.adapters.DrawerItemCustomAdapter
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.ContactsFragment
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.GradesFragment
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.MapFragment
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.ScheduleFragment


/**
 * Created by simonas on 4/30/17.
 */

class HomeActivity: AppCompatActivity() {
    lateinit var mWindowTitles: Array<String>
    lateinit var mDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mWindowTitles = resources.getStringArray(R.array.windowTitles)

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

        drawerLayout.setDrawerListener(mDrawerToggle)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }
    /** Swaps fragments in the main content view  */
    private fun selectItem(position: Int) {
        // Insert the fragment by replacing any existing fragment
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = GradesFragment()
            1 -> fragment = ContactsFragment()
            2 -> fragment = MapFragment()
            3 -> fragment = ScheduleFragment()

            else -> {
            }
        }
        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit()

        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true)
        title = mWindowTitles[position]
        drawerLayout.closeDrawer(drawerListView)
    }

    override fun setTitle(title: CharSequence) {
        val mTitle = title
        actionBar!!.title = mTitle
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

