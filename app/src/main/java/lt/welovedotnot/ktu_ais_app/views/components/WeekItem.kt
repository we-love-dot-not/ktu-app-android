package lt.welovedotnot.ktu_ais_app.views.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mcxiaoke.koi.ext.dpToPx
import kotlinx.android.synthetic.main.week_item.view.*
import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel
import android.R.attr.right
import android.R.attr.left
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.LinearLayout
import lt.welovedotnot.ktu_ais_app.setMargin


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

    fun setModel(model: WeekModel): WeekItem {
        weekNumber.text = model.savaitesNr
        gradesLinearLayout.removeAllViews()

        model.grades?.forEachIndexed { index, gradeModel ->
            val gradeView = GradeItem(context)
            gradeView.setModel(gradeModel)
            gradesLinearLayout.addView(gradeView)

            if (index < model?.grades.size-1) {
                gradeView.setMargin(bottom = 12.dpToPx())
            }
        }

        return this
    }
}
