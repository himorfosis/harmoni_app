package com.harmonievent.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.harmonievent.R
import com.harmonievent.homepage.fragment.auth.Login
import com.harmonievent.homepage.fragment.auth.Register
import com.harmonievent.homepage.fragment.about.AboutFragment
import com.harmonievent.homepage.fragment.calendar.CalendarFragment
import com.harmonievent.homepage.fragment.callback.HomeCallback
import com.harmonievent.homepage.fragment.event.EventFragment
import com.harmonievent.homepage.fragment.home.HomeFragment
import com.harmonievent.homepage.fragment.service.ServiceFragment
import kotlinx.android.synthetic.main.activity_home_auth.drawer_layout
import kotlinx.android.synthetic.main.activity_home_auth.nav_view

class HomeAuthActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeCallback.OnClickItem {

    var INDEX = 0
    lateinit var toolbar: Toolbar

    companion object {
        val HOMEPAGE = 0
        val EVENT = 1
        val SERVICE = 2
        val CALENDAR = 3
        val ABOUT = 4
        val LOGIN = 5
        val REGISTER = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_auth)

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
            R.id.nav_login -> {
                loginFrag()
            }
            R.id.nav_register -> {
                registerFrag()
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true

    }

    private fun homepageFrag() {
        toolbar.title = "Harmoni"
        INDEX = HOMEPAGE
        nav_view.menu.findItem(R.id.nav_home).isChecked = true
        val fragment = HomeFragment()
        replaceFragment(fragment)
    }

    private fun eventFrag() {
        toolbar.title = "Event"
        INDEX = EVENT
        nav_view.menu.findItem(R.id.nav_event).isChecked = true
        val fragment = EventFragment()
        replaceFragment(fragment)
    }

    private fun serviceFrag() {
        toolbar.title = "Service"
        INDEX = SERVICE
        nav_view.menu.findItem(R.id.nav_service).isChecked = true
        val fragment = ServiceFragment()
        replaceFragment(fragment)
    }

    private fun calendarFrag() {
        toolbar.title = "Kalender"
        INDEX = CALENDAR
        nav_view.menu.findItem(R.id.nav_calendar).isChecked = true
        val fragment = CalendarFragment()
        replaceFragment(fragment)
    }

    private fun aboutFrag() {
        toolbar.title = "ABOUT"
        INDEX = ABOUT
        nav_view.menu.findItem(R.id.nav_about).isChecked = true
        val fragment = AboutFragment()
        replaceFragment(fragment)
    }

    private fun loginFrag() {
        toolbar.title = "Login"
        INDEX = LOGIN
        nav_view.menu.findItem(R.id.nav_login).isChecked = true
        val fragment = Login()
        replaceFragment(fragment)
    }

    private fun registerFrag() {
        toolbar.title = "Register"
        INDEX = REGISTER
        nav_view.menu.findItem(R.id.nav_register).isChecked = true
        val fragment = Register()
        replaceFragment(fragment)
    }

    private fun handleSelectedFragment(it: Int) {
        when(it) {
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
            LOGIN -> {
                loginFrag()
            }
            REGISTER -> {
                registerFrag()
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
