package com.example.mapa_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapa_app.R
import com.example.mapa_app.model.SensorResponse


class SensorAdapter(private var sensorList: List<SensorResponse>) :
    RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_item, parent, false)
        return SensorViewHolder(view)
    }

    override fun getItemCount(): Int = sensorList.size

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensor = sensorList[position]
        holder.bind(sensor)
    }

    fun updateData(sensorResponses: List<SensorResponse>) {
        sensorList = sensorResponses
        notifyDataSetChanged()
    }

    inner class SensorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.txtNombre)
        private val ubicacionTextView: TextView = itemView.findViewById(R.id.txtUbicacion)
        private val pm1TextView: TextView = itemView.findViewById(R.id.txtPM1)
        private val pm10TextView: TextView = itemView.findViewById(R.id.txtPM10)
        private val pm25TextView: TextView = itemView.findViewById(R.id.txtPM25)
        private val temperaturaTextView: TextView = itemView.findViewById(R.id.txtTeperatura)
        private val fiaTextView: TextView = itemView.findViewById(R.id.txtFia)

        fun bind(sensor: SensorResponse) {
            nombreTextView.text = "Sensor Nro: ${sensor.id.substringAfterLast(":")}"
            ubicacionTextView.text = "Latitud: ${sensor.location.value.coordinates[0]}, Longitud: ${sensor.location.value.coordinates[1]}"
            pm1TextView.text = "PM1: ${sensor.pm1?.value ?: "-"}"
            pm10TextView.text = "PM10: ${sensor.pm10?.value ?: "-"}"
            pm25TextView.text = "PM2.5: ${sensor.pm25?.value ?: "-"}"
            temperaturaTextView.text = "Temperatura: ${sensor.temperature?.value ?: "-"}"
            fiaTextView.text = "Fiabilidad: ${sensor.reliability?.value ?: "-"}"
        }
    }
}

