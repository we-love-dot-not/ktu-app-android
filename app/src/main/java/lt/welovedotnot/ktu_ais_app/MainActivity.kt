package lt.welovedotnot.ktu_ais_app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import kotlinx.android.synthetic.main.activity_main.*
import com.rengwuxian.materialedittext.MaterialEditText
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import lt.welovedotnot.ktu_ais_app.views.activities.HomeActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginBtn.setOnClickListener {
            if(!credentialsEmpty()) {
                val loginRequest = LoginRequest()
                loginRequest.username = etUsername.text.toString()
                loginRequest.password = etPassword.text.toString()

                Api.login(loginRequest) { loginResponse ->
                    if (loginResponse == null) {
                        onSuccess()
                    } else {
                        onFailure()
                    }
                }
            }
        }
    }

    fun onSuccess() {
        val myIntent = Intent(this, HomeActivity::class.java)
        this.startActivity(myIntent)
    }
    fun onFailure() {
        etPassword.setText("")
        Toast.makeText(this, "Error message",
                Toast.LENGTH_SHORT).show()
    }

    fun credentialsEmpty(): Boolean {
        if (etUsername.text.toString().isEmpty() || etPassword.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter credentials",
                    Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}
