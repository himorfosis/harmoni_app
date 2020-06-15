package com.harmonievent.homepage.fragment.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.harmonievent.R
import com.harmonievent.service.ServiceAdapter
import com.harmonievent.service.ServiceData
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment : Fragment() {

    lateinit var adapterService: ServiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

    }

    private fun setAdapter() {

        adapterService = ServiceAdapter()
        service_recycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = adapterService
        }

        adapterService.addAll(ServiceData.listDataService())

    }
}