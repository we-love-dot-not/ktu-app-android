package lt.hacker_house.ktu_ais.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import lt.welovedotnot.ktu_ais_api.models.LoginModel


/**
 * Created by simonas on 4/30/17.
 */

@RealmClass
open class RlUserModel : RealmObject() {

    companion object {

        fun from(model: LoginModel?): RlUserModel? {
            return if (model == null)
                null else RlUserModel().apply {
                cookie = model.studCookie
                studId = model.studentId
                fullName = model.studentName
                yearList.addAll(RlYearModel.from(model.studentSemesters))
            }
        }

    }

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
    open var yearList: RealmList<RlYearModel> = RealmList()

    open var weekList: RealmList<RlWeekModel> = RealmList()

    open var gradeList: RealmList<RlGradesResponse> = RealmList()

    open var username: String? = null

    open var password: String? = null

    open var timestamp: Long = 0L

    open var defaultSemesterDataString: String? = null

    var defaultSemester: RlSemesterInfoModel
        set(value) {
            defaultSemesterDataString = value.toDataString()
        }
        get() = RlSemesterInfoModel.fromString(defaultSemesterDataString!!)
}