package lt.welovedotnot.ktu_ais_app

import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.models.ModulesRequest
import org.junit.Test

import java.util.concurrent.CountDownLatch

/**
 * Created by simonas on 4/30/17.
 */

class ModulesUnitTest {

    @Test
    @Throws(Exception::class)
    fun login_isCorrect() {
        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = "simsan1"
        loginRequest.password = "Valentin1"

        Api.login(loginRequest) { loginResponse ->
            val body = ModulesRequest()
            body.year=2016
            body.studId=583742
            Api.modules(body, loginResponse!!.cookie!!) { modulesResp ->
                modulesResp.toString()
                lock.countDown()
            }
        }

        lock.await()
    }
}
