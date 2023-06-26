package com.example.mapa_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapa_app.ApiServiceBuilder.APIServiceBuilder
import com.example.mapa_app.R
import com.example.mapa_app.adapters.SensorAdapter
import com.example.mapa_app.model.SensorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Fragment_ListadoEstaciones : Fragment() {
    private var sensorViewModel: SensorViewModel = SensorViewModel()
    private lateinit var sensorAdapter: SensorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment__listado_estaciones, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.sensores_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sensorAdapter = SensorAdapter(emptyList())
        recyclerView.adapter = sensorAdapter


        sensorViewModel = ViewModelProvider(this)[SensorViewModel::class.java]
        sensorViewModel.sensorList.observe(viewLifecycleOwner) { sensores ->
            sensorAdapter.updateData(sensores ?: emptyList())
        }

        fetchData()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun fetchData() {
        sensorViewModel.fetchSensores()

        sensorViewModel.error.observe(viewLifecycleOwner, Observer<String> { errorMessage ->
            Log.e("API Error", "Error en la solicitud: $errorMessage")
            // Mostrar un mensaje de error al usuario
            Toast.makeText(
                requireContext(),
                "Error en la solicitud: $errorMessage",
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}





