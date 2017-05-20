package lt.welovedotnot.ktu_ais_app.models

import android.app.Fragment

/**
 * Created by simonas on 5/13/17.
 */

class ScreenModel(
        val name: String,
        val subtitle: String? = null,
        val fragment: Fragment,
        val isEnabled: Boolean
)

fun Collection<ScreenModel>.toStringList(): MutableList<String> {
    val stringList = mutableListOf<String>()
    this.forEach { model ->
        if (model.isEnabled) {
            stringList.add(model.name)
        }
    }
    return stringList
}