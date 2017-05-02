package lt.welovedotnot.ktu_ais_app.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by simonas on 5/2/17.
 */

class GetGradesResponse {

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("semester")
    @Expose
    var semester: String? = null

    @SerializedName("module_code")
    @Expose
    var moduleCode: String? = null

    @SerializedName("module_name")
    @Expose
    var moduleName: String? = null

    @SerializedName("semester_number")
    @Expose
    var semesterNumber: String? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("profestor")
    @Expose
    var profestor: String? = null

    @SerializedName("typeId")
    @Expose
    var typeId: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("week")
    @Expose
    var week: String? = null

    @SerializedName("mark")
    @Expose
    var mark: List<String>? = null

}