package lt.hacker_house.ktu_ais

import org.junit.Test

import lt.hacker_house.ktu_ais.api.Api
import lt.hacker_house.ktu_ais.models.LoginRequest
import lt.hacker_house.ktu_ais.models.UserModel
import org.junit.Assert.*

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class LoginAPIUnitTest {
    val FAILED_LOGIN = "Bad creds"
    val FAILED_COOKIE = "Cookie is empty."

    @Test
    @Throws(Exception::class)
    fun login_isCorrect() {
        var loginResponse: UserModel? = null
        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = TestConf.USER
        loginRequest.password = TestConf.PASS

        Api.login(loginRequest) {
            loginResponse = it
            lock.countDown()
        }

        lock.await()

        assertNotNull(FAILED_LOGIN, loginResponse)
        assertNotEquals(FAILED_COOKIE, loginResponse!!.cookie!!, "")
    }
}
