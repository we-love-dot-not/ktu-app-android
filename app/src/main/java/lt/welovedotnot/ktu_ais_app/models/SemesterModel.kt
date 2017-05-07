package lt.welovedotnot.ktu_ais_app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by simonas on 4/30/17.
 */

@RealmClass
open class SemesterModel: RealmObject() {

    @Expose // retro
    @SerializedName("id") // retro
    open var id: String? = null

    @Expose // retro
    @SerializedName("year") // retro
    open var year: String? = null
}