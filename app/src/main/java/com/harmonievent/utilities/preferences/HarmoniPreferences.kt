package com.harmonievent.utilities.preferences

import android.content.Context

object HarmoniPreferences {

    lateinit var default: AppPreferences
    lateinit var account: AppPreferences

    fun invoke(context: Context) {
        default = AppPreferences(context)
        account = AppPreferences(context, "account")
    }
}