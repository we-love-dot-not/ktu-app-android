package lt.welovedotnot.ktu_ais_app

import android.view.ViewGroup
import lt.welovedotnot.ktu_ais_app.api.models.GetGradesResponse
import lt.welovedotnot.ktu_ais_app.api.models.GradeModel
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel

/**
 * Created by simonas on 5/2/17.
 */

fun ViewGroup.setMargin(left: Int = -1, top: Int = -1, right: Int = -1, bottom: Int = -1) {
    val params =  this.layoutParams as ViewGroup.MarginLayoutParams

    if (left != -1) {
        params.leftMargin = left
    }
    if (top != -1) {
        params.topMargin = top
    }
    if (right != -1) {
        params.rightMargin = right
    }
    if (bottom != -1) {
        params.bottomMargin = bottom
    }

    this.layoutParams = params
    this.requestLayout()
}

fun MutableList<GetGradesResponse>.toWeekList(): MutableList<WeekModel> {

    val map: HashMap<String, MutableList<GetGradesResponse>> = HashMap()

    this.filter { it.semesterNumber == "03" }.forEach {
        var get = map[it.week!!]
        if (get != null) {
            get.add(it)
        } else {
            get = mutableListOf()
            get.add(it)
        }
        map.put(it.week!!, get)
    }

    val respList = mutableListOf<WeekModel>()
    map.forEach { t, u ->
        val model = WeekModel()
        val weekList = mutableListOf<Int>()
        t.split('-').forEach {
            weekList.add(it.toInt())
        }
        model.weekNumbers = weekList
        u.forEach {
            val gradeModel = GradeModel()
            gradeModel.type = it.typeId
            gradeModel.name = it.name
            gradeModel.mark = it.rlMark
            model.grades.add(gradeModel)
        }
        respList.add(model)
    }
    respList.sortBy { it.weekNumbers?.get(0) }
    return respList
}