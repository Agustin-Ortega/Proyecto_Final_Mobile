package com.example.mapa_app.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapa_app.ApiServiceBuilder.APIServiceBuilder
import com.example.mapa_app.model.SensorHistoricResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentHistoricoViewModel : ViewModel() {
    val sensorHistoric: MutableLiveData<List<SensorHistoricResponse>?> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun fetchSensorHistoricById(sensorId: String) {
        if (sensorId.isNullOrEmpty()) {
            error.value = "ID del sensor no especificado"
            return
        }

        val service = APIServiceBuilder.createHistoric()
        val call = service.getHistoricById(sensorId)

        call.enqueue(object : Callback<List<SensorHistoricResponse>> {
            override fun onResponse(
                call: Call<List<SensorHistoricResponse>>,
                response: Response<List<SensorHistoricResponse>>
            ) {
                if (response.isSuccessful) {
                    val historicData = response.body()
                    sensorHistoric.value = historicData
                } else {
                    error.value = "Error en la respuesta del servidor"
                }
            }

            override fun onFailure(call: Call<List<SensorHistoricResponse>>, t: Throwable) {
                error.value = "Error al conectar al servidor: ${t.message}"
            }
        })
    }
}