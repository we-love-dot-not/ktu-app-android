package lt.welovedotnot.ktu_ais_app.views.fragments

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_schedule.*
import lt.hacker_house.ktu_ais.BuildConfig.DEBUG
import lt.hacker_house.ktu_ais.R
import lt.hacker_house.ktu_ais.db.User
import lt.hacker_house.ktu_ais.utils.FileDl
import lt.hacker_house.ktu_ais.utils.iCalParser
import java.io.FileInputStream
import android.content.Intent.getIntent
import android.content.Intent




/**
 * Created by Mindaugas on 5/1/2017.
 */

class ScheduleFragment : Fragment() {
    val TAG = ScheduleFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_schedule, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openScheduleButton.setOnClickListener {
            FileDl.download(getScheduleDownloadUrl()) { file ->
                Log.d(TAG, file.toString())
                try {
                    startActivity(iCalParser(FileInputStream(file)).buildIntent())
                } catch (e: Exception) {
                    if (DEBUG) Log.e(TAG, "Couldn't parse", e)
                }
            }
        }
    }

    private fun getScheduleDownloadUrl(): String {
        val sb = StringBuilder("https://uais.cr.ktu.lt/ktuis/tv_rprt2.ical1?p=")
        User.get { it?.also { sb.append(it.studId) } }
        sb.append("&t=basic.ics")
        return sb.toString()
    }

}
