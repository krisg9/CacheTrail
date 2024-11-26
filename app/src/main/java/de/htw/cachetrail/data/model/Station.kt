package de.htw.cachetrail.data.model

data class Station(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val question: String,
    val answer: String,
)
