package com.harmonievent.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harmonievent.R
import com.harmonievent.model.ServiceDataModel
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.android.synthetic.main.toolbar_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ServiceDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        setToolbar()
        initUI()

    }

    private fun initUI() {

        val data = intent.getStringExtra("service")

        when (data) {
            ServiceData.ServiceOrganizer -> {
                showDataService(ServiceData.eventOrganizer())
            }

            ServiceData.ServiceManagement -> {
                showDataService(ServiceData.eventManagement())
            }

            ServiceData.ServiceEquipmentRental -> {
                showDataService(ServiceData.eventEquipmentRental())
            }

            ServiceData.ServiceAdvertising -> {
                showDataService(ServiceData.advertising())
            }

            ServiceData.ServiceMultimedia -> {
                showDataService(ServiceData.multimediaService())
            }

        }

    }

    private fun showDataService(it: ServiceDataModel) {

        service_title_tv.text = it.title
        service_description_tv.text = it.description

    }

    private fun setToolbar() {

        back_bar_btn.onClick {
            finish()
        }

        title_bar_tv.text = "Service Detail"

    }

}
