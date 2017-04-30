package lt.welovedotnot.ktu_ais_app.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by simonas on 4/30/17.
 */
open class SemesterModel {
    @SerializedName("year")
    open var year: String? = null

    @SerializedName("id")
    open var id: String? = null
}