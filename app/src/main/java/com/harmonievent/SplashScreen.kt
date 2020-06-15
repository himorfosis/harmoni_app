package com.harmonievent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.harmonievent.app.HarmoniApp
import com.harmonievent.homepage.HomeActivity
import com.harmonievent.homepage.HomeAuthActivity
import com.harmonievent.utilities.preferences.HarmoniPreferences
import org.jetbrains.anko.intentFor

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val statusLogin = HarmoniPreferences.account.getString("name")
        Handler().postDelayed(object : Thread() {
            override fun run() {
                isLog("status : $statusLogin")
                if (statusLogin == "") {
                    startActivity(intentFor<HomeAuthActivity>())
                } else {
                    startActivity(intentFor<HomeActivity>())
                }
            }
        }, 1000)

    }

    private fun isLog(msg: String) {
        Log.e("Splash", msg)
    }

}
