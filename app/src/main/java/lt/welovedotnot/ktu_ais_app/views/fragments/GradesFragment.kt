package lt.welovedotnot.ktu_ais_app.views.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_grades.view.*

import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.toWeekList

/**
 * Created by Mindaugas on 5/1/2017.
 */

class GradesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_grades, null)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        User.get {
            view?.gradesListView?.setModels(it?.weekList!!.toList())
        }
    }

}
