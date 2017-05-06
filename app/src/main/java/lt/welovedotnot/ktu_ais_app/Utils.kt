package lt.welovedotnot.ktu_ais_app

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import lt.welovedotnot.ktu_ais_app.api.models.GetGradesResponse
import lt.welovedotnot.ktu_ais_app.api.models.GradeModel
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel
import lt.welovedotnot.ktu_ais_app.views.activities.HomeActivity

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

/**
 * @param selectedSemester string of a number in this format ##. e.g 04
 */
fun Collection<GetGradesResponse>.toWeekList(selectedSemester: String): MutableList<WeekModel> {

    val map: HashMap<String, MutableList<GetGradesResponse>> = HashMap()

    this.filter { it.semesterNumber == selectedSemester }.forEach {
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
    map.forEach { key, item ->
        val model = WeekModel()
        model.weekNumbersString = key
        item.forEach {
            val gradeModel = GradeModel()
            gradeModel.type = it.typeId
            gradeModel.name = it.name
            gradeModel.mark = it.rlMark
            model.grades.add(gradeModel)
        }
        respList.add(model)
    }
    respList.sortBy { it.weekNumbers[0] }
    return respList
}

fun Activity.startActivityNoBack(target: Class<*>) {
    val intent = Intent(this, target)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    this.startActivity(intent)
}

//fun Collection<GetGradesResponse>.diff(newList: Collection<GetGradesResponse>) {
//    this.forEach { oldItem ->
//        oldItem.
//    }
//}