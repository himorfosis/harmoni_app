package com.harmonievent.homepage.fragment.home

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harmonievent.R
import com.harmonievent.homepage.HomeActivity
import com.harmonievent.homepage.HomeAuthActivity
import com.harmonievent.homepage.fragment.about.AboutFragment
import com.harmonievent.homepage.fragment.calendar.CalendarFragment
import com.harmonievent.homepage.fragment.event.EventFragment
import com.harmonievent.homepage.fragment.service.ServiceFragment
import com.harmonievent.utilities.preferences.HarmoniPreferences
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

    }

    private fun initUI() {

        eventBtn.onClick {
            handleSelectedButton(HomeActivity.EVENT)
        }

        serviceBtn.onClick {
            handleSelectedButton(HomeActivity.SERVICE)
        }

        aboutBtn.onClick {
            handleSelectedButton(HomeActivity.ABOUT)
        }

        calendarBtn.onClick {
            handleSelectedButton(HomeActivity.CALENDAR)
        }

    }

    private fun handleSelectedButton(data: Int) {

        val statusLogin = HarmoniPreferences.account.getString("name")
        isLog("status login : $statusLogin")
        if (statusLogin == "") {
            startActivity(intentFor<HomeAuthActivity>(
                "HOME" to data)
            )
        } else {
            startActivity(intentFor<HomeActivity>(
                "HOME" to data)
            )
        }
    }


    private fun replaceFragment(fragment: Fragment) {

        isLog("ReplaceFragment")

        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.frame_container, fragment)
        ft.commit()

    }

    private fun isLog(msg: String) {
        Log.e("HomeFrag", msg)
    }

}