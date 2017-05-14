package lt.welovedotnot.ktu_ais_app

import com.mcxiaoke.koi.log.logi
import org.junit.Test

import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.models.UserModel
import org.junit.Assert.*

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class LoginAPIUnitTest {
    val FAILED_LOGIN = "Bad creds"
    val FAILED_COOKIE = "Cookie is empty."

    val USERNAME = "test_username"
    val PASSWORD = "test_password"

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
        assertNotEquals(FAILED_COOKIE, loginResponse!!.cookie!!, "")
    }
}
