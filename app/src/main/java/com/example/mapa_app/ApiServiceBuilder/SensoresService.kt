package com.example.mapa_app.ApiServiceBuilder

import com.example.mapa_app.model.SensorResponse
import retrofit2.Call
import retrofit2.http.GET

interface SensoresService {

    @GET("entities/")
    fun getSensoresList(): Call<List<SensorResponse>>


}