package lt.welovedotnot.ktu_ais_app.models

/**
 * Created by simonas on 5/20/17.
 */

class SemesterInfoModel(val year: Int, val semesterString: String) {

    companion object {
        val SEPARATOR = '-'

        fun fromString(dataString: String): SemesterInfoModel {
            val dataSplit = dataString.split(SEPARATOR)
            return SemesterInfoModel(
                    year = dataSplit[0].toInt(),
                    semesterString = dataSplit[1]
            )
        }
    }

    fun toDataString(): String {
        val joinToString = mutableListOf(year.toString(), semesterString).joinToString(SEPARATOR.toString())
        return joinToString
    }
}