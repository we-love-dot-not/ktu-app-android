package lt.hacker_house.ktu_ais.models

/**
 * Created by simonas on 5/20/17.
 */

class RlSemesterInfoModel(val year: Int, val semesterString: String) {

    companion object {
        val SEPARATOR = '-'

        fun fromString(dataString: String): RlSemesterInfoModel {
            val dataSplit = dataString.split(SEPARATOR)
            return RlSemesterInfoModel(
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