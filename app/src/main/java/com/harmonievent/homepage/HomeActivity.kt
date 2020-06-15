package com.harmonievent.homepage

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.harmonievent.R
import com.harmonievent.app.HarmoniApp
import com.harmonievent.homepage.fragment.auth.Login
import com.harmonievent.homepage.fragment.about.AboutFragment
import com.harmonievent.homepage.fragment.calendar.CalendarFragment
import com.harmonievent.homepage.fragment.callback.HomeCallback
import com.harmonievent.homepage.fragment.event.EventFragment
import com.harmonievent.homepage.fragment.home.HomeFragment
import com.harmonievent.homepage.fragment.service.ServiceFragment
import com.harmonievent.utilities.preferences.HarmoniPreferences
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.drawer_layout
import kotlinx.android.synthetic.main.activity_home.nav_view
import kotlinx.android.synthetic.main.activity_home_auth.*
import org.jetbrains.anko.intentFor

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeCallback.OnClickItem {

    lateinit var toolbar: Toolbar
    var INDEX = 0

    companion object {
        val HOMEPAGE = 0
        val EVENT = 1
        val SERVICE = 2
        val CALENDAR = 3
        val ABOUT = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar = findViewById(R.id.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, 0, 0
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        checkParseLayout()
        setSupportActionBar(toolbar)

    }

    private fun checkParseLayout() {
        val data = intent.getIntExtra("HOME", 0)
        handleSelectedFragment(data)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home -> {
                homepageFrag()
            }
            R.id.nav_event -> {
                eventFrag()
            }
            R.id.nav_service -> {
                serviceFrag()
            }

            R.id.nav_calendar -> {
                calendarFrag()
            }
            R.id.nav_about -> {
                aboutFrag()
            }
            R.id.nav_logout -> {
                // save data preferences
                HarmoniPreferences.account.setString("name", "")
                HarmoniPreferences.account.setString("password", "")
                HarmoniPreferences.account.setString("email", "")
                HarmoniPreferences.account.setString("phone", "")
                HarmoniPreferences.account.setString("id", "")

                startActivity(
                    intentFor<HomeAuthActivity>(
                        "HOME" to HomeAuthActivity.LOGIN
                    )
                )
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun homepageFrag() {
        toolbar.title = "Harmoni"
        INDEX = HOMEPAGE
        nav_view.menu.getItem(HOMEPAGE).isChecked = true
        val fragment = HomeFragment()
        replaceFragment(fragment)
    }

    private fun eventFrag() {
        toolbar.title = "Event"
        INDEX = EVENT
        nav_view.menu.getItem(EVENT).isChecked = true
        val fragment = EventFragment()
        replaceFragment(fragment)
    }

    private fun serviceFrag() {
        toolbar.title = "Service"
        INDEX = SERVICE
        nav_view.menu.getItem(SERVICE).isChecked = true
        val fragment = ServiceFragment()
        replaceFragment(fragment)
    }

    private fun calendarFrag() {
        toolbar.title = "Kalender"
        INDEX = CALENDAR
        nav_view.menu.getItem(CALENDAR).isChecked = true
        val fragment = CalendarFragment()
        replaceFragment(fragment)
    }

    private fun aboutFrag() {
        toolbar.title = "Tentang"
        INDEX = ABOUT
        nav_view.menu.getItem(ABOUT).isChecked = true
        val fragment = AboutFragment()
        replaceFragment(fragment)
    }

    private fun handleSelectedFragment(it: Int) {
        when (it) {
            HOMEPAGE -> {
                homepageFrag()
            }
            EVENT -> {
                eventFrag()
            }
            SERVICE -> {
                serviceFrag()
            }
            CALENDAR -> {
                calendarFrag()
            }
            ABOUT -> {
                aboutFrag()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (INDEX == 0) {
            finishAffinity()
        } else {
            homepageFrag()
        }
    }

    override fun onItemClicked(data: Int) {
        handleSelectedFragment(data)
    }


}
