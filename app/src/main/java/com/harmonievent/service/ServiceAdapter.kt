package com.harmonievent.service

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harmonievent.R
import com.harmonievent.model.ServiceDataModel
import kotlinx.android.synthetic.main.item_service.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class ServiceAdapter : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    private var listData: MutableList<ServiceDataModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = listData[position]
        val context = holder.itemView.context

        isLog("pos $position")

        holder.bindView(data)

        holder.itemView.onClick {
            context.startActivity(
                context.intentFor<ServiceDetails>(
                    "service" to data.title
                )
            )
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title_tv = itemView.service_title_tv
        val description_tv = itemView.service_description_tv

        fun bindView(it: ServiceDataModel) {
            title_tv.text = it.title
            description_tv.text = it.description
        }

    }

    fun addAll(data: List<ServiceDataModel>) {
        listData.addAll(data)
        notifyDataSetChanged()
    }

    private fun isLog(msg: String) {
        Log.e("Service Adapter", msg)
    }

}