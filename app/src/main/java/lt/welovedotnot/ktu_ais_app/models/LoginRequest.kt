package lt.welovedotnot.ktu_ais_app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by simonas on 4/30/17.
 */
open class LoginRequest {

    @Expose
    @SerializedName("user")
    open var username: String? = null

    @Expose
    @SerializedName("pass")
    open var password: String? = null
}