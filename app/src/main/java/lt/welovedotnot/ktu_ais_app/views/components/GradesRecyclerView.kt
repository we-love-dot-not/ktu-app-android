package lt.welovedotnot.ktu_ais_app.views.components

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel
import lt.welovedotnot.ktu_ais_app.adapters.WeekListAdapter
import android.support.v7.widget.LinearLayoutManager



/**
 * Created by simonas on 5/2/17.
 */

class GradesRecyclerView : RecyclerView {
    val adapter = WeekListAdapter()

    constructor(context: Context)
            : super(context) { init() }

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs) { init() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) { init() }

    private fun init() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setLayoutManager(layoutManager)
        setAdapter(adapter)
    }

    fun setModels(modelList: MutableList<WeekModel>) {
        adapter.list.clear()
        adapter.list.addAll(modelList)
        adapter.notifyDataSetChanged()
    }
}
