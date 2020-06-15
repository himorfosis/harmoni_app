package com.harmonievent.app

import android.app.Application
import com.bumptech.glide.Glide
import com.harmonievent.utilities.preferences.AppPreferences
import com.harmonievent.utilities.preferences.HarmoniPreferences

class HarmoniApp: Application() {

    override fun onCreate() {
        super.onCreate()
        account = AppPreferences(this, "akun")
        HarmoniPreferences.invoke(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(applicationContext).onTrimMemory(Glide.TRIM_MEMORY_RUNNING_LOW)
    }

    companion object {
        lateinit var account: AppPreferences
        fun findInAccount(key: String): String? {
            return account.getString(key)
        }

    }

}