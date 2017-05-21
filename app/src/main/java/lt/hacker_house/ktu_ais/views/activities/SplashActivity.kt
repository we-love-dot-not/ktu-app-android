package lt.hacker_house.ktu_ais.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import lt.hacker_house.ktu_ais.db.User

/**
 * Created by simonas on 5/1/17.
 */

class SplashActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        User.get { userModel ->
            if (userModel == null) {
                proceedToLogin()
            } else {
                proceedToHome()
            }
        }
    }

    fun proceedToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun proceedToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
