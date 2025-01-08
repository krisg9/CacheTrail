package de.htw.cachetrail.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TrailSharedPreferencesDataSource(
    context: Context,
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "trails",
        Context.MODE_PRIVATE
    )
) {
    private val gson = Gson()
    private val trails = MutableStateFlow<List<Trail>>(emptyList())

    init {
        loadTrails()
    }

    private fun loadTrails() {
        val trailsJson = sharedPreferences.getString("trails", "[]")
        val trails = gson.fromJson(trailsJson, Array<Trail>::class.java).toList()
        this.trails.value = trails
    }

    fun getAllTrails(): Flow<List<Trail>> = trails

    fun addTrail(trail: Trail) {
        val trails = trails.value.toMutableList()
        trails.add(trail)
        saveTrails(trails)
    }

    fun editTrail(trail: Trail) {
        val trails = trails.value.toMutableList()
        val index = trails.indexOfFirst { it.id == trail.id }
        if (index != -1) {
            trails[index] = trail
            saveTrails(trails)
        }
    }

    fun deleteTrail(trailId: String) {
        val trails = trails.value.toMutableList()
        trails.removeAll { it.id == trailId }
        saveTrails(trails)
    }

    fun addStation(trailId: String, station: Station) {
        val trails = trails.value.toMutableList()
        val existingTrailIndex = trails.indexOfFirst { it.id == trailId }

        if (existingTrailIndex != -1) {
            val existingTrail = trails[existingTrailIndex]
            val updatedStations = existingTrail.stations + station

            val updatedTrail = existingTrail.copy(stations = updatedStations)
            trails[existingTrailIndex] = updatedTrail

            saveTrails(trails)
        }
    }

    private fun saveTrails(trails: List<Trail>) {
        val json = gson.toJson(trails)
        sharedPreferences.edit().putString("trails", json).apply()
        this.trails.value = trails
    }

    fun deleteStation(trailId: String, station: Station) {
        val updatedTrails = trails.value.map { trail ->
            if (trail.id == trailId) {
                val updatedStations =
                    trail.stations.toMutableList().filterNot { it.id == station.id }
                trail.copy(stations = updatedStations)
            } else {
                trail
            }
        }
        saveTrails(updatedTrails)
    }
}