package com.harmonievent.homepage.fragment.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.harmonievent.R
import com.harmonievent.entity.DataSample
import com.harmonievent.event.adapter.EventAdapter
import com.harmonievent.event.EventInput
import com.harmonievent.homepage.HomeAuthActivity
import com.harmonievent.model.EventModelResponse
import com.harmonievent.network.config.AppNetwork
import com.harmonievent.network.service.EventService
import com.harmonievent.network.service.UserService
import com.harmonievent.utilities.DateCore
import com.harmonievent.utilities.preferences.HarmoniPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.layout_status_failure.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class EventFragment : Fragment() {

    lateinit var calendar: Calendar
    private lateinit var adapterEvent: EventAdapter
    val service = AppNetwork.buildService(EventService::class.java)
    private val disposable = CompositeDisposable()

    private var listData: MutableList<EventModelResponse.Data> = ArrayList()

    var MONTH_SELECTED = "-"
    var YEAR_SELECTED = "-"
    var CAL_YEAR_POS = 0
    var CAL_MONTH_POS = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendar = Calendar.getInstance()
        initUI()
        setAdapter()
        setSelectedDate()
        fetchEvent()

    }

    private fun initUI() {

        add_event_img.onClick {

            val id = HarmoniPreferences.account.getString("id")
            if (id!!.isEmpty()) {
                startActivity(
                    intentFor<HomeAuthActivity>(
                        "HOME" to HomeAuthActivity.LOGIN
                    )
                )
            } else {
                startActivity(intentFor<EventInput>())
            }
        }

        show_event_btn.onClick {
//            toast("Menampilkan Event $MONTH_SELECTED-$YEAR_SELECTED")
            onSuccessFetchEvent()
        }

    }

    private fun setSelectedDate() {

        isLog("setSelectedDate")
        var monthNow = if (Calendar.MONTH < 10) "0${Calendar.MONTH}" else "${Calendar.MONTH}"
        isLog("month now : $monthNow")
//        MONTH_SELECTED = monthNow

        val date = SimpleDateFormat("MM")
        val month = date.format(Date())

        MONTH_SELECTED = month
        YEAR_SELECTED = calendar.get(Calendar.YEAR).toString()

        addYearToSpinner()
        addMonthToSpinner()

        month_spinner.setSelection(calendar.get(Calendar.MONTH))
        month_spinner.setSelection(calendar.get(Calendar.MONTH))

    }

    private fun setAdapter() {
        isLog("setAdapter")

        adapterEvent = EventAdapter()
        event_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterEvent
        }
    }

    private fun fetchEvent() {

        service.fetchEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                isLog("Success")
                listData.addAll(it.data)
                onSuccessFetchEvent()
            }, {
                onFailedFetch()
                isLog("Failed")
            }).let {
                disposable.add(it)
            }

    }

    private fun onSuccessFetchEvent() {

        isLog("onSuccessFetchEvent")
        isLog("date : $YEAR_SELECTED-$MONTH_SELECTED")

        adapterEvent.clear()
        var listEvent: MutableList<EventModelResponse.Data> = ArrayList()

        listData.forEach {
            val monthData = it.tgl_mulai.substring(0, 7)
            val monthSelected = "$YEAR_SELECTED-$MONTH_SELECTED"
            isLog("month data : $monthData")
            if (monthSelected == monthData) {
                listEvent.add(it)
            }
        }

        if (listEvent.isNotEmpty()) {
            onEventAvail()
            adapterEvent.addAll(listEvent)
        } else {
            onEventEmpty()
        }


    }

    private fun addMonthToSpinner() {

        isLog("addMonthToSpinner")
        val month = DateFormatSymbols.getInstance().months
        val arrayMonth: ArrayList<String> = ArrayList()
        for (i in month.indices) {
            arrayMonth.add(month[i])
        }
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayMonth)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        month_spinner.adapter = arrayAdapter
        month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                CAL_MONTH_POS = position
                val monthSelected = position + 1
                MONTH_SELECTED = if (monthSelected < 10) "0$monthSelected" else monthSelected.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ga usah ada aksi
            }
        }

    }


    private fun addYearToSpinner() {

        isLog("addYearToSpinner")

        val listYear: MutableList<String> = ArrayList()
        val startYear = 1950
        val totalYear = 100
        for (position in 0 until totalYear) {
            val year = startYear + position
            listYear.add(year.toString())
        }

        isLog("list year size ${listYear.size}")

        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listYear)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        year_spinner.adapter = arrayAdapter
        year_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val data = parent!!.getItemAtPosition(position).toString()
                YEAR_SELECTED = data
                CAL_YEAR_POS = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ga usah ada aksi
            }
        }

        for (item in 0 until listYear.size) {

            if (YEAR_SELECTED.toInt() == listYear[item].toInt()) {
                val yearAdapter = arrayAdapter.getPosition(listYear[item])
                year_spinner.setSelection(yearAdapter)
                break
            }
        }

    }

    private fun isLog(msg: String) {
        Log.e("EventFrag", msg)
    }

    private fun onFailedFetch() {
        status_error_tv.visibility = View.VISIBLE
        status_error_message_tv.visibility = View.VISIBLE

        status_error_tv.text = getString(R.string.failed_connection)
        status_error_message_tv.text = getString(R.string.failed_connection_message)
    }

    private fun onEventAvail() {
        status_error_tv.visibility = View.GONE
        status_error_message_tv.visibility = View.GONE
    }

    private fun onEventEmpty() {
        status_error_tv.visibility = View.VISIBLE
        status_error_message_tv.visibility = View.VISIBLE

        status_error_tv.text = getString(R.string.event_empty)
        status_error_message_tv.text = getString(R.string.event_empty_description)
    }


    private fun isDisconnect() {

        status_error_tv.visibility = View.VISIBLE
        status_error_message_tv.visibility = View.VISIBLE

        status_error_tv.text = getString(R.string.disconnect)
        status_error_message_tv.text = getString(R.string.disconnect_message)
    }

}