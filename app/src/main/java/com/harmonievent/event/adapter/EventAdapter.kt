package com.harmonievent.event.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harmonievent.R
import com.harmonievent.entity.EventModel
import com.harmonievent.event.EventDetail
import com.harmonievent.model.EventModelResponse
import kotlinx.android.synthetic.main.item_event.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class EventAdapter : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private var listData: MutableList<EventModelResponse.Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var data = listData[position]
        val ctx = holder.itemView.context
        data.let {
            holder.bindView(it)
            holder.itemView.onClick { coro ->
                ctx.startActivity(
                    ctx.intentFor<EventDetail>(
                        "title" to it.judul,
                        "description" to it.deskripsi,
                        "image" to it.gambar,
                        "location" to it.lokasi,
                        "date_start" to it.tgl_mulai,
                        "date_end" to it.tgl_selesai
                    )
                )
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title_event_tv = itemView.title_event_tv
        val location_event_tv = itemView.location_event_tv
        val date_event_tv = itemView.date_event_tv
        val event_img = itemView.event_img

        fun bindView(data: EventModelResponse.Data) {
            title_event_tv.text = data.judul
            location_event_tv.text = data.lokasi
            date_event_tv.text = "${data.tgl_mulai} - ${data.tgl_selesai} "

            Glide.with(itemView.context)
                .load(data.gambar)
                .thumbnail(0.1f)
                .error(R.drawable.ic_broken_image)
                .into(event_img)

        }

    }

    private fun add(data: EventModelResponse.Data) {
        listData!!.add(data)
        notifyItemInserted(listData!!.size - 1)
    }

    fun addAll(data: List<EventModelResponse.Data>) {
        data.forEach {
            add(it)
        }
    }

    fun clear() {

        if (listData.isNotEmpty()) {
            listData.clear()
            notifyDataSetChanged()
        }

    }

}