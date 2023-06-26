package com.example.mapa_app.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapa_app.ApiServiceBuilder.APIServiceBuilder
import com.example.mapa_app.ApiServiceBuilder.SensoresService
import com.example.mapa_app.model.SensorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorViewModel : ViewModel() {
    val sensorList: MutableLiveData<List<SensorResponse>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun fetchSensores() {
        APIServiceBuilder.create().getSensoresList().enqueue(object : Callback<List<SensorResponse>> {
            override fun onResponse(call: Call<List<SensorResponse>>, response: Response<List<SensorResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    sensorList.value = response.body()
                } else {
                    error.value = "Error en la respuesta del servidor"
                }
            }

            override fun onFailure(call: Call<List<SensorResponse>>, t: Throwable) {
                error.value = "Error en la solicitud: ${t.message}"
            }
        })
    }
}