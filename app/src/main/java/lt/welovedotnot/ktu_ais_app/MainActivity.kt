package lt.welovedotnot.ktu_ais_app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mcxiaoke.koi.log.logd
import com.rengwuxian.materialedittext.MaterialEditText
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import lt.welovedotnot.ktu_ais_app.api.models.SemesterModel
import lt.welovedotnot.ktu_ais_app.db.User

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener {

            if(credentialsEmpty()) {
                var username = etUsername.text.toString()
                var password = etPassword.text.toString()

                User.login(username, password) { isSuccess ->
                    if (isSuccess) {
                        onSuccess()
                    } else {
                        onFailure()
                    }
                }
            }
        }
    }

    fun onSuccess() {
        User.get {
            Toast.makeText(this, it?.fullName, Toast.LENGTH_SHORT).show()
        }
    }

    fun onFailure() {
        etPassword.setText("")
        Toast.makeText(this, "Error message", Toast.LENGTH_SHORT).show()
    }

    fun credentialsEmpty(): Boolean {
        if (etUsername.isEmpty() || etPassword.isEmpty()) {
            Toast.makeText(this, "Enter credentials", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun MaterialEditText.isEmpty() = this.text.toString().isEmpty()
}
