package com.example.mapa_app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView


import androidx.lifecycle.ViewModelProvider

import com.example.mapa_app.R
import com.example.mapa_app.model.SensorResponse
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow


class Fragment_Mapa : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var mapController: MapController
    private lateinit var sensorViewModel: SensorViewModel
    private lateinit var searchAutoCompleteTextView: AutoCompleteTextView
    private var completeSensorList: List<SensorResponse> = emptyList()
    private lateinit var markerList: MutableList<Marker>
    private var selectedMarker: Marker? = null
    private var selectedEntity: SensorResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment__mapa, container, false)

        // Configurar el agente de usuario para la instancia de OSMDroid
        Configuration.getInstance().userAgentValue = "MapApp"

        // Inicializar el mapa y su controlador
        mapView = rootView.findViewById(R.id.mapView)
        searchAutoCompleteTextView = rootView.findViewById(R.id.searchAutoCompleteTextView)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.setMultiTouchControls(true)

        mapController = mapView.controller as MapController
        mapController.setCenter(GeoPoint(-41.1335, -71.3103))
        mapController.setZoom(12)

        // Configurar el botón de borrar en el AutoCompleteTextView
        searchAutoCompleteTextView.setOnTouchListener { _, event ->
            val drawableEnd = 2 // Índice del drawableEnd
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= searchAutoCompleteTextView.right - searchAutoCompleteTextView.compoundDrawables[drawableEnd].bounds.width()) {
                searchAutoCompleteTextView.text = null
                return@setOnTouchListener true
            }
            false
        }

        // Inicializar el ViewModel para los sensores
        sensorViewModel = ViewModelProvider(this).get(SensorViewModel::class.java)

        // Observar los cambios en la lista de sensores
        sensorViewModel.sensorList.observe(viewLifecycleOwner, Observer { sensorList ->
            if (sensorList.isNotEmpty()) {
                completeSensorList = sensorList
                mapView.overlays.clear()

                markerList = mutableListOf<Marker>()

                // Crear marcadores en el mapa para cada sensor
                for (sensorResponse in sensorList) {
                    val coordinates = sensorResponse.location.value.coordinates
                    if (coordinates.size >= 2) {
                        val latitude = coordinates[0]
                        val longitude = coordinates[1]
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(latitude, longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                        marker.setOnMarkerClickListener { marker, mapView ->
                            if (selectedMarker == marker) {
                                // Si se hace clic en el mismo marcador, llevar al otro fragmento
                                val navController = requireActivity().findNavController(R.id.fragmentContainerView2)
                                val action = Fragment_MapaDirections.actionFragmentMapaToFragmentDetalle(selectedEntity?.id ?: "")
                                navController.navigate(action)
                                clearSelectedMarker()
                            } else {
                                // Si se hace clic en un marcador diferente, muestrar la informacion del sensor
                                selectedMarker = marker
                                selectedEntity = sensorResponse

                                val temp = sensorResponse.temperature?.value.toString()
                                val pm1 = sensorResponse.pm1?.value.toString()
                                val pm10 = sensorResponse.pm10?.value.toString()
                                val pm25 = sensorResponse.pm25?.value.toString()
                                val rel = sensorResponse.reliability?.value.toString()
                                val mensajeCompleto = "Temperatura: $temp\npm1: $pm1\npm10: $pm10\npm25: $pm25"

                                marker.title = mensajeCompleto
                                marker.showInfoWindow()
                            }
                            true
                        }

                        mapView.overlays.add(marker)
                        markerList.add(marker)
                    }
                }

                mapView.invalidate() // Actualizar el mapa

            } else {
                // Manejar la lista de sensores vacía
                mapView.overlays.clear()
                mapView.invalidate() // Actualizar el mapa
            }
        })

        // Observar los errores en la solicitud de sensores
        sensorViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Log.e("API Error", "Error en la solicitud: $errorMessage")
            Toast.makeText(requireContext(), "Error en la solicitud: $errorMessage", Toast.LENGTH_SHORT).show()
        })

        // Obtener la lista de sensores desde la API
        sensorViewModel.fetchSensores()

        // Configurar el AutoCompleteTextView para buscar direcciones
        searchAutoCompleteTextView.threshold = 1
        searchAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = searchAutoCompleteTextView.adapter?.getItem(position) as? String
            if (selectedItem != null) {
                searchAutoCompleteTextView.setText(selectedItem, false)
                val selectedSensor = completeSensorList.find { sensor ->
                    sensor.address.value.address.streetAddress == selectedItem
                }
                if (selectedSensor != null) {
                    val coordinates = selectedSensor.location.value.coordinates
                    if (coordinates.size >= 2) {
                        val selectedMarker = markerList.find { marker ->
                            marker.position.latitude == coordinates[0] && marker.position.longitude == coordinates[1]
                        }
                        if (selectedMarker != null) {
                            mapController.animateTo(selectedMarker.position)
                        }
                    }
                }
            }
        }

        // Agregar un TextWatcher al AutoCompleteTextView para buscar direcciones mientras se escribe
        searchAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                searchAddressByStreetAddress(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return rootView
    }

    // Buscar direcciones por calle
    private fun searchAddressByStreetAddress(address: String) {
        val filteredSensors = completeSensorList.filter { sensor ->
            sensor.address.value.address.streetAddress.contains(address, ignoreCase = true)
        }

        if (filteredSensors.isNotEmpty()) {
            val suggestions = filteredSensors.map { sensor ->
                sensor.address.value.address.streetAddress
            }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions)
            searchAutoCompleteTextView.setAdapter(adapter)

            val selectedSensor = filteredSensors.first()
            val coordinates = selectedSensor.location.value.coordinates
            if (coordinates.size >= 2) {
                val selectedMarker = markerList.find { marker ->
                    marker.position.latitude == coordinates[0] && marker.position.longitude == coordinates[1]
                }
                if (selectedMarker != null) {
                    mapController.animateTo(selectedMarker.position)
                }
            }
        } else {
            Log.e("Fragment_Mapa", "No se encontraron sensores coincidentes")
        }
    }

    // Método para limpiar el marcador seleccionado
    private fun clearSelectedMarker() {
        selectedMarker?.title = null
        selectedMarker?.closeInfoWindow()
        selectedMarker = null
        selectedEntity = null
    }
}