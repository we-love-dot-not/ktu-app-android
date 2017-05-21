package lt.hacker_house.ktu_ais

import org.junit.Test

import lt.hacker_house.ktu_ais.api.Api
import lt.hacker_house.ktu_ais.models.LoginRequest
import lt.hacker_house.ktu_ais.models.ModulesRequest
import lt.hacker_house.ktu_ais.models.UserModel
import lt.hacker_house.ktu_ais.models.WeekModel
import lt.hacker_house.ktu_ais.utils.toWeekList
import org.junit.Assert.*

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class GradesAPIUnitTest {
    val FAILED_LOGIN = "Bad creds"
    val FAILED_ZERO_COUNT = "There should be more than 0 grades."
    val FAILED_PARSE = "Grades from API and Parsed count doesn't match"
    val CURRENT_SEMESTER: String = "04"

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
            modulesReq.studId = userModel!!.yearList[1].id!!.toInt()
            modulesReq.year = userModel.yearList[1].year!!.toInt()
            Api.grades(modulesReq, userModel.cookie!!) { gradeList ->
                assertNotEquals(FAILED_ZERO_COUNT, gradeList!!.size, 0)
                val parsedData = gradeList.toWeekList(CURRENT_SEMESTER)
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
