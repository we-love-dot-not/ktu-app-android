package lt.welovedotnot.ktu_ais_app

import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import org.junit.Test

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class LoginUnitTest {

    @Test
    @Throws(Exception::class)
    fun login_isCorrect() {
        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = "test_username"
        loginRequest.password = "test_password"

        Api.login(loginRequest) { loginResponse ->
            if (loginResponse != null) {
                assert(loginResponse.cookie?.isEmpty()!!)

            } else {
                assert(false)
            }

            lock.countDown()
        }

        lock.await()
    }
}
