package com.example.mapa_app.model

data class SensorHistoricResponse(
    val id: String,
    val recvTime: String,
    val recvTimeTs: Long,
    val address: String,
    val pm25: Double,
    val pm1: Double,
    val reliability: Double,
    val pm10: Double,
    val temperature: Double,
    val location: String,
    val dataProvider: String,
    val ownerId: String,
    val TimeInstant: String
)

