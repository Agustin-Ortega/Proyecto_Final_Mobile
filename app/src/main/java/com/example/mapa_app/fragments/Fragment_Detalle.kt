package com.example.mapa_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapa_app.R
import com.example.mapa_app.adapters.SensorAdapter
import com.example.mapa_app.adapters.SensorHistoricAdapter
import com.example.mapa_app.model.SensorHistoricResponse

class Fragment_Detalle : Fragment() {
    private lateinit var viewModel: FragmentHistoricoViewModel
    private val sensorId: String by lazy {
        Fragment_DetalleArgs.fromBundle(requireArguments()).sensorId
    }
    private lateinit var sensorAdapter: SensorHistoricAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__historico, container, false)
        val text = view.findViewById<TextView>(R.id.tv_historicos)
        text.text = "Sensor Nro: ${sensorId.substringAfterLast(":")}"

        val recyclerView: RecyclerView = view.findViewById(R.id.historicos_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sensorAdapter = SensorHistoricAdapter(emptyList())
        recyclerView.adapter = sensorAdapter

        viewModel = ViewModelProvider(this).get(FragmentHistoricoViewModel::class.java)
        viewModel.sensorHistoric.observe(viewLifecycleOwner, Observer { sensorHistoricResponses ->
            sensorAdapter.updateData(sensorHistoricResponses ?: emptyList())
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Log.e("API Error", "Error en la solicitud: $errorMessage")
            Toast.makeText(
                requireContext(),
                "Error en la solicitud: $errorMessage",
                Toast.LENGTH_SHORT
            ).show()
        })
        viewModel.fetchSensorHistoricById(sensorId)

        return view
    }
}