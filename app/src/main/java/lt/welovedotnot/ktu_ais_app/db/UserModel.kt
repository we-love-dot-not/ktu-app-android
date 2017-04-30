package lt.welovedotnot.ktu_ais_app.db

import io.realm.RealmObject
import lt.welovedotnot.ktu_ais_app.api.models.LoginResponse

/**
 * Created by simonas on 4/30/17.
 */

class UserModel(): RealmObject() {
    var authCookie: String? = null
    var fullName: String? = null
    var studId: String? = null

    constructor(model: LoginResponse) : this() {
        authCookie = model.cookie
        studId = model.studId
        fullName = model.fullName
    }
}
