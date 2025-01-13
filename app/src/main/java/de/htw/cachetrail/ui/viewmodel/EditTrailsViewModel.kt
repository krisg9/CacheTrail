package de.htw.cachetrail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.di.ServiceLocator
import de.htw.cachetrail.domain.ICacheTrailService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditTrailsViewModel(
    private val service: ICacheTrailService = ServiceLocator.getTrailService()
) :
    ViewModel() {

    val stations = emptyList<Station>()

    fun addStation(trailId: String, station: Station) {
        viewModelScope.launch {
            service.addStation(trailId, station)
        }
    }

    fun deleteStation(station: Station) {
        viewModelScope.launch {
            val trailId = service.getAllTrails().first()
                .find { trail ->
                    trail.stations.any { it.id == station.id }
                }?.id

            trailId?.let {
                service.deleteStation(it, station)
            }
        }
    }

    fun addTrail(newTrail: Trail) {
        service.addTrail(newTrail)
    }

    fun getTrailById(trailId: String): Flow<Trail?> {
        return service.getAllTrails().map { trails ->
            trails.find { it.id == trailId }
        }
    }

    fun deleteTrail(trailId: String) {
        service.deleteTrail(trailId)
    }
}
