package de.htw.cachetrail.data.model

data class Trail(
    val id: String,
    val name: String,
    val stations: List<Station> = emptyList(),
    val description: String = ""
)