package lt.welovedotnot.ktu_ais_app.views.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rengwuxian.materialedittext.MaterialEditText
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_main.*
import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.api.models.GradeModel
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.views.notifications.KTUNotification

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gradeUpdate = KTUNotification()

        val gradeModel = GradeModel()
        gradeModel.name = "Diskrečiosios struktūros"
        gradeModel.id = "P170B008"
        gradeModel.type = "Inžinerinis projektas"
        gradeModel.typeId = "IR"
        gradeModel.profestor = "Koordinuojantysis dėstytojas doc. M. Patašius"
        gradeModel.mark = "09"

        val weekNum: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val gradesList: RealmList<GradeModel> = RealmList(gradeModel)
        val week = WeekModel()
        week.weekNumbers = weekNum
        week.grades = gradesList
        gradeUpdate.sendUpcomingTestNotification(week, this)

        loginBtn.setOnClickListener {

            if(!credentialsEmpty()) {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                User.login(username, password) { isSuccess ->
                    if (isSuccess) {
                        onProceed()
                    } else {
                        onFailure()
                    }
                }
            }
        }
    }

    fun onProceed() {
        User.get {
            Toast.makeText(this, it?.fullName, Toast.LENGTH_SHORT).show()
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
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
