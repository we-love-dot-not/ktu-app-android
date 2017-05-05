package lt.welovedotnot.ktu_ais_app.api.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by simonas on 5/1/17.
 */

class WeekModel {
    var weekNumbers: List<Int>? = null
    var grades: RealmList<GradeModel> = RealmList()
}