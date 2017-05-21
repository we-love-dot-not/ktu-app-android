package lt.hacker_house.ktu_ais.views.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_main.*
import lt.hacker_house.ktu_ais.R
import lt.hacker_house.ktu_ais.utils.addOnViewShrinkListener
import lt.hacker_house.ktu_ais.db.User
import lt.hacker_house.ktu_ais.utils.startActivityNoBack

class LoginActivity: AbsBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener {

            if(!credentialsEmpty()) {
                loginBtn.progress = 50

                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                User.login(username, password) { isSuccess ->
                    if (isSuccess) {
                        loginBtn.progress = 100
                        onProceed()
                    } else {
                        loginBtn.progress = -1
                        onFailure()
                        Handler(Looper.getMainLooper()).postDelayed({
                            loginBtn.progress = 0
                        }, 3000)
                    }
                }
            }
        }

        mainLayout.addOnViewShrinkListener({ isSmaller ->
            if (isSmaller) {
                logoLayout.visibility = View.GONE
            } else {
                logoLayout.visibility = View.VISIBLE
            }
        })

    }

    fun onProceed() {
        startActivityNoBack(HomeActivity::class.java)
    }

    fun onFailure() {
        etPassword.setText("")
    }

    fun credentialsEmpty(): Boolean {
        if (etUsername.isEmpty() || etPassword.isEmpty()) {
            Toast.makeText(this, "Įveskite prisijungimo duomenis.", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun MaterialEditText.isEmpty() = this.text.toString().isEmpty()
}
