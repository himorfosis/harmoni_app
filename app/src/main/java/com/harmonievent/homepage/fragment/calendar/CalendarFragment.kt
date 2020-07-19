package com.harmonievent.homepage.fragment.calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.harmonievent.R
import com.harmonievent.dialog.DialogInfo
import com.harmonievent.dialog.DialogLoading
import com.harmonievent.entity.DataSample
import com.harmonievent.event.EventInput
import com.harmonievent.event.adapter.EventAdapter
import com.harmonievent.homepage.HomeAuthActivity
import com.harmonievent.model.EventModelResponse
import com.harmonievent.network.config.AppNetwork
import com.harmonievent.network.service.EventService
import com.harmonievent.utilities.DateCore
import com.harmonievent.utilities.preferences.HarmoniPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.layout_status_failure.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment(), CompactCalendarView.CompactCalendarViewListener {

    val service = AppNetwork.buildService(EventService::class.java)
    private val disposable = CompositeDisposable()

    private lateinit var adapterEvent: EventAdapter
    private val dateFormatMonth = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")

    private var listData: MutableList<EventModelResponse.Data> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewCustom.setListener(this)
        setAdapter()
        initUI()
    }

    private fun initUI() {

        val dateMonthName = dateFormatMonth.format(DateCore.getDateMonthNow())
        month_calendar_tv.text = dateMonthName

        fetchEvent()
        calendarViewCustom.setUseThreeLetterAbbreviation(true)

    }

    private fun inputEvent(date: String) {
        val id = HarmoniPreferences.account.getString("id")
        if (id!!.isEmpty()) {
            startActivity(intentFor<HomeAuthActivity>(
                    "HOME" to HomeAuthActivity.LOGIN
                )
            )
        } else {
            startActivity(intentFor<EventInput>(
                "date" to date
            ))
        }
    }

    private fun setEventSelectedMonth(monthSelected: String) {

        isLog("setEventSelectedMonth")
        isLog("month : $monthSelected")

        adapterEvent.clear()

        var listEvent: MutableList<EventModelResponse.Data> = ArrayList()
        listData.forEach {
            val monthData = it.tgl_mulai.substring(0, 7)
            if (monthSelected == monthData) {
                listEvent.add(it)
            }
        }

        if (listEvent.isNotEmpty()) {
            setCalendarData(listEvent)
            onSuccessfullFetch(listEvent)
        } else {
            onEventEmpty()
        }

    }

    private fun setCalendarData(it: List<EventModelResponse.Data>) {

        isLog("==== setCalendarData ====")

        it.forEach {

            val date = DateCore.selectedDate(it.tgl_mulai)
            val month = DateCore.selectedMonth(it.tgl_mulai)
            val year = DateCore.selectedYear(it.tgl_mulai)

            val dateFinal = DateCore.selectedDate(it.tgl_selesai)
            val monthinal = DateCore.selectedMonth(it.tgl_selesai)
            val yearinal = DateCore.selectedYear(it.tgl_selesai)

            val dateTimeFinish = formatter.parse("$dateFinal-$monthinal-$yearinal")
            val dateTime = formatter.parse("$date-$month-$year")

            if (it.tgl_mulai != it.tgl_selesai) {

                var dateStart = it.tgl_mulai.substring(8, 10)
                var dateFinal = it.tgl_selesai.substring(8, 10)

                val durationEvent = dateFinal.toInt() - dateStart.toInt()

                for (item in 0 until durationEvent) {
                    isLog("=== Duration ===")
                    val date = dateStart.toInt() + item
                    val selectedDateEvent = if (date > 10) {
                        formatter.parse("$date-$month-$year")
                    } else {
                        formatter.parse("0$date-$month-$year")
                    }
                    setDateCalendarViewCustom(it, selectedDateEvent)
                }
                setDateCalendarViewCustom(it, dateTimeFinish)

            } else {
                setDateCalendarViewCustom(it, dateTime)
            }

        }

    }

    private fun setDateCalendarViewCustom(it : EventModelResponse.Data, date: Date) {
        if (it.status == "Menunggu Verifikasi") {
            val eventDate = Event(Color.YELLOW, date.time, it.judul)
            calendarViewCustom.addEvent(eventDate)
        } else {
            val eventDate = Event(Color.GREEN, date.time, it.judul)
            calendarViewCustom.addEvent(eventDate)
        }
    }

    private fun fetchEvent() {

        isLog("fetchEvent")
        service.fetchEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                isLog("Success")
                if (it.data.isNotEmpty()) {
                    isLog("event size : ${it.data.size}")
                    listData.addAll(it.data)
                    isLog("date selected : ${DateCore.getMonthNow()}")
                    setEventSelectedMonth(DateCore.getMonthNow())
                } else {
                    onEventEmpty()
                }

            }, {
                onFailedFetch()
                isLog("Failed")
            }).let {
                disposable.add(it)
            }

    }

    private fun setAdapter() {

        adapterEvent = EventAdapter()
        calendar_event_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
            adapter = adapterEvent
        }
    }

    private fun onSuccessfullFetch(it: List<EventModelResponse.Data>) {

        isLog("onSuccessfullFetch")
        status_error_tv.visibility = View.GONE
        status_error_message_tv.visibility = View.GONE

        adapterEvent.addAll(it)

    }

    private fun onEventEmpty() {
        status_error_tv.visibility = View.VISIBLE
        status_error_message_tv.visibility = View.VISIBLE

        status_error_tv.text = getString(R.string.event_empty)
        status_error_message_tv.text = getString(R.string.event_empty_description)
    }

    private fun onFailedFetch() {
        status_error_tv.visibility = View.VISIBLE
        status_error_message_tv.visibility = View.VISIBLE

        status_error_tv.text = getString(R.string.failed_connection)
        status_error_message_tv.text = getString(R.string.failed_connection_message)
    }

    private fun isLog(msg: String) {
        Log.e("Calendar", msg)
    }

    override fun onDayClick(dateClicked: Date?) {

        val dateSelected = DateCore.convertDateToSpecificDate(dateClicked!!)
        val dateTimeToday = DateCore.convertToDateTime(DateCore.getDateToday())
        val dateTimeSelected = DateCore.convertToDateTime(dateSelected)

        if (dateTimeSelected > dateTimeToday) {

            var statusEvent = false
            for (pos in 0 until listData.size) {
                val it = listData[pos]

                if (it.tgl_mulai != it.tgl_selesai) {

                    var dateStart = it.tgl_mulai.substring(8, 10)
                    var dateFinal = it.tgl_selesai.substring(8, 10)

                    var monthSelected = it.tgl_mulai.substring(0, 7)

                    isLog("month selected = $monthSelected")

                    val durationEvent = dateFinal.toInt() - dateStart.toInt()

                    for (item in 0 until durationEvent) {
                        isLog("=== Duration ===")
                        val date = dateStart.toInt() + item
                        val selectedDateEvent = if (date > 10) {
                            "$monthSelected-$date"
                        } else {
                            "$monthSelected-0$date"
                        }

                        if (dateSelected == selectedDateEvent || dateSelected == it.tgl_selesai) {
                            if (it.status != "Menunggu Verifikasi") {
                                isLog("date selected : ${it.tgl_mulai}")
                                onDialog(it.judul, "Event ${it.judul} diselenggarakan bertempat di ${it.lokasi}")
                                statusEvent = true
                            }
                        }
                    }

                    if (statusEvent == true) {
                        break
                    }

                } else {
                    if (dateSelected == it.tgl_mulai || dateSelected == it.tgl_selesai) {
                        if (it.status == "Menunggu Verifikasi") {
                            statusEvent = false
                        } else {
                            isLog("date selected : ${it.tgl_mulai}")
                            onDialog(it.judul, "Event ${it.judul} diselenggarakan bertempat di ${it.lokasi}")
                            statusEvent = true
                        }
                        break
                    }
                }

                if (dateSelected == it.tgl_mulai) {
                    if (it.status == "Menunggu Verifikasi") {
                        statusEvent = false
                    } else {
                        isLog("date selected : ${it.tgl_mulai}")
                        onDialog(it.judul, "Event ${it.judul} diselenggarakan bertempat di ${it.lokasi}")
                        statusEvent = true
                    }
                    break
                }
            }

            if (statusEvent == false) {
                inputEvent(dateSelected)
            }

        } else {
            onDialog("Tanggal Kadaluarsa", "Yah, Tanggal ini tidak dapat dipilih, silahkan pilih tanggal lain")
        }

    }

    override fun onMonthScroll(firstDayOfNewMonth: Date?) {
        isLog("onMonthScroll : $firstDayOfNewMonth")
        val dateMonth = dateFormatMonth.format(firstDayOfNewMonth)

        isLog("date : $dateMonth")
        month_calendar_tv.text = dateMonth

        val monthSelected = DateCore.convertDateToMonth(firstDayOfNewMonth!!)
        isLog("month selected : $monthSelected")
        setEventSelectedMonth(monthSelected)

    }

    private fun onDialog(title: String, description: String) {
        val dialog = DialogInfo(
            requireContext(),
            title,
            description
        )
        dialog.setCancelable(false)
        dialog.show()

    }

}