package lt.welovedotnot.ktu_ais_app

import lt.welovedotnot.ktu_ais_app.models.GetGradesResponse
import org.junit.Test

/**
 * Created by simonas on 4/30/17.
 */

class GradesDiffUnitTest {
    val oldData = mutableListOf<GetGradesResponse>()
    val newData = mutableListOf<GetGradesResponse>()

    init {
        val grade1 = GetGradesResponse()
        grade1.id = "MODULE1"
        grade1.name = "MODULE1 04"
        grade1.typeId = "LB"
        grade1.type = "Labaratornis darbas"
        grade1.semesterNumber = "04"
        grade1.week = "3"
        grade1.mark = listOf("1", "2", "3")

        val grade2 = GetGradesResponse()
        grade2.id = "MODULE2"
        grade2.name = "MODULE2 04"
        grade2.typeId = "LB"
        grade2.type = "Labaratornis darbas"
        grade2.semesterNumber = "04"
        grade2.week = "2"
        grade2.mark = listOf("1", "", "3")

        val grade3 = GetGradesResponse()
        grade3.id = "MODULE3"
        grade3.name = "MODULE3 04"
        grade3.typeId = "LB"
        grade3.type = "Labaratornis darbas"
        grade3.semesterNumber = "04"
        grade3.week = "5"
        grade3.mark = listOf("", "", "1")

        oldData.add(grade1)
        oldData.add(grade2)
        oldData.add(grade3)

        val grade2Fresh = GetGradesResponse()
        grade2Fresh.id = "MODULE2"
        grade2Fresh.name = "MODULE2 04"
        grade2Fresh.typeId = "LB"
        grade2Fresh.type = "Labaratornis darbas"
        grade2Fresh.semesterNumber = "04"
        grade2Fresh.week = "2"
        grade2Fresh.mark = listOf("1", "4", "3")

        val grade3Fresh = GetGradesResponse()
        grade3Fresh.id = "MODULE3"
        grade3Fresh.name = "MODULE3 04"
        grade3Fresh.typeId = "LB"
        grade3Fresh.type = "Labaratornis darbas"
        grade3Fresh.semesterNumber = "04"
        grade3Fresh.week = "5"
        grade3Fresh.mark = listOf("", "", "7")

        newData.add(grade1)
        newData.add(grade2Fresh)
        newData.add(grade3Fresh)
    }

    @Test
    @Throws(Exception::class)
    fun login_isCorrect() {
        val diff = oldData.diff(newData)
        assert(diff.size == 2)
    }
}
