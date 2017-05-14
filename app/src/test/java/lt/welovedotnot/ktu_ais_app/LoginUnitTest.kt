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

class LoginUnitTest {

    @Test
    @Throws(Exception::class)
    fun login_isCorrect() {
        var loginResponse: UserModel? = null
        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = "test_username"
        loginRequest.password = "test_password"

        Api.login(loginRequest) {
            loginResponse = it
            lock.countDown()
        }

        lock.await()

        assertNotNull("Bad creds", loginResponse)
        assertNotEquals("Cookie is empty.", loginResponse!!.cookie!!.isEmpty(), "")
    }
}
