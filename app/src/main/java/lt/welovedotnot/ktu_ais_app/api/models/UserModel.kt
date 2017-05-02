package lt.welovedotnot.ktu_ais_app.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import retrofit2.http.GET


/**
 * Created by simonas on 4/30/17.
 */

@RealmClass
open class UserModel: RealmObject() {

    @PrimaryKey // rl
    open var id = "one_id_to_rule_them_all"

    @Expose // retro
    @SerializedName("cookie") // retro
    open var cookie: String? = null

    @Expose // retro
    @SerializedName("student_id") // retro
    open var studId: String? = null

    @Expose // retro
    @SerializedName("student_name") // retro
    open var fullName: String? = null

    @Expose // retro
    @SerializedName("student_semesters") // retro
    open var semesterList: RealmList<SemesterModel>? = null

    open var gradesList: RealmList<GetGradesResponse>? = null

    open var username: String? = null

    open var password: String? = null
}