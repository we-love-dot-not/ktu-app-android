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
import com.google.common.io.Files
import java.io.*
import java.nio.charset.StandardCharsets


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
            User.getScheduleUrl { url ->
                FileDl.download(url) { file ->
                    Log.d(TAG, file.toString())
                    try {
                        val string = file!!.readFile()
                        file.writeFile(string.replace(";CHARSET=UTF-8", ""))
                        val fileInputStream = FileInputStream(file)
                        val buildIntent = iCalParser(fileInputStream).buildIntent()
                        startActivity(buildIntent)
                    } catch (e: Exception) {
                        if (DEBUG) Log.e(TAG, "Couldn't parse", e)
                    }
                }
            }
        }
    }

    fun File.readFile(): String {
        return Files.toString(this, StandardCharsets.UTF_8)
    }

    fun File.writeFile(data: String) {
        Files.write(data, this, StandardCharsets.UTF_8)
    }
}
