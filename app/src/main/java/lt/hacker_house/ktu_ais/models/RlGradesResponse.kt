package lt.hacker_house.ktu_ais.models

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass
import lt.welovedotnot.ktu_ais_api.models.MarkModel

/**
 * Created by simonas on 5/2/17.
 */

@RealmClass
open class RlGradesResponse : RealmObject() {

    companion object {

        fun from(model: MarkModel): RlGradesResponse {
            return RlGradesResponse().apply {
                name = model.name
                id = model.id
                semester = model.semester
                moduleCode = model.module_code
                moduleName = model.module_name
                semesterNumber = model.semester_number
                language = model.language
                profestor = model.profestor
                typeId = model.typeId
                type = model.type
                week = model.week
                rlMark = model.mark.joinToString(";")
            }
        }

        fun from(modelList: List<MarkModel>): List<RlGradesResponse> {
            return mutableListOf<RlGradesResponse>().apply {
                modelList.forEach { add(RlGradesResponse.from(it)) }
            }
        }

    }

    open var name: String? = null
    open var id: String? = null
    open var semester: String? = null
    open var moduleCode: String? = null
    open var moduleName: String? = null
    open var semesterNumber: String? = null
    open var language: String? = null
    open var profestor: String? = null
    open var typeId: String? = null
    open var type: String? = null
    open var week: String? = null

    @Ignore
    open var mark: MutableList<String> = mutableListOf()
        get() {
            if (field.isEmpty()) {
                return rlMark.split(";").toMutableList()
            } else {
                return field
            }
        }

    open var rlMark: String = ""

    override fun equals(other: Any?): Boolean {
        if (other is RlGradesResponse) {
            val eqList = listOf(
                    id == other.id,
                    semesterNumber == other.semesterNumber,
                    week == other.week,
                    typeId == other.typeId
            )
            return eqList.filter { !it }.isEmpty()
        } else {
            return super.equals(other)
        }
    }

    fun diff(model: RlGradesResponse, callback: (GradeUpdateModel)->(Unit)) {
        val marksCopy = mark
        marksCopy.forEachIndexed { index, item ->
            if (model.mark.size <= index) {
                val updateModel = GradeUpdateModel(
                        name = name!!,
                        type = type!!,
                        mark = item
                )
                callback.invoke(updateModel)
            } else {
                val updatedMark = model.mark[index]
                if (updatedMark != item) {
                    val updateModel = GradeUpdateModel(
                            name = name!!,
                            type = type!!,
                            mark = item
                    )
                    callback.invoke(updateModel)
                }
            }
        }
    }
}