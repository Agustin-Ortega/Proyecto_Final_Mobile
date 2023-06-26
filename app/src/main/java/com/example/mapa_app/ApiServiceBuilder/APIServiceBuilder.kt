package com.example.mapa_app.ApiServiceBuilder

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIServiceBuilder {
    private const val BASE_URL = "http://localhost:1026/v2/"
    private const val BASE_URL_DRACO = "http://localhost:8081/"

    private val retrofit: Retrofit = buildRetrofit(BASE_URL)
    private val retrofitDraco: Retrofit = buildRetrofitWithoutHeaders(BASE_URL_DRACO)

    private fun buildRetrofit(baseUrl: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("fiware-service", "openiot")
                    .header("fiware-servicepath", "/")
                    .build()
                chain.proceed(newRequest)
            }
        }.build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buildRetrofitWithoutHeaders(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun create(): SensoresService {
        return retrofit.create(SensoresService::class.java)
    }

    fun createHistoric(): SensoresHistoricService {
        return retrofitDraco.create(SensoresHistoricService::class.java)
    }
}