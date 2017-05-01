package lt.welovedotnot.ktu_ais_app.views.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.week_item.view.*
import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel

/**
 * Created by simonas on 5/1/17.
 */

class WeekItem: RelativeLayout {
    constructor(context: Context) : super(context) { init() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    fun init() {
        inflate(context, R.layout.week_item, this)
    }

    fun setModel(model: WeekModel) {
        weekNumber.text = model.savaitesNr
        gradesLinearLayout.removeAllViews()

        model.grades?.forEach { item ->
            val gradeView = GradeItem(context)
            gradeView.setModel(item)
            gradesLinearLayout.addView(gradeView)
        }
    }
}
