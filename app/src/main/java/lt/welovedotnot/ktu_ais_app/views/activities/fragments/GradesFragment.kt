package lt.welovedotnot.ktu_ais_app.views.activities.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.WeekModelRealmProxy
import kotlinx.android.synthetic.main.fragment_grades.view.*
import kotlinx.android.synthetic.main.grade_item.*

import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.api.models.GradeModel
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel
import lt.welovedotnot.ktu_ais_app.views.components.WeekItem

/**
 * Created by Mindaugas on 5/1/2017.
 */

class GradesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val view = inflater.inflate(R.layout.fragment_grades, container, false)
        val week = WeekModel()
        week.savaitesNr = "11 savaite"
        val gradeModel = GradeModel()
        gradeModel.name = "Algoritmų sudarymas ir analizė"
        gradeModel.mark = "NE"
        gradeModel.type = "Inžinerinis projektas"
        week.grades.add(gradeModel)
        week.grades.add(gradeModel)
        week.grades.add(gradeModel)
        week.grades.add(gradeModel)
        val weekItem = WeekItem(activity)
        weekItem.setModel(week)
        view.linearLayout.addView(weekItem)
        return view
    }
}
