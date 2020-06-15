package com.harmonievent.utilities

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateCore {

    fun getDateToday(): String {
        val date = SimpleDateFormat("yyyy-MM-dd")
        val today = date.format(Date())
        return today.toString()
    }

    fun convertStringToDateTime(dateData: String): Long {

        val format = SimpleDateFormat("dd-MM-yyyy")
        val date = format.parse(dateData)
        return date.time
    }

//    fun descripTimestamp(dateTime: Long): String {
//        val date = Date(dateTime)
//
//        val date = formatter.parse(it.tgl_mulai)
//
//
//        val format = SimpleDateFormat("dd-MM-yyyy")
//        val dateFormat = format.format(date)
//        return dateFormat.time
//    }

    fun getMonthNow(): String {
        val date = SimpleDateFormat("yyyy-MM")
        val today = date.format(Date())
        return today.toString()
    }

    fun convertDateToMonth(date: Date): String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM")
        return dateFormat.format(date)
    }

    fun convertDateToSpecificDate(date: Date): String {
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(date)
    }

    fun selectedDate(date: String): String {
        val setting = date.substring(date.indexOf("-") + 1)
        return setting.substring(setting.lastIndexOf("-") + 1)
    }

    fun selectedMonth(date: String): String {
        val dateOfMonth = date.substring(date.indexOf("-") + 1)
        return dateOfMonth.substring(0, dateOfMonth.indexOf("-"))
    }

    fun selectedYear(date: String): String {
        return date.substring(0, date.indexOf("-"))
    }


}