package lt.welovedotnot.ktu_ais_app.db

import io.realm.RealmObject

/**
 * Created by simonas on 4/30/17.
 */

open class User: RealmObject() {
    open var authCookie: String? = null
    open var fullName: String? = null
    open var studId: String? = null

}
