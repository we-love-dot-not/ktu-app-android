package lt.welovedotnot.ktu_ais_app.api.models

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by simonas on 4/30/17.
 */
open class LoginResponse() {

    open var cookie: String? = null

    open var studId: String? = null

    constructor(jsonArr: JSONArray) : this() {
        for (i in 0 until jsonArr.length()) {
            val json: JSONObject = jsonArr.getJSONObject(i)
            if (json.getString("name") == "STUDCOOKIE") {
                cookie = json.getString("value")
            }
        }
    }
}