package com.harmonievent.event

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.harmonievent.R
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.toolbar_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class EventDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        setToolbar()
        initUI()

    }

    private fun initUI() {

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val image = intent.getStringExtra("image")
        val location = intent.getStringExtra("location")
        val dateStart = intent.getStringExtra("date_start")
        val dateEnd = intent.getStringExtra("date_end")

        title_event_tv.text = title
        deskription_event_tv.text = description
        location_event_tv.text = "Lokasi : $location"
        date_event_tv.text = "Tanggal : $dateStart - $dateEnd"

        Glide.with(this).load(image)
            .thumbnail(0.1f)
            .error(R.drawable.ic_broken_image)
            .into(image_event_img)

    }

    private fun setToolbar() {

        back_bar_btn.onClick {
            finish()
        }

        title_bar_tv.text = "Detail Event"

    }

}
