package lt.welovedotnot.ktu_ais_app

import org.junit.Test

import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.models.ModulesRequest
import lt.welovedotnot.ktu_ais_app.models.UserModel
import lt.welovedotnot.ktu_ais_app.models.WeekModel
import lt.welovedotnot.ktu_ais_app.utils.LocalProps
import lt.welovedotnot.ktu_ais_app.utils.toWeekList
import org.junit.Assert.*

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class GradesAPIUnitTest {
    val FAILED_LOGIN = "Bad creds"
    val FAILED_ZERO_COUNT = "There should be more than 0 grades."
    val FAILED_PARSE = "Grades from API and Parsed count doesn't match"

    @Test
    @Throws(Exception::class)
    fun grade_isCorrect() {
        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = TestConf.USER
        loginRequest.password = TestConf.PASS

        Api.login(loginRequest) { userModel ->
            lock.countDown()
            val modulesReq = ModulesRequest()
            modulesReq.studId = userModel!!.semesterList[1].id!!.toInt()
            modulesReq.year = userModel.semesterList[1].year!!.toInt()
            Api.grades(modulesReq, userModel.cookie!!) { gradeList ->
                assertNotEquals(FAILED_ZERO_COUNT, gradeList!!.size, 0)
                val parsedData = gradeList.toWeekList(AppConf.CURRENT_SEMESTER)
                assertEquals(FAILED_PARSE, userModel.getGradeCount(), parsedData.getParsedCount())
            }
        }

        lock.await()
    }

    fun UserModel.getGradeCount(): Int
        = this.gradeList.size

    fun Collection<WeekModel>.getParsedCount(): Int {
        var parsedCount = 0
        this.forEach { week ->
            week.grades.forEach { parsedCount++ }
        }
        return parsedCount
    }
}
