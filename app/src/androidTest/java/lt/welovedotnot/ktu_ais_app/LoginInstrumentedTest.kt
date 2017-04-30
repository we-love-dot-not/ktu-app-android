package lt.welovedotnot.ktu_ais_app

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.db.RealmUtils
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.db.UserModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertEquals
import java.util.concurrent.CountDownLatch

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        RealmUtils.init(appContext)

        val lock = CountDownLatch(1)
        val loginRequest = LoginRequest()
        loginRequest.username = "test_username"
        loginRequest.password = "test_password"

        Api.login(loginRequest) { loginResponse ->
            User.login(UserModel(loginResponse!!))
            var user = User.get()
            User.logout()
            user = User.get()
            lock.countDown()
        }

        lock.await()

        assertEquals("lt.welovedotnot.ktu_ais_app", appContext.packageName)
    }
}
