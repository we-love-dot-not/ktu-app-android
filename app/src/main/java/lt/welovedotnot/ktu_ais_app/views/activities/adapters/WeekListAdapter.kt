package lt.welovedotnot.ktu_ais_app.views.activities.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel
import lt.welovedotnot.ktu_ais_app.views.components.WeekItem

/**
 * Created by simonas on 5/2/17.
 */

class WeekListAdapter : RecyclerView.Adapter<WeekListAdapter.WeekVH>() {
    val list: MutableList<WeekModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekVH? {
        val view = WeekItem(parent.context)
        val vh = WeekVH(view)
        return vh
    }

    override fun onBindViewHolder(holder: WeekVH, position: Int) {
        holder.weekView.setModel(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class WeekVH(val weekView: WeekItem) : RecyclerView.ViewHolder(weekView)
}
