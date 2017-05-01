package lt.welovedotnot.ktu_ais_app.views.activities

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import lt.welovedotnot.ktu_ais_app.R
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ListView.OnItemClickListener
import kotlinx.android.synthetic.main.activity_home.view.*
import lt.welovedotnot.ktu_ais_app.views.activities.adapters.DrawerItemCustomAdapter
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.ContactsFragment
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.GradesFragment
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.MapFragment
import lt.welovedotnot.ktu_ais_app.views.activities.fragments.ScheduleFragment


/**
 * Created by simonas on 4/30/17.
 */

class HomeActivity: AppCompatActivity() {
    lateinit var windowTitles: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        windowTitles = getResources().getStringArray(R.array.windowTitles);

        val adapter = DrawerItemCustomAdapter(this, R.layout.drawer_list_item, windowTitles)
        drawerListView.setAdapter(adapter)
        drawerListView.setOnItemClickListener(DrawerItemClickListener())

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
        setTitle(windowTitles[position])
        drawerLayout.closeDrawer(drawerListView)
    }

    override fun setTitle(title: CharSequence) {
        val mTitle = title
        actionBar!!.setTitle(mTitle)
    }

    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectItem(position)
        }
    }

}

