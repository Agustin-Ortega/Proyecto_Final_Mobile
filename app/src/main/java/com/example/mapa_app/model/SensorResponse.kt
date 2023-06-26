package com.example.mapa_app.model
data class SensorResponse(
    val id: String,
    val type: String,
    val TimeInstant: TimeInstant,
    val address: Address,
    val dataProvider: DataProvider,
    val location: Location,
    val ownerId: OwnerId,
    val pm1: Measurement?,
    val pm10: Measurement?,
    val pm25: Measurement?,
    val reliability: Measurement?,
    val temperature: Measurement?
)

data class TimeInstant(
    val type: String,
    val value: String,
    val metadata: Metadata
)

data class Address(
    val type: String,
    val value: AddressValue,
    val metadata: Metadata
)

data class AddressValue(
    val address: AddressDetails
)

data class AddressDetails(
    val streetAddress: String,
    val addressLocality: String,
    val addressRegion: String
)

data class DataProvider(
    val type: String,
    val value: String,
    val metadata: Metadata
)

data class Location(
    val type: String,
    val value: Coordinates,
    val metadata: Metadata
)

data class Coordinates(
    val coordinates: List<Double>
)

data class OwnerId(
    val type: String,
    val value: String,
    val metadata: Metadata
)

data class Measurement(
    val type: String,
    val value: Float?,
    val metadata: Metadata
)

data class Metadata(
    val metadata: Any
)