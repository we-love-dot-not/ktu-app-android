package lt.welovedotnot.ktu_ais_app

import org.junit.Test

import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.models.UserModel
import lt.welovedotnot.ktu_ais_app.utils.LocalProps
import org.junit.Assert.*

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class GradesAPIUnitTest {
    val FAILED_LOGIN = "Bad creds"
    val FAILED_ZERO_COUNT = "There should be more than 0 grades."
    val FAILED_PARSE = "Grades from API and Parsed count doesn't match"

    val USERNAME = LocalProps.getUsername()
    val PASSWORD = LocalProps.getPassword()

    @Test
    @Throws(Exception::class)
    fun login_isCorrect() {
        var loginResponse: UserModel? = null
        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = USERNAME
        loginRequest.password = PASSWORD

        Api.login(loginRequest) {
            loginResponse = it
            lock.countDown()
        }

        lock.await()

        assertNotNull(FAILED_LOGIN, loginResponse)
        assertNotEquals(FAILED_ZERO_COUNT, loginResponse!!.gradeList.size, 0)

        assertEquals(FAILED_PARSE, loginResponse!!.getGradeCount(), loginResponse!!.getParsedCount())
    }

    fun UserModel.getGradeCount(): Int
        = this.gradeList.size

    fun UserModel.getParsedCount(): Int {
        var parsedCount = 0
        this.weekList.forEach { week ->
            week.grades.forEach { parsedCount++ }
        }
        return parsedCount
    }
}
