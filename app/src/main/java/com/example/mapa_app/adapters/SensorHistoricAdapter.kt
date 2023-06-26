package com.example.mapa_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapa_app.R
import com.example.mapa_app.model.SensorHistoricResponse
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class SensorHistoricAdapter(private var sensorHistoricList: List<SensorHistoricResponse>) :
    RecyclerView.Adapter<SensorHistoricAdapter.SensorHistoricViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorHistoricViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.historic_item, parent, false)
        return SensorHistoricViewHolder(view)
    }

    override fun getItemCount(): Int = sensorHistoricList.size

    override fun onBindViewHolder(holder: SensorHistoricViewHolder, position: Int) {
        val sensorHistoric = sensorHistoricList[position]
        holder.bind(sensorHistoric)
    }

    fun updateData(sensorHistoricResponses: List<SensorHistoricResponse>) {
        sensorHistoricList = sensorHistoricResponses
        notifyDataSetChanged()
    }

    inner class SensorHistoricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tiempoTextView: TextView = itemView.findViewById(R.id.tiempo_value)
        private val pm1TextView: TextView = itemView.findViewById(R.id.pm1_value)
        private val pm10TextView: TextView = itemView.findViewById(R.id.pm10_value)
        private val pm25TextView: TextView = itemView.findViewById(R.id.pm25_value)
        private val temperatureTextView: TextView = itemView.findViewById(R.id.temperature_value)
        private val reliabilityTextView: TextView = itemView.findViewById(R.id.fiabilidad_value)
        fun bind(sensorHistoric: SensorHistoricResponse) {
            val timeInstant = sensorHistoric.TimeInstant
            val parsedDate = parseDate(timeInstant)
            val formattedDate = formatDate(parsedDate)

            tiempoTextView.text = formattedDate
            pm1TextView.text = sensorHistoric.pm1.toString()
            pm10TextView.text = sensorHistoric.pm10.toString()
            pm25TextView.text = sensorHistoric.pm25.toString()
            temperatureTextView.text = sensorHistoric.temperature.toString()
            reliabilityTextView.text = sensorHistoric.reliability.toString()
        }

        private fun parseDate(dateString: String): Date {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            return dateFormat.parse(dateString) ?: Date()
        }

        private fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(date)
        }
    }
}