package lt.hacker_house.ktu_ais.utils

import android.content.Intent
import android.provider.CalendarContract
import android.util.Log

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent

import java.io.InputStream


/**
 * Created by Mindaugas on 5/21/2017.
 */

class iCalParser(ics: InputStream) {

    private val calendar: Calendar
    private val event: VEvent?

    init {
        val builder = CalendarBuilder()
        calendar = builder.build(ics)
        event = findFirstEvent()

    }

    internal var DEBUG = true

    /**
     * USE THIS PROB https://codepen.io/asommer70/post/adding-multiple-android-calendar-events-at-once
     */
    fun buildIntent(): Intent {
        val i = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)

        if (DEBUG) {
            for (o in event!!.properties) {
                val property = o as Property
                Log.d(TAG, "Property [" + property.name + ", " + property.value + "]")
            }
        }

        i.putExtra(CalendarContract.Events.TITLE, getValueOrNull(Property.SUMMARY))
        i.putExtra(CalendarContract.Events.DESCRIPTION, getValueOrNull(Property.DESCRIPTION))
        i.putExtra(CalendarContract.Events.EVENT_LOCATION, getValueOrNull(Property.LOCATION))

        val start = event!!.startDate.date.time
        val end = event!!.endDate.date.time
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)

        val allDayEvent = event!!.getProperty("X-MICROSOFT-CDO-ALLDAYEVENT") != null            //Microsoft's custom field exists
                && event!!.getProperty("X-MICROSOFT-CDO-ALLDAYEVENT").value == "true"  //  and is true, or
                || end - start % 1000 * 60 * 60 * 24 == 0L                                             //the duration is an integer number of days
        i.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDayEvent)

        i.putExtra(CalendarContract.Events.RRULE, getValueOrNull(Property.RRULE))



        return i
    }

    private fun getValueOrNull(name: String): String? {
        val p = event!!.getProperty(name)
        return p?.value
    }

    private fun findFirstEvent(): VEvent? {
        for (o in calendar.components) {
            val c = o as Component
            val e = if (c is VEvent) c else null
            if (e != null) {
                return e
            }
        }
        return VEvent()
    }

    companion object {
        val TAG = iCalParser::class.java.canonicalName
    }
}
