package lt.welovedotnot.ktu_ais_app.views.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tonyodev.fetch.Fetch
import com.tonyodev.fetch.request.Request
import kotlinx.android.synthetic.main.fragment_schedule.*
import lt.hacker_house.ktu_ais.R
import lt.hacker_house.ktu_ais.db.User

import java.io.File
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus


/**
 * Created by Mindaugas on 5/1/2017.
 */

class ScheduleFragment : Fragment() {

    val ScheduleFileName: String = "ScheduleFile.ics"
    val CachePath: String = "/data/user/0/lt.hacker_house.ktu_ais/cache/"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val fetch = Fetch.getInstance(activity)
        if(!scheduleExists()) {
            var downloadId = -1L
            fetch.addFetchListener { id, status, progress, downloadedBytes, fileSize, error ->
                if (downloadId === id && status == Fetch.STATUS_DONE) {
                    fetch.release()
                }
            }
            downloadId = fetch.startDownload()
        }
        return inflater!!.inflate(R.layout.fragment_schedule, container, false)
        OpenScheduleButton.setOnClickListener {

        }
    }

    private fun GetScheduleDownloadUrl(): String {
        val sb = StringBuilder("https://uais.cr.ktu.lt/ktuis/tv_rprt2.ical1?p=")
        User.get { it?.also { sb.append(it.studId) } }
        sb.append("&t=basic.ics")
        return sb.toString()
    }

    private fun Fetch.startDownload() : Long {
        val request = Request(GetScheduleDownloadUrl(), CachePath, ScheduleFileName)
        return this.enqueue(request)
    }

    private fun scheduleExists(): Boolean {
        val outputDir = activity.cacheDir
        var file = File(outputDir.toURI().toString() + ScheduleFileName)
        return file.exists()
    }
}
