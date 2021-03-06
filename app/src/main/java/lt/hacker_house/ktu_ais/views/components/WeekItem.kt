package lt.hacker_house.ktu_ais.views.components

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.mcxiaoke.koi.ext.dpToPx
import kotlinx.android.synthetic.main.week_item.view.*
import lt.hacker_house.ktu_ais.R
import lt.hacker_house.ktu_ais.models.RlWeekModel
import lt.hacker_house.ktu_ais.utils.setMargin


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

    fun setModel(model: RlWeekModel): WeekItem {
        weekNumber.text = "${model.weekNumbersString} ${context.getString(R.string.week)}"
        gradesLinearLayout.removeAllViews()

        model.grades.forEachIndexed { index, gradeModel ->
            val gradeView = GradeItem(context)
            gradeView.setModel(gradeModel)
            gradesLinearLayout.addView(gradeView)

            if (index < model.grades.size-1) {
                gradeView.setMargin(bottom = 12.dpToPx())
            }
        }

        return this
    }
}
