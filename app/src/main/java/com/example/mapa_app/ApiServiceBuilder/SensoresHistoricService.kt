package com.example.mapa_app.ApiServiceBuilder

import com.example.mapa_app.model.SensorHistoricResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SensoresHistoricService {
    @GET("entity/{id}")
    fun getHistoricById(@Path("id") id: String): Call<List<SensorHistoricResponse>>
}