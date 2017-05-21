package lt.hacker_house.ktu_ais.utils

import com.tonyodev.fetch.Fetch
import com.tonyodev.fetch.request.Request
import lt.hacker_house.ktu_ais.App
import java.io.File

/**
 * Created by simonas on 5/21/17.
 */

object FileDl {
    val SCHEDULE_FILE_NAME: String = "ScheduleFile.ics"

    fun download(url: String, callback: (File?)->(Unit)) {
        val fetch = Fetch.getInstance(App.context)
        val fileName = System.currentTimeMillis().toString() + SCHEDULE_FILE_NAME
        val request = Request(url, getCachePath(), fileName)
        val downloadId = fetch.enqueue(request)
        fetch.addFetchListener { id, status, progress, downloadedBytes, fileSize, error ->
            if (downloadId == id && status == Fetch.STATUS_DONE) {
                val filePath = fetch.getFilePath(downloadId)
                if (filePath != null) {
                    val file = File(filePath)
                    callback.invoke(file)
                } else {
                    callback.invoke(null)
                }
                fetch.release()
            }
        }
    }

    private fun getCachePath() = App.context.cacheDir.absolutePath
}