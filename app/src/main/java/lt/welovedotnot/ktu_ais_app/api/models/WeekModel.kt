package lt.welovedotnot.ktu_ais_app.api.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by simonas on 5/1/17.
 */

@RealmClass
open class WeekModel : RealmObject() {
    open var savaitesNr: String? = null
    open var grades: RealmList<GradeModel> = RealmList()

}