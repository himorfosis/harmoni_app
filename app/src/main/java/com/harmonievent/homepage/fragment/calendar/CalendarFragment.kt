package com.harmonievent.homepage.fragment.calendar

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
import com.harmonievent.event.adapter.EventAdapter
import com.harmonievent.model.EventModelResponse
import com.harmonievent.network.config.AppNetwork
import com.harmonievent.network.service.EventService
import com.harmonievent.utilities.DateCore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.layout_status_failure.*
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

        fetchEvent()
        calendarViewCustom.setUseThreeLetterAbbreviation(true)
    }

    private fun setEventSelectedMonth(monthSelected: String) {

        isLog("setEventSelectedMonth")

        adapterEvent.clear()

        isLog("month : $monthSelected")

        var listEvent: MutableList<EventModelResponse.Data> = ArrayList()

        listData.forEach {
            val monthData = it.tgl_mulai.substring(0, 7)
            isLog("month data : $monthData")
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

        isLog("setCalendarData size : ${it.size}")

        it.forEach {

            val date = DateCore.selectedDate(it.tgl_mulai)
            val month = DateCore.selectedMonth(it.tgl_mulai)
            val year = DateCore.selectedYear(it.tgl_mulai)

            val dateTime = formatter.parse("$date-$month-$year")
            val eventDate = Event(R.color.gold, dateTime.time, it.judul)
            calendarViewCustom.addEvent(eventDate)
        }

    }

    private fun fetchEvent() {

        service.fetchEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                isLog("Success")
                if (it.data.isNotEmpty()) {
                    listData.addAll(it.data)
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


        val monthSelected = DateCore.convertDateToSpecificDate(dateClicked!!)
        isLog("date clicked : $monthSelected")
        for(pos in 0 until listData.size) {
            val it = listData[pos]
            if (monthSelected == it.tgl_mulai) {
                val dialog = DialogInfo(requireContext(), it.judul, "Event ini diselenggarakan bertempat di ${it.lokasi}")
                dialog.setCancelable(false)
                dialog.show()
            }
        }

    }

    override fun onMonthScroll(firstDayOfNewMonth: Date?) {
        isLog("swipe : $firstDayOfNewMonth")
        val dateMonth = dateFormatMonth.format(firstDayOfNewMonth)
        month_calendar_tv.text = dateMonth

        val monthSelected = DateCore.convertDateToMonth(firstDayOfNewMonth!!)
        isLog("month selected : $monthSelected")
        setEventSelectedMonth(monthSelected)

    }

}