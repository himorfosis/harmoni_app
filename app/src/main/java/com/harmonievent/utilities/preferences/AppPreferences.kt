package com.harmonievent.utilities.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context, preference: String = context.packageName) {
    private val sp: SharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE)

    fun getString(field: String): String? {
        return sp.getString(field, "")
    }

    fun setString(field: String, value: String? = "") {
        sp.edit().putString(field, value).apply()
    }

    fun getBoolean(field: String): Boolean? {
        return sp.getBoolean(field, false)
    }

    fun setBoolean(field: String, value: Boolean) {
        sp.edit().putBoolean(field, value).apply()
    }

    fun getInt(field: String): Int {
        return sp.getInt(field, 0)
    }

    fun setInt(field: String, value: Int = 0) {
        sp.edit().putInt(field, value).apply()
    }

    fun clear(){
        sp.edit().clear().apply()
    }

}