package lt.welovedotnot.ktu_ais_app.utils

import java.util.*

/**
 * Created by simonas on 5/16/17.
 */

object LocalProps {
    val PROPS_FILE = "local.properties"
    val PROP_PASS = "pass"
    val PROP_USER = "user"

    fun getUsername()
            = getProp(PROP_USER)

    fun getPassword()
            = getProp(PROP_PASS)

    private fun getProp(key: String)
            = getPropStream().getProperty(key)

    private fun getPropStream(): Properties {
        val  properties = Properties()
        val inputStream = this.javaClass.classLoader.getResourceAsStream(PROPS_FILE)
        properties.load(inputStream)
        return properties
    }
}