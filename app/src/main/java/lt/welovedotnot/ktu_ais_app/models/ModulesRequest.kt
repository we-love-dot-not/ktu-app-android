package lt.welovedotnot.ktu_ais_app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by simonas on 4/30/17.
 */
open class ModulesRequest {

    @Expose
    @SerializedName("plano_metai")
    open var year: Int? = null

    @Expose
    @SerializedName("p_stud_id")
    open var studId: Int? = null
}