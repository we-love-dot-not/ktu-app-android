package lt.welovedotnot.ktu_ais_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.models.ScreenModel

/**
 * Created by Mindaugas on 5/1/2017.
 */

class DrawerItemCustomAdapter(context: Context, var titles: List<ScreenModel>) : BaseAdapter() {
    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        if (view == null) {
            view = inflater.inflate(R.layout.drawer_list_item, parent, false)
        }

        val textViewName = view!!.findViewById(R.id.drawerItemText) as TextView
        textViewName.text = titles[position].name

        return view
    }
}
