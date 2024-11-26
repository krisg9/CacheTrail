package de.htw.cachetrail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.di.ServiceLocator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditTrailsViewModel : ViewModel() {

    private val trailRepository = ServiceLocator.getTrailRepository()
    val stations = emptyList<Station>()

    fun addStation(trailId: String, station: Station) {
        viewModelScope.launch {
            trailRepository.addStation(trailId, station)
        }
    }

    fun deleteStation(station: Station) {
        viewModelScope.launch {
            val trailId = trailRepository.getAllTrails().first()
                .find { trail ->
                    trail.stations.any { it.id == station.id }
                }?.id

            trailId?.let {
                trailRepository.deleteStation(it, station)
            }
        }
    }

    fun addTrail(newTrail: Trail) {
        trailRepository.addTrail(newTrail)
    }

    fun getTrailById(trailId: String): Flow<Trail?> {
        return trailRepository.getAllTrails().map { trails ->
            trails.find { it.id == trailId }
        }
    }

    fun deleteTrail(trailId: String) {
        trailRepository.deleteTrail(trailId)
    }
}
