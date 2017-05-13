package lt.welovedotnot.ktu_ais_app.models

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass

/**
 * Created by simonas on 5/1/17.
 */

@RealmClass
open class GradeModel: RealmObject() {
    open var name: String? = null
    open var id: String? = null
    open var profestor: String? = null
    open var typeId: String? = null
    open var type: String? = null
    open var mark: String? = null

    companion object {
        @Ignore
        val EMPTY_MARK = "–"
    }
}