package lt.hacker_house.ktu_ais.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import lt.hacker_house.ktu_ais.models.RlGradesResponse
import lt.hacker_house.ktu_ais.models.RlGradeModel
import lt.hacker_house.ktu_ais.models.GradeUpdateModel
import lt.hacker_house.ktu_ais.models.RlWeekModel

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
fun Collection<RlGradesResponse>.toWeekList(selectedSemester: String): MutableList<RlWeekModel> {

    val map: HashMap<String, MutableList<RlGradesResponse>> = HashMap()

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

    val respList = mutableListOf<RlWeekModel>()
    map.forEach { (key, item) ->
        val model = RlWeekModel()
        model.weekNumbersString = key
        item.forEach {
            val gradeModel = RlGradeModel()
            gradeModel.typeId = it.typeId
            gradeModel.type = it.type
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

fun Collection<RlGradesResponse>.diff(newList: Collection<RlGradesResponse>): Collection<GradeUpdateModel> {
    val resultList = mutableListOf<GradeUpdateModel>()

    this.forEach { oldItem ->
        val newItemList = newList.findAll(oldItem)
        val oldItemList = this.findAll(oldItem)
        newItemList.forEachIndexed { index, newListItem ->
            val oldListItem = oldItemList[index]
            newListItem.diff(oldListItem) { newGrade ->
                val comp = oldListItem
                val new = newListItem
                resultList.add(newGrade)
            }
        }
    }
    return resultList
}

fun Collection<RlGradesResponse>.findAll(other: RlGradesResponse): List<RlGradesResponse> {
    val resultList = mutableListOf<RlGradesResponse>()
    this.forEach { item ->
        if (item == other) {
            resultList.add(item)
        }
    }
    return resultList
}

fun View.addOnViewShrinkListener(isSmaller: (Boolean)->(Unit)) {
    this.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        if (bottom < oldBottom) {
            // Opened
            v.postDelayed({
                isSmaller.invoke(true)
            }, 1) // 1ms delay is required.
        } else if (bottom != oldBottom) {
            // Closed
            v.postDelayed({
                isSmaller.invoke(false)
            }, 1) // 1ms delay is required.
        }
    }
}

fun Collection<RlGradesResponse>.filterSemester(semersterNum: String)
        = this.filter { it.semesterNumber == semersterNum }